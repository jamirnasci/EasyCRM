package com.jamir.easycrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.exception.CustomerException;
import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository cr;

    public List<Customer> findAll() {
        return cr.findAll();
    }

    public List<Customer> search(String key, Long userId) {
        return cr.search(key, userId);
    }

    public List<Customer> findByUser(User user) {
        return cr.findByUser(user);
    }

    public Optional<Customer> findById(Long id) {
        return cr.findById(id);
    }

    public Customer create(Customer c) {
        try {
            return cr.save(c);
        } catch (Exception e) {
            throw new CustomerException("Falha ao criar cliente, verifique as informações.");
        }
    }

    public Customer update(Long idcustomer, Customer c) {
        try {
            Customer customerFound = cr.findById(idcustomer)
                    .orElseThrow(() -> new CustomerException("Cliente não encontrado"));

            customerFound.setCpf(c.getCpf());
            customerFound.setDescription(c.getDescription());
            customerFound.setEmail(c.getEmail());
            customerFound.setName(c.getName());
            customerFound.setPhone(c.getPhone());
            customerFound.setStatus(c.getStatus());

            return cr.save(customerFound);

        } catch (DataIntegrityViolationException e) {
            throw new CustomerException("Dados inválidos ou duplicados");
        }
    }

    public Optional<Customer> delete(Long idcustomer) {
        return cr.findById(idcustomer).map(customerFound -> {
            cr.delete(customerFound);
            return customerFound;
        });
    }
}
