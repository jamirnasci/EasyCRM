package com.jamir.easycrm.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public String handleUserException(UserException ex, RedirectAttributes redirectAttributes) {
        // Handle UserException and return an appropriate response
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/usuario/update";
    }

    @ExceptionHandler(SaleException.class)
    public String handleSaleException(SaleException ex, RedirectAttributes redirectAttributes) {
        // Handle SaleException and return an appropriate response
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/vendas";
    }

    @ExceptionHandler(ProductException.class)
    public String handleProductException(ProductException ex, RedirectAttributes redirectAttributes) {
        // Handle ProductException and return an appropriate response
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/produtos";
    }

    @ExceptionHandler(InteractionException.class)
    public String handleInteractionException(InteractionException ex, RedirectAttributes redirectAttributes) {
        // Handle InteractionException and return an appropriate response
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/interacoes";
    }
    @ExceptionHandler(CustomerException.class)
    public String handleCustomerException(CustomerException ex, RedirectAttributes redirectAttributes) {        
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/clientes";
    }
    @ExceptionHandler(AdmException.class)
    public String handleAdmException(AdmException ex, RedirectAttributes redirectAttributes) {        
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/adm/home";
    }
}
