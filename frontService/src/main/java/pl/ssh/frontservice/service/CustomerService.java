package pl.ssh.frontservice.service;

import org.springframework.stereotype.Service;
import pl.ssh.frontservice.model.Customer;
import pl.ssh.frontservice.repository.CustomerRepository;

import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).get();
    }

    public boolean isAdmin(Long id){
        var customer = getCustomerById(id);
        return customer.getRole().equals("ADMIN");
    }
}
