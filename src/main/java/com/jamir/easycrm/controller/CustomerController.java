package com.jamir.easycrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.CustomerStatus;
import com.jamir.easycrm.service.CustomerService;

import jakarta.validation.Valid;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService cs;
	
	@GetMapping("/clientes")
	public ModelAndView clientes(
		@RequestParam(name = "search", required = false) String search,
		@AuthenticationPrincipal UserPrincipal user
	) {
		if(search != null) {
			List<Customer> loadedCustomers = cs.search(search, user.getUser().getIduser());
			ModelAndView mv = new ModelAndView("clientes/page");
			mv.addObject("statuses", CustomerStatus.values());
			mv.addObject("customers", loadedCustomers);
			return mv;
		}
		List<Customer> loadedCustomers = cs.findByUser(user.getUser());
		ModelAndView mv = new ModelAndView("clientes/page");
		mv.addObject("statuses", CustomerStatus.values());
		mv.addObject("customers", loadedCustomers);
		return mv;
	}
	@PostMapping("/clientes/create")
	public String create(@Valid Customer c, BindingResult result, @AuthenticationPrincipal UserPrincipal user, RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Falha ao validar os dados do cliente. Verifique os dados e tente novamente.");
			return "redirect:/clientes";
		}
		c.setUser(user.getUser());
		cs.create(c);
		return "redirect:/clientes";
	}
	@GetMapping("/clientes/update/{id}")
	public ModelAndView update(
		@PathVariable(name = "id") Long id,
		@AuthenticationPrincipal UserPrincipal user
	) {
		return cs.findById(id).map(customerFound ->{
			if(!customerFound.getUser().getIduser().equals(user.getUser().getIduser())) {
				return new ModelAndView("utils/unauthorized");
			}
			ModelAndView mv = new ModelAndView("clientes/update");
			mv.addObject("customer", customerFound);
			mv.addObject("statuses", CustomerStatus.values());
			return mv;
		}).orElseGet(()->{
			return new ModelAndView("redirect:/clientes");
		});		
	}
	@PostMapping("/clientes/update/{id}")
	public String update(@PathVariable(name = "id") Long id, @Valid Customer c, BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Falha ao validar os dados do cliente. Verifique os dados e tente novamente.");
			return "redirect:/clientes/update/" + id;
		}
		cs.update(id, c);
		return "redirect:/clientes";
	}
	@DeleteMapping("/clientes/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable(name = "id") Long id) {
		Map<String, String> res = new HashMap<String, String>();
		return cs.delete(id).map(removedCustomer -> {

            res.put("msg", "Cliente removido com sucesso");
            return ResponseEntity.status(HttpStatus.OK).body(res);
		}).orElseGet(()->{

            res.put("msg", "Falha ao remover cliente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		});
	}
}
