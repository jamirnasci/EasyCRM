package com.jamir.easycrm.controller;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.Interaction;
import com.jamir.easycrm.model.InteractionStatus;
import com.jamir.easycrm.model.InteractionType;
import com.jamir.easycrm.service.CustomerService;
import com.jamir.easycrm.service.InteractionService;

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
			@RequestParam(name = "status", required = false) String status) {
		ModelAndView mv = new ModelAndView("interacoes/page");
		if (status != null && status.isBlank()) {
			status = null;
		}
		if (d1 != null && d2 != null) {			
			mv.addObject("interactions", is.findBetweenDatesAndStatus(d1, d2, status));
		} else {
			mv.addObject("interactions", is.findAll());
		}
		mv.addObject("types", InteractionType.values());
		mv.addObject("statuses", InteractionStatus.values());
		mv.addObject("customers", cs.findAll());
		return mv;
	}

	@PostMapping("/interacoes/create")
	public String create(Interaction i, @AuthenticationPrincipal UserPrincipal user) {
		i.setUser(user.getUser());
		return is.create(i).map(savedInteraction -> {
			String msg = "Interação agendada com sucesso";
			return "redirect:/interacoes?msg=" + msg;
		}).orElseGet(() -> {
			String msg = "Falha ao agendar interação";
			return "redirect:/interacoes?msg=" + msg;
		});
	}
}