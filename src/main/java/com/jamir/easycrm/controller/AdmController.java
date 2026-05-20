package com.jamir.easycrm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.model.UserRoles;
import com.jamir.easycrm.model.UserStatus;
import com.jamir.easycrm.service.AdmService;
import com.jamir.easycrm.service.UserService;

import jakarta.validation.Valid;

@Controller()
@RequestMapping("/adm")
public class AdmController {

    @Autowired
    private UserService us;
    @Autowired
    private AdmService as;

    @PreAuthorize("hasRole('ADM')")
    @GetMapping("/home")
    public ModelAndView admHome(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(name = "name", required = false) String name) {
        Long currentUserId = user.getUser().getIduser();
        ModelAndView mv = new ModelAndView("adm/home");
        if (name != null && !name.isBlank() && !name.isEmpty()) {
            List<User> users = us.searchByName(name, currentUserId);
            mv.addObject("users", users);
        } else {
            List<User> users = us.findByIdNot(currentUserId);
            mv.addObject("users", users);
        }
        mv.addObject("roles", UserRoles.values());
        mv.addObject("statuses", UserStatus.values());
        return mv;
    }

    @PreAuthorize("hasRole('ADM')")
    @PostMapping("/usuario/cadastro")
    public String createAccount(
            @ModelAttribute @Valid User user,
            BindingResult result,
            @RequestParam("imgFile") MultipartFile imgFile,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            String msg = result.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", msg);
            return "redirect:/usuario/cadastro";
        }
        us.createUser(user, imgFile);
        return "redirect:/login";
    }

    @PreAuthorize("hasRole('ADM')")
    @GetMapping("/usuario/update/{id}")
    public ModelAndView updateUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        User userFound = us.findById(id);
        ModelAndView mv = new ModelAndView("adm/update-usuario");
        mv.addObject("user", userFound);
        mv.addObject("roles", UserRoles.values());
        mv.addObject("statuses", UserStatus.values());
        return mv;
    }

    @PreAuthorize("hasRole('ADM')")
    @PostMapping("/usuario/update/{id}")
    public String editAccount(
            @ModelAttribute @Valid User user,
            BindingResult result,
            @RequestParam("imgFile") MultipartFile imgFile,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserPrincipal authUser) {
        if (result.hasErrors()) {
            String msg = result.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", msg);
            return "redirect:/adm/usuario/update/" + id;
        }
        User updatedUser = as.update(id, user, imgFile);
        if (updatedUser != null) {
            return "redirect:/adm/home";
        } else {
            return "redirect:/adm/home?error";
        }
    }
}
