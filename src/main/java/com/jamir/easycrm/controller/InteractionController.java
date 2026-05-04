package com.jamir.easycrm.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.Interaction;
import com.jamir.easycrm.model.InteractionStatus;
import com.jamir.easycrm.model.InteractionType;
import com.jamir.easycrm.service.CustomerService;
import com.jamir.easycrm.service.InteractionService;

import jakarta.validation.Valid;

@Controller
public class InteractionController {

	@Autowired
	private CustomerService cs;
	@Autowired
	private InteractionService is;

	@GetMapping("/interacoes")
	public ModelAndView interactions(
			@RequestParam(name = "d1", required = false) LocalDate d1,
			@RequestParam(name = "d2", required = false) LocalDate d2,
			@RequestParam(name = "status", required = false) String status,
			@AuthenticationPrincipal UserPrincipal user) {
		ModelAndView mv = new ModelAndView("interacoes/page");
		if (status != null && status.isBlank()) {
			status = null;
		}
		if (d1 != null && d2 != null) {
			mv.addObject("interactions", is.findBetweenDatesAndStatus(d1, d2, status, user.getUser().getIduser()));
		} else {
			mv.addObject("interactions", is.findByUser(user.getUser()));
		}
		mv.addObject("types", InteractionType.values());
		mv.addObject("statuses", InteractionStatus.values());
		mv.addObject("customers", cs.findByUser(user.getUser()));
		return mv;
	}

	@PostMapping("/interacoes/create")
	public String create(@Valid Interaction i, BindingResult result, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserPrincipal user) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error",
					"Erro ao validar os dados da interação. Verifique as informações e tente novamente.");
			return "redirect:/interacoes";
		}
		i.setUser(user.getUser());
		is.create(i);
		return "redirect:/interacoes";
	}

	@GetMapping("/interacoes/update/{id}")
	public ModelAndView update(
			@PathVariable(name = "id") Long id,
			@AuthenticationPrincipal UserPrincipal user) {
		return is.findById(id).map(interactionFound -> {
			if (!interactionFound.getCustomer().getUser().getIduser().equals(user.getUser().getIduser())) {
				return new ModelAndView("utils/unauthorized");
			}
			ModelAndView mv = new ModelAndView("interacoes/update");
			mv.addObject("interaction", interactionFound);
			mv.addObject("types", InteractionType.values());
			mv.addObject("statuses", InteractionStatus.values());
			mv.addObject("customers", cs.findAll());
			return mv;
		}).orElseGet(() -> {
			return new ModelAndView("redirect:/interacoes");
		});
	}

	@PostMapping("/interacoes/update/{id}")
	public String update(
			@PathVariable(name = "id") Long id,
			@Valid Interaction i,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UserPrincipal user) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error",
					"Erro ao validar os dados da interação. Verifique as informações e tente novamente.");
			return "redirect:/interacoes/update/" + id;
		}
		is.update(id, i, user.getUser());
		return "redirect:/interacoes";
	}
}