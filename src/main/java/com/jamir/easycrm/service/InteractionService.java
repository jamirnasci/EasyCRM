package com.jamir.easycrm.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.exception.InteractionException;
import com.jamir.easycrm.model.Interaction;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.InteractionRepository;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository ir;

    public Interaction create(Interaction i) {
        try {
            return ir.save(i);
        } catch (Exception e) {
            throw new InteractionException("Falha ao criar interação, verifique as informações.");
        }
    }

    public List<Interaction> findByUser(User user) {
        return ir.findByUser(user);
    }

    public List<Interaction> findBetweenDatesAndStatus(LocalDate d1, LocalDate d2, String status, Long userId) {
        if (status != null) {
            return ir.findBetweenDatesAndStatus(d1, d2, status, userId);
        } else {
            return ir.findBetweenDates(d1, d2, userId);
        }
    }

    public Optional<Interaction> findById(Long id) {
        return ir.findById(id);
    }

    public Interaction update(Long id, Interaction i, User user) {
        try {
            Interaction existingInteraction = ir.findById(id)
                    .orElseThrow(() -> new InteractionException("Interação não encontrada"));

            existingInteraction.setDate(i.getDate());
            existingInteraction.setTime(i.getTime());
            existingInteraction.setType(i.getType());
            existingInteraction.setStatus(i.getStatus());
            existingInteraction.setDescription(i.getDescription());
            if(i.getCustomer().getUser().getIduser().equals(user.getIduser())) {
                existingInteraction.setCustomer(i.getCustomer());
            } else {
                throw new InteractionException("O cliente selecionado não pertence ao usuário logado.");
            }
            existingInteraction.setCustomer(i.getCustomer());
            return ir.save(existingInteraction);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new InteractionException("Dados inválidos ou duplicados");
        }

    }
}