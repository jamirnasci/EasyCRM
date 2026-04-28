package com.jamir.easycrm.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.model.Product;
import com.jamir.easycrm.model.Sale;
import com.jamir.easycrm.model.SaleStatus;
import com.jamir.easycrm.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleService {
    @Autowired
    private SaleRepository sr;
    @Autowired
    private ProductService ps;

    public List<Sale> findAll() {
        return sr.findAll();
    }

    public BigDecimal sumTotalSales(List<Sale> sales) {
        BigDecimal total = BigDecimal.ZERO;
        for (Sale s : sales) {
            total = total.add(s.getTotal());
        }
        return total;
    }

    public int sumByStatus(List<Sale> sales, SaleStatus status) {
        int total = 0;
        for (Sale s : sales) {
            if (s.getStatus() == status) {
                total++;
            }
        }
        return total;
    }

    public Optional<Sale> create(Sale s) {
        if (s.getProduct() == null) {
            return Optional.empty();
        }
        Product selectedProduct = ps.findById(s.getProduct().getIdproduct()).orElse(null);
        if (selectedProduct != null && s.getQuantity() < selectedProduct.getQuantity()) {
            ps.decrementQuantity(selectedProduct, s.getQuantity());
            return Optional.of(sr.save(s));
        }
        return Optional.empty();
    }

    public Optional<Sale> findById(Long id) {
        return sr.findById(id);
    }

    public List<Sale> findBeetweenDates(java.time.LocalDate d1, java.time.LocalDate d2) {
        return sr.findBetweenDates(d1, d2);
    }

    @Transactional
    public Optional<Sale> update(Long idsale, Sale s) {
        return sr.findById(idsale).map(saleFound -> {

            ps.incrementQuantity(saleFound.getProduct(), saleFound.getQuantity());
            Optional<Product> newProduct = ps.decrementQuantity(s.getProduct(), s.getQuantity());
            if(newProduct.isEmpty()) {
                throw new RuntimeException("Não foi possível atualizar a venda devido à quantidade insuficiente do produto.");
            }
            saleFound.setCustomer(s.getCustomer());
            saleFound.setPaymentMethod(s.getPaymentMethod());
            saleFound.setProduct(newProduct.get());
            saleFound.setQuantity(s.getQuantity());
            saleFound.setStatus(s.getStatus());
            saleFound.setTotal(s.getTotal());

            return sr.save(saleFound);
        });
    }
}
