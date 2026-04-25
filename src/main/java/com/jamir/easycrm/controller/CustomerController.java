package com.jamir.easycrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.CustomerStatus;
import com.jamir.easycrm.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService cs;
	
	@GetMapping("/clientes")
	public ModelAndView clientes(@RequestParam(name = "search", required = false) String search) {
		if(search != null) {
			List<Customer> loadedCustomers = cs.search(search);
			ModelAndView mv = new ModelAndView("clientes/page");
			mv.addObject("statuses", CustomerStatus.values());
			mv.addObject("customers", loadedCustomers);
			return mv;
		}
		List<Customer> loadedCustomers = cs.findAll();
		ModelAndView mv = new ModelAndView("clientes/page");
		mv.addObject("statuses", CustomerStatus.values());
		mv.addObject("customers", loadedCustomers);
		return mv;
	}
	@PostMapping("/clientes/create")
	public String create(Customer c, @AuthenticationPrincipal UserPrincipal user) {
		c.setUser(user.getUser());
		return cs.create(c).map(savedCustomer ->{
			return "redirect:/clientes";
		}).orElseGet(()->{
			String msg = "Falha ao cadastrar cliente";
			return "redirect:/clientes?msg=" + msg;
		});
	}
	@GetMapping("/clientes/update/{id}")
	public ModelAndView update(@PathVariable(name = "id") Long id) {
		return cs.findById(id).map(customerFound ->{
			ModelAndView mv = new ModelAndView("clientes/update");
			mv.addObject("customer", customerFound);
			mv.addObject("statuses", CustomerStatus.values());
			return mv;
		}).orElseGet(()->{
			return new ModelAndView("redirect:/clientes");
		});		
	}
	@PostMapping("/clientes/update/{id}")
	public String update(@PathVariable(name = "id") Long id, Customer c) {
		return cs.update(id, c).map(updatedCustomer ->{
			return "redirect:/clientes";
		}).orElseGet(()->{
			String msg = "Falha ao atualizar cliente";
			return "redirect:/clientes?msg=" + msg;
		});		
	}
	@DeleteMapping("/clientes/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(@PathVariable(name = "id") Long id) {
		Map<String, String> res = new HashMap();
		return cs.delete(id).map(removedCustomer -> {

            res.put("msg", "Cliente removido com sucesso");
            return ResponseEntity.status(HttpStatus.OK).body(res);
		}).orElseGet(()->{

            res.put("msg", "Falha ao remover cliente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		});
	}
}
