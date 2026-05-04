package com.jamir.easycrm.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.PaymentMethod;
import com.jamir.easycrm.model.Sale;
import com.jamir.easycrm.model.SaleStatus;
import com.jamir.easycrm.service.CustomerService;
import com.jamir.easycrm.service.ProductService;
import com.jamir.easycrm.service.SaleService;

import jakarta.validation.Valid;

@Controller
public class SaleController {

	@Autowired
	private SaleService ss;
	@Autowired
	private CustomerService cs;
	@Autowired
	private ProductService ps;

	@GetMapping("/vendas")
	public ModelAndView vendas(
			@RequestParam(name = "d1", required = false) LocalDate d1,
			@RequestParam(name = "d2", required = false) LocalDate d2,
			@AuthenticationPrincipal UserPrincipal user
		) {
		ModelAndView mv = new ModelAndView("vendas/page");
		BigDecimal total = BigDecimal.ZERO;
		if (d1 != null && d2 != null) {
			List<Sale> salesPerDate = ss.findBeetweenDates(d1, d2, user.getUser());
			total = ss.sumTotalSales(salesPerDate);
			mv.addObject("sales", salesPerDate);
			mv.addObject("total", total);
			mv.addObject("quantity", salesPerDate.size());
			mv.addObject("negociacaoCount", ss.sumByStatus(salesPerDate, SaleStatus.EM_NEGOCIACAO));
			mv.addObject("finalizadaCount", ss.sumByStatus(salesPerDate, SaleStatus.FINALIZADA));
			mv.addObject("d1", d1);
			mv.addObject("d2", d2);
		} else {
			mv.addObject("sales", ss.findByUser(user.getUser()));
		}
		List<Sale> sales = ss.findByUser(user.getUser());
		mv.addObject("total", ss.sumTotalSales(sales));
		mv.addObject("quantity", sales.size());
		mv.addObject("customers", cs.findByUser(user.getUser()));
		mv.addObject("products", ps.findAll());
		mv.addObject("negociacaoCount", ss.sumByStatus(sales, SaleStatus.EM_NEGOCIACAO));
		mv.addObject("finalizadaCount", ss.sumByStatus(sales, SaleStatus.FINALIZADA));
		mv.addObject("paymentMethods", PaymentMethod.values());
		mv.addObject("saleStatus", SaleStatus.values());
		return mv;
	}

	@PostMapping("/vendas/create")
	public String novaVenda(@Valid Sale sale, BindingResult result, @AuthenticationPrincipal UserPrincipal user, RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Erro ao validar os dados venda. Verifique os dados e tente novamente.");
			return "redirect:/vendas";
		}
		sale.setUser(user.getUser());
		ss.create(sale);
		return "redirect:/vendas";
	}

	@GetMapping("/vendas/update/{id}")
	public ModelAndView update(
		@PathVariable(name = "id") Long id,
		@AuthenticationPrincipal UserPrincipal user
	) {
		return ss.findById(id).map(saleFound -> {
			if(!saleFound.getCustomer().getUser().getIduser().equals(user.getUser().getIduser())) {
				return new ModelAndView("utils/unauthorized");
			}
			ModelAndView mv = new ModelAndView("vendas/update");
			mv.addObject("sale", saleFound);
			mv.addObject("customers", cs.findByUser(user.getUser()));
			mv.addObject("products", ps.findAll());
			mv.addObject("paymentMethods", PaymentMethod.values());
			mv.addObject("saleStatus", SaleStatus.values());
			return mv;
		}).orElseGet(() -> {
			return new ModelAndView("redirect:/vendas");
		});
	}

	@PostMapping("/vendas/update/{id}")
	public String update(
		@PathVariable(name = "id") Long id, 
		@ModelAttribute @Valid Sale s,
		BindingResult result,
		@AuthenticationPrincipal UserPrincipal user,
		RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Erro ao validar os dados da venda. Verifique os dados e tente novamente.");
			return "redirect:/vendas/update/" + id;
		}
		s.setUser(user.getUser());
		ss.update(id, s);
		return "redirect:/vendas";
	}
}
