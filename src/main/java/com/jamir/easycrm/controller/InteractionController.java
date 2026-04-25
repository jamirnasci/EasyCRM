package com.jamir.easycrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InteractionController {
	@GetMapping("/interacoes")
	public String interactions() {
		return "interacoes/page";
	}
}
