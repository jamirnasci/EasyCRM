package com.jamir.easycrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jamir.easycrm.config.UserPrincipal;
import com.jamir.easycrm.model.Product;
import com.jamir.easycrm.model.ProductCategory;
import com.jamir.easycrm.model.UserRoles;
import com.jamir.easycrm.service.ProductService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

@Controller
public class ProductController {

	@Autowired
	private ProductService ps;

	@GetMapping("/produtos")
	public ModelAndView product(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "category", required = false) ProductCategory category) {
		ModelAndView mv = new ModelAndView("/produtos/page");
		if (search != null || category != null) {
			List<Product> searchResult = ps.search(search, category);
			mv.addObject("products", searchResult);
		} else {
			mv.addObject("products", ps.findAll());
		}
		mv.addObject("categories", ProductCategory.values());
		return mv;
	}

	@GetMapping("/produtos/findone/{id}")
	public ResponseEntity<?> findOne(@PathVariable(name = "id") Long id) {

		return ps.findById(id).map(productFound -> {
			return ResponseEntity.ok(productFound);
		}).orElseGet(() -> {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		});
	}

	@PreAuthorize("hasRole('ADM')")
	@PostMapping("/produtos/create")
	public String create(
			@ModelAttribute @Valid Product p,
			BindingResult result,
			@RequestParam("imgFile") MultipartFile imgFile,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UserPrincipal user) {
		if(result.hasErrors()) {
			String msg = result.getFieldError().getDefaultMessage();
			redirectAttributes.addFlashAttribute("error", msg);
			return "redirect:/produtos";
		}
		ps.create(p, imgFile);
		return "redirect:/produtos";
	}

	@GetMapping("/produtos/details/{id}")
	public ModelAndView details(@PathVariable(name = "id") Long id) {
		return ps.findById(id).map(productFound -> {
			ModelAndView mv = new ModelAndView("produtos/details");
			mv.addObject("p", productFound);
			// mv.addObject("categories", ProductCategory.values());
			return mv;
		}).orElseGet(() -> {
			return new ModelAndView("redirect:/produtos");
		});
	}

	@PreAuthorize("hasRole('ADM')")
	@GetMapping("/produtos/update/{id}")
	public ModelAndView update(
			@PathVariable(name = "id") Long id,
			@AuthenticationPrincipal UserPrincipal user) {
		return ps.findById(id).map(productFound -> {
			ModelAndView mv = new ModelAndView("produtos/update");
			mv.addObject("p", productFound);
			mv.addObject("categories", ProductCategory.values());
			return mv;
		}).orElseGet(() -> {
			return new ModelAndView("redirect:/produtos");
		});
	}
	
	@PreAuthorize("hasRole('ADM')")
	@PostMapping("/produtos/update/{id}")
	public String update(
			@PathVariable(name = "id") Long id, 
			@ModelAttribute @Valid Product p,
			BindingResult result,
			@RequestParam(name = "imgFile") MultipartFile imgFile,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UserPrincipal user) {
		if(result.hasErrors()) {
			String msg = result.getFieldError().getDefaultMessage();
			redirectAttributes.addFlashAttribute("error", msg);
			return "redirect:/produtos/update/" + id;
		}
		ps.update(id, p, imgFile);
		return "redirect:/produtos";
	}

	@PreAuthorize("hasRole('ADM')")
	@DeleteMapping("/produtos/delete/{id}")
	public ResponseEntity<Map<String, String>> delete(
			@PathVariable(name = "id") Long id,
			@AuthenticationPrincipal UserPrincipal user) {
		Map<String, String> res = new HashMap<String, String>();
		return ps.delete(id).map(removedProduct -> {

			res.put("msg", "Produto removido com sucesso");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}).orElseGet(() -> {
			res.put("msg", "Falha ao remover produto");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		});
	}
}
