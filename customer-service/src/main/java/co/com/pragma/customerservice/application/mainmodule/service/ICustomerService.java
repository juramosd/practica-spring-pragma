package co.com.pragma.customerservice.application.mainmodule.service;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.domain.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<CustomerDto> getAll();
    Optional<CustomerDto> createCustomer(CustomerDto customer);
    Optional<CustomerDto> updateCustomer(CustomerDto customer);
    Optional<CustomerDto> deleteCustomer(CustomerDto customer);
    Optional<CustomerDto> getCustomer(Long id);
    Optional<CustomerDto> getCustomerIdentification(String id);
    List<CustomerDto> getCustomerAgeMax(int age);
}
