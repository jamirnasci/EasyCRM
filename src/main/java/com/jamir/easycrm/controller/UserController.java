package com.jamir.easycrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jamir.easycrm.model.User;
import com.jamir.easycrm.model.UserRoles;
import com.jamir.easycrm.model.UserStatus;
import com.jamir.easycrm.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService us;
	
	@GetMapping("/usuario")
	public String user() {
		return "";
	}
	@GetMapping("/usuario/cadastro")
	public ModelAndView createAccount() {
		ModelAndView mv = new ModelAndView("cadastro");
	    mv.addObject("roles", UserRoles.values());
	    mv.addObject("statuses", UserStatus.values());
		return mv;
	}
	@PostMapping("/usuario/cadastro")
	public String createAccount(
			@ModelAttribute User user,
			@RequestParam("imgFile") MultipartFile imgFile
		) {
		us.createUser(user, imgFile);
		return "redirect:/login";
	}
}
