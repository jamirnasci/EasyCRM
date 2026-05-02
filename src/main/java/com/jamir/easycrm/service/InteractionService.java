package com.jamir.easycrm.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.model.Interaction;
import com.jamir.easycrm.model.InteractionStatus;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.InteractionRepository;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository ir;

    public Optional<Interaction> create(Interaction i){
        return Optional.of(ir.save(i));
    }
    public List<Interaction> findByUser(User user){
        return ir.findByUser(user);
    }
    public List<Interaction> findBetweenDatesAndStatus(LocalDate d1, LocalDate d2, String status, Long userId){
        if(status != null){
            return ir.findBetweenDatesAndStatus(d1, d2, status, userId);
        }else{
            return ir.findBetweenDates(d1, d2, userId);
        }
    }
    public Optional<Interaction> findById(Long id) {
        return ir.findById(id);
    }
    public Optional<Interaction> update(Long id, Interaction i) {
        return ir.findById(id).map(existingInteraction -> {
            existingInteraction.setDate(i.getDate());
            existingInteraction.setTime(i.getTime());
            existingInteraction.setType(i.getType());
            existingInteraction.setStatus(i.getStatus());
            existingInteraction.setDescription(i.getDescription());
            existingInteraction.setCustomer(i.getCustomer());
            return ir.save(existingInteraction);
        });
    }
}