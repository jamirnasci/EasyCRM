package com.jamir.easycrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.User;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	public List<Customer> findByUser(User user);
	@Query(value = "SELECT * FROM customer WHERE (LOWER(name) LIKE %:key% OR LOWER(cpf) LIKE %:key%) AND user_id = :userId", nativeQuery = true)
	public List<Customer> search(String key, Long userId);
}
