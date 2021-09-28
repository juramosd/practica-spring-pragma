package co.com.pragma.customerservice.infraestructure.crud;

import co.com.pragma.customerservice.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ICustomerCrudRepository extends JpaRepository<Customer,Long> {
    Customer findByIdentification(String identification);
    List<Customer> findByFirstName(String FirstName);
    List<Customer> findByCity(String city);
    List<Customer> findByAgeGreaterThanEqual(int age);
}
