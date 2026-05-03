package com.jamir.easycrm.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.exception.ProductException;
import com.jamir.easycrm.model.Product;
import com.jamir.easycrm.model.Sale;
import com.jamir.easycrm.model.SaleStatus;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleService {
    @Autowired
    private SaleRepository sr;
    @Autowired
    private ProductService ps;

    public List<Sale> findByUser(User user) {
        return sr.findByUser(user);
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

    @Transactional
    public Sale create(Sale s) {

        Product selectedProduct = ps.findById(s.getProduct().getIdproduct())
                .orElseThrow(() -> new ProductException("Produto não encontrado"));

        if (s.getQuantity() <= 0) {
            throw new ProductException("Quantidade inválida");
        }

        if (s.getQuantity() > selectedProduct.getQuantity()) {
            throw new ProductException("Quantidade insuficiente em estoque");
        }

        ps.decrementQuantity(selectedProduct, s.getQuantity());

        return sr.save(s);
    }

    public Optional<Sale> findById(Long id) {
        return sr.findById(id);
    }

    public List<Sale> findBeetweenDates(LocalDate d1, LocalDate d2, User user) {
        return sr.findBetweenDates(d1, d2, user);
    }

    @Transactional
    public Sale update(Long idsale, Sale s) {

        if(!s.getUser().getIduser().equals(s.getCustomer().getUser().getIduser())) {
            throw new ProductException("O cliente selecionado não pertence ao usuário logado.");
        }

        Sale saleFound = sr.findById(idsale)
                .orElseThrow(() -> new ProductException("Venda não encontrada"));

        Product oldProduct = saleFound.getProduct();
        Product newProduct = ps.findById(s.getProduct().getIdproduct())
                .orElseThrow(() -> new ProductException("Produto não encontrado"));

        int oldQty = saleFound.getQuantity();
        int newQty = s.getQuantity();

        if (oldProduct.getIdproduct().equals(newProduct.getIdproduct())) {

            int diff = newQty - oldQty;

            if (diff > 0) {
                if (diff > newProduct.getQuantity()) {
                    throw new ProductException("Estoque insuficiente para essa operação");
                }
                ps.decrementQuantity(newProduct, diff);
            } else if (diff < 0) {
                ps.incrementQuantity(newProduct, -diff);
            }

        } else {
            ps.incrementQuantity(oldProduct, oldQty);

            if (newQty > newProduct.getQuantity()) {
                throw new ProductException("Estoque insuficiente para essa operação");
            }

            ps.decrementQuantity(newProduct, newQty);
        }

        saleFound.setCustomer(s.getCustomer());
        saleFound.setPaymentMethod(s.getPaymentMethod());
        saleFound.setProduct(newProduct);
        saleFound.setQuantity(newQty);
        saleFound.setStatus(s.getStatus());
        saleFound.setTotal(s.getTotal());

        return sr.save(saleFound);
    }
}
