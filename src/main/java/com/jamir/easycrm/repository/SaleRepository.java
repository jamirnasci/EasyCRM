package com.jamir.easycrm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jamir.easycrm.model.Sale;
import com.jamir.easycrm.model.User;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{
    @Query("SELECT s FROM Sale s WHERE s.date BETWEEN :d1 AND :d2 AND s.user = :user")
    public List<Sale> findBetweenDates(LocalDate d1, LocalDate d2, User user);
    public List<Sale> findByUser(User user);
}
