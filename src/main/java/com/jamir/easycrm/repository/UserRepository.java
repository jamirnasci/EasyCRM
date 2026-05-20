package com.jamir.easycrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jamir.easycrm.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	public Optional<User> findByEmail(String email);
	public List<User> findByIduserNot(Long id);
	@Query(value = "SELECT * FROM user WHERE LOWER(name) LIKE %:name% AND iduser <> :currentUserId", nativeQuery = true)
	public List<User> searchByName(String name, Long currentUserId);
}
