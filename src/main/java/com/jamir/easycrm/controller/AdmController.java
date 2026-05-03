package com.jamir.easycrm.controller;

import java.util.List;
import java.util.Optional;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @GetMapping("/home")
    public ModelAndView admHome(
            @AuthenticationPrincipal UserPrincipal user) {
        if (user.getUser().getRole() != UserRoles.ADM) {
            return new ModelAndView("utils/unauthorized");
        }
        List<User> users = us.findAll();
        ModelAndView mv = new ModelAndView("adm/home");
        mv.addObject("users", users);
        return mv;
    }

    @GetMapping("/usuario/update/{id}")
    public ModelAndView updateUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        if (user.getUser().getRole() != UserRoles.ADM) {
            return new ModelAndView("utils/unauthorized");
        }
        User userFound = us.findById(id);
        ModelAndView mv = new ModelAndView("adm/update-usuario");
        mv.addObject("user", userFound);
        mv.addObject("roles", UserRoles.values());
        mv.addObject("statuses", UserStatus.values());
        return mv;
    }

    @PostMapping("/usuario/update/{id}")
    public String editAccount(
            @ModelAttribute @Valid User user,
            BindingResult result,
            @RequestParam("imgFile") MultipartFile imgFile,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserPrincipal authUser
    ) {
        if(authUser.getUser().getRole() != UserRoles.ADM) {
            return "utils/unauthorized";
        }
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Erro ao validar os dados do usuário. Verifique os dados e tente novamente.");
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
