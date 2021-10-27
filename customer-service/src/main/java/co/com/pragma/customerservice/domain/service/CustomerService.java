package co.com.pragma.customerservice.domain.service;


import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.application.mainmodule.service.ICustomerService;
import co.com.pragma.customerservice.domain.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    @Qualifier("repositoryFeignWebFlux")
    private ICustomerRepository customerRepository;

    @Override
    public List<CustomerDto> getAll() {
        return customerRepository.getAll();
    }

    @Override
    public Optional<CustomerDto> createCustomer(CustomerDto customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<CustomerDto> updateCustomer(CustomerDto customer) {
        return customerRepository.modify(customer);
    }

    @Override
    public Optional<CustomerDto> deleteCustomer(CustomerDto customer) {
        return customerRepository.delete(customer);
    }
    @Override
    public Optional<CustomerDto> getCustomer(Long id) {
        return  customerRepository.getCustomer(id);
    }

    @Override
    public Optional<CustomerDto> getCustomerIdentification(String id) {
        return customerRepository.getCustomerIdentification(id);
    }

    @Override
    public List<CustomerDto> getCustomerAgeMax(int age) {
        return customerRepository.findByAgeGreaterThanEqual(age);
    }
}
