package co.com.pragma.photoservice.infraestructure.feign;

import co.com.pragma.photoservice.domain.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "customer-service",url = "http://localhost:8001/",path = "")
@RequestMapping("/clientes")
public interface ICustomerFeign {
    @GetMapping
    List<Customer> listAllCustomers();

    @GetMapping(value = "/{id}")
    Customer getCustomer(@PathVariable("id") String id);
}
