package com.jamir.easycrm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jamir.easycrm.model.Customer;
import com.jamir.easycrm.model.User;
import com.jamir.easycrm.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository cr;
	
	public List<Customer> findAll(){
        return cr.findAll();
    }
	
	public List<Customer> search(String key){
        return cr.search(key);
    }
	
    public List<Customer> findByUser(User user){
        return cr.findByUser(user);
    }    

    public Optional<Customer> findById(Long id){
        return cr.findById(id);
    }
    
    public Optional<Customer> create(Customer c){
        return Optional.of(cr.save(c));
    }
    public Optional<Customer> update(Long idcustomer, Customer c){
        return cr.findById(idcustomer).map(customerFound ->{
            customerFound.setCpf(c.getCpf());
            customerFound.setDescription(c.getDescription());
            customerFound.setEmail(c.getEmail());
            customerFound.setName(c.getName());
            customerFound.setPhone(c.getPhone()); 
            customerFound.setStatus(c.getStatus());
            return cr.save(customerFound);
        });
    }
    public Optional<Customer> delete(Long idcustomer){
        return cr.findById(idcustomer).map(customerFound ->{
            cr.delete(customerFound);
            return customerFound;
        });
    }
}
