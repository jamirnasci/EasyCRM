package com.jamir.easycrm.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jamir.easycrm.model.Product;
import com.jamir.easycrm.model.ProductCategory;
import com.jamir.easycrm.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository pr;

	public List<Product> findAll() {
		return pr.findAll();
	}

	public List<Product> search(String search, ProductCategory category) {
		if (search != null && !search.trim().isEmpty() && category != null) {
			return pr.searchByNameAndCategory(search.toLowerCase(), category);
		} else if (search != null && !search.trim().isEmpty()) {
			return pr.searchByName(search.toLowerCase());
		} else if (category != null) {
			return pr.searchByCategory(category);
		}
		return pr.findAll();
	}

	public Optional<Product> findById(Long id) {
		return pr.findById(id);
	}

	public Optional<Product> decrementQuantity(Product p, int quantity) {
		return pr.findById(p.getIdproduct()).map(product -> {
			if (product.getQuantity() >= quantity) {
				product.setQuantity(product.getQuantity() - quantity);
				return pr.save(product);
			}
			throw new RuntimeException("Quantidade insuficiente em estoque para o produto: " + product.getName());
		});
	}

	public Optional<Product> incrementQuantity(Product p, int quantity) {
		return pr.findById(p.getIdproduct()).map(product -> {
			product.setQuantity(product.getQuantity() + quantity);
			return pr.save(product);
		});
	}

	public String createProductImage(MultipartFile imgFile) {
		if (imgFile != null && imgFile.isEmpty()) {
			throw new RuntimeException("Arquivo foto de produto inválido");
		}
		String originalName = imgFile.getOriginalFilename();
		String extension = originalName.substring(originalName.lastIndexOf("."));

		String fileName = UUID.randomUUID().toString();
		Path uploadPath = Paths.get("uploads/products/");

		if (!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Path filePath = uploadPath.resolve(fileName);
		try {
			Files.copy(imgFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public Product create(Product p, MultipartFile imgFile) {
		String fileName = createProductImage(imgFile);
		p.setImgUrl("/uploads/products/" + fileName);
		return pr.save(p);

	}

	public Optional<Product> update(Long idproduct, Product p) {
		return pr.findById(idproduct).map(productFound -> {
			productFound.setName(p.getName());
			productFound.setDescription(p.getDescription());
			productFound.setPrice(p.getPrice());
			productFound.setCategory(p.getCategory());
			productFound.setQuantity(p.getQuantity());
			productFound.setName(p.getName());
			return pr.save(productFound);
		});
	}

	public Optional<Product> delete(Long idcustomer) {
		return pr.findById(idcustomer).map(productFound -> {
			String filePath = productFound.getImgUrl();
			if (filePath.startsWith("/")) {
				filePath = filePath.substring(1);
			}
			String basePath = System.getProperty("user.dir");
			File imgFile = new File(basePath, filePath);

			if (imgFile.exists()) {
				if (!imgFile.delete()) {
					throw new RuntimeException("Falha ao remover imagem do produto");
				}
			}
			pr.delete(productFound);
			return productFound;
		});
	}
}
