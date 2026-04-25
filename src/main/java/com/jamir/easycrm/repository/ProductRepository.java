package com.jamir.easycrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jamir.easycrm.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	@Query(value = "SELECT * FROM producr WHERE LOWER(name) LIKE %:key%", nativeQuery = true)
	public List<Product> search(String key);
}
