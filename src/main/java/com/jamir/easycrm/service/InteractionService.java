package com.jamir.easycrm.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.model.Interaction;
import com.jamir.easycrm.model.InteractionStatus;
import com.jamir.easycrm.repository.InteractionRepository;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository ir;

    public Optional<Interaction> create(Interaction i){
        return Optional.of(ir.save(i));
    }
    public List<Interaction> findAll(){
        return ir.findAll();
    }
    public List<Interaction> findBetweenDatesAndStatus(LocalDate d1, LocalDate d2, String status){
        if(status != null){
            return ir.findBetweenDatesAndStatus(d1, d2, status);
        }else{
            return ir.findBetweenDates(d1, d2);
        }
    }
}