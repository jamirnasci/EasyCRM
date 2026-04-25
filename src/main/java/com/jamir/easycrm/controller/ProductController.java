package com.jamir.easycrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
	@GetMapping("/produtos")
	public String product() {
		return "produtos/page";
	}
}
