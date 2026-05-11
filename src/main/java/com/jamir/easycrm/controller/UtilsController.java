package com.jamir.easycrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UtilsController {
    
    @GetMapping("/unauthorized")
    public String unauthorized(){
        return "utils/unauthorized";
    }
}
