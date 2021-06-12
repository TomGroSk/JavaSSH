package pl.ssh.frontservice.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ssh.frontservice.model.Customer;
import pl.ssh.frontservice.repository.CustomerRepository;

import java.util.Collections;


@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public MyUserDetailsService(CustomerRepository userRepository) {
        this.customerRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(customer.getUsername(), customer.getPassword()
                , true
                , true
                , true
                , true,
                Collections.singleton(new SimpleGrantedAuthority(customer.getRole()))
        );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
