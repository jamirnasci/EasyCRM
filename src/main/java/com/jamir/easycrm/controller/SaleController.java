package com.jamir.easycrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.PaymentMethod;
import com.jamir.easycrm.model.Sale;
import com.jamir.easycrm.model.SaleStatus;
import com.jamir.easycrm.service.CustomerService;
import com.jamir.easycrm.service.ProductService;
import com.jamir.easycrm.service.SaleService;

@Controller
public class SaleController {

	@Autowired
	private SaleService ss;
	@Autowired
	private CustomerService cs;
	@Autowired
	private ProductService ps;

	@GetMapping("/vendas")
	public ModelAndView vendas() {
		ModelAndView mv = new ModelAndView("vendas/page");
		mv.addObject("sales", ss.findAll());
		mv.addObject("customers", cs.findAll());
		mv.addObject("products", ps.findAll());
		mv.addObject("paymentMethods", PaymentMethod.values());
		mv.addObject("saleStatus", SaleStatus.values());
		return mv;
	}

	@PostMapping("/vendas/create")
	public String novaVenda(Sale sale, @AuthenticationPrincipal UserPrincipal user) {
		sale.setUser(user.getUser());
		return ss.create(sale).map(savedSale -> {
			String msg = "Venda cadastrada com sucesso";
			return "redirect:/vendas?msg=" + msg;
		}).orElseGet(() -> {
			String msg = "Falha ao cadastrar venda";
			return "redirect:/vendas?msg=" + msg;
		});
	}

	@GetMapping("/vendas/update/{id}")
	public ModelAndView update(@PathVariable(name = "id") Long id) {
		return ss.findById(id).map(saleFound -> {
			ModelAndView mv = new ModelAndView("vendas/update");
			mv.addObject("sale", saleFound);
			mv.addObject("customers", cs.findAll());
			mv.addObject("products", ps.findAll());
			mv.addObject("paymentMethods", PaymentMethod.values());
			mv.addObject("saleStatus", SaleStatus.values());
			return mv;
		}).orElseGet(() -> {
			return new ModelAndView("redirect:/vendas");
		});
	}
	@PostMapping("/vendas/update/{id}")
	public String update(@PathVariable(name = "id") Long id, Sale s) {
		return ss.update(id, s).map(updatedSale ->{
			return "redirect:/vendas";
		}).orElseGet(()->{
			String msg = "Falha ao atualizar venda";
			return "redirect:/vendas?msg=" + msg;
		});		
	}
}
