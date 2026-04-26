package com.jamir.easycrm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jamir.easycrm.model.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long>{
    @Query(value = "SELECT * FROM interaction WHERE date BETWEEN :d1 AND :d2;", nativeQuery = true)
    public List<Interaction> findBetweenDates(@Param("d1") LocalDate d1, @Param("d2") LocalDate d2);
    @Query(value = "SELECT * FROM interaction WHERE date BETWEEN :d1 AND :d2 AND status = :status;", nativeQuery = true)
    public List<Interaction> findBetweenDatesAndStatus(@Param("d1") LocalDate d1, @Param("d2") LocalDate d2, @Param("status") String status);
}