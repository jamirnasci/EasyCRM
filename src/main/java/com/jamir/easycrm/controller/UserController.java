package com.jamir.easycrm.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jamir.easycrm.config.UserPrincipal;
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
	@GetMapping("/usuario/update")
	public ModelAndView editAccount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		ModelAndView mv = new ModelAndView("usuario/update");
		User user = us.findById(userPrincipal.getUser().getIduser());
		user.setPassword("");
		mv.addObject("user", user);
	    mv.addObject("roles", UserRoles.values());
	    mv.addObject("statuses", UserStatus.values());
		return mv;
	}
	@PostMapping("/usuario/update")
	public String editAccount(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@ModelAttribute User user,
			@RequestParam("imgFile") MultipartFile imgFile,
			@RequestParam("newPassword") String newPassword
		) {
		User currentUser = userPrincipal.getUser();
		Optional<User> updatedUser = us.update(currentUser.getIduser(), user, imgFile, newPassword);
		if(updatedUser.isPresent()) {
			return "redirect:/usuario/perfil";
		} else {
			return "redirect:/usuario/update?error";
		}
	}
	@GetMapping("/usuario/perfil")
	public ModelAndView userProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		ModelAndView mv = new ModelAndView("usuario/perfil");

		mv.addObject("user", userPrincipal.getUser());
		return mv;
	}
		
}
