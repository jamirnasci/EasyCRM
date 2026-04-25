package com.jamir.easycrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SaleController {
	@GetMapping("/vendas")
	public String vendas() {
		return "vendas/page";
	}
}
