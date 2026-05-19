package com.jamir.easycrm.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.model.UserRoles;
import com.jamir.easycrm.model.UserStatus;
import com.jamir.easycrm.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private UserService us;

	@GetMapping("/usuario")
	public String user() {
		return "";
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
			@ModelAttribute @Valid User user,
			BindingResult result,
			@RequestParam("imgFile") MultipartFile imgFile,
			@RequestParam("newPassword") String newPassword,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			String msg = result.getFieldError().getDefaultMessage();
			redirectAttributes.addFlashAttribute("error", msg);
			return "redirect:/usuario/update";
		}
		User currentUser = userPrincipal.getUser();
		Optional<User> updatedUser = us.update(currentUser.getIduser(), user, imgFile, newPassword);
		if (updatedUser.isPresent()) {
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
