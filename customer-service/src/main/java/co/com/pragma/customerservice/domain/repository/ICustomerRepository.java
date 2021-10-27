package co.com.pragma.customerservice.domain.repository;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.domain.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepository {
    List<CustomerDto> getAll();
    Optional<CustomerDto> getCustomer(Long customerId);
    Optional<CustomerDto> save(CustomerDto customerDto);
    Optional<CustomerDto> modify(CustomerDto customerDto);
    Optional<CustomerDto> delete(CustomerDto customerDto);
    List<CustomerDto> findByAgeGreaterThanEqual(int age);
    Optional<CustomerDto> getCustomerIdentification(String id);
    Optional<CustomerDto> deleteBD(CustomerDto customerDto);
}
