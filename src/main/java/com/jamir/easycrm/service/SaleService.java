package com.jamir.easycrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.Product;
import com.jamir.easycrm.model.Sale;
import com.jamir.easycrm.repository.SaleRepository;

@Service
public class SaleService {
    @Autowired
    private SaleRepository sr;

    public List<Sale> findAll() {
        return sr.findAll();
    }

    public Optional<Sale> create(Sale s) {
        return Optional.of(sr.save(s));
    }

    public Optional<Sale> findById(Long id) {
        return sr.findById(id);
    }

    public Optional<Sale> update(Long idsale, Sale s) {
        return sr.findById(idsale).map(saleFound -> {
            saleFound.setCustomer(s.getCustomer());
            saleFound.setPaymentMethod(s.getPaymentMethod());
            saleFound.setProduct(s.getProduct());
            saleFound.setQuantity(s.getQuantity());
            saleFound.setStatus(s.getStatus());
            saleFound.setTotal(s.getTotal());
            return sr.save(saleFound);
        });
    }
}
