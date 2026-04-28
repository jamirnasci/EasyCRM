package com.jamir.easycrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jamir.easycrm.model.Product;
import com.jamir.easycrm.model.ProductCategory;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	@Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:search%")
	public List<Product> searchByName(String search);
	
	@Query(value = "SELECT p FROM Product p WHERE p.category = :category")
	public List<Product> searchByCategory(ProductCategory category);
	
	@Query(value = "SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:search% AND p.category = :category")
	public List<Product> searchByNameAndCategory(String search, ProductCategory category);
}
