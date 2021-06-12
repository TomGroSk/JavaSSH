package pl.ssh.frontservice.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.ssh.frontservice.model.Customer;
import pl.ssh.frontservice.repository.CustomerRepository;

@Component
public class UserValidator implements Validator {

    private final CustomerRepository userRepository;

    public UserValidator(CustomerRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors err) {
        ValidationUtils.rejectIfEmpty(err, "username", "customer.name.empty");
        ValidationUtils.rejectIfEmpty(err, "password", "customer.password.empty");

        Customer customer = (Customer) obj;
        if (!customer.getPassword().equals(customer.getRePassword())) { //if passwords are the same
            err.rejectValue("rePassword", "customer.rePassword.notEqual");
        }
        for (Customer u : userRepository.findAll()) { //if username already exist
            if (u.getUsername().equals(customer.getUsername())) {
                err.rejectValue("username", "customer.username.exist");
            }
        }
    }

}
