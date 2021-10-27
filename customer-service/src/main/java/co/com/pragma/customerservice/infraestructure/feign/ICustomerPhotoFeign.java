package co.com.pragma.customerservice.infraestructure.feign;

import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import co.com.pragma.customerservice.infraestructure.fallback.CustomerHystrixFactory;
//import co.com.pragma.customerservice.presentation.web.config.LoadBalancerConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "photo-service")
//@LoadBalancerClient(name = "photo-service", configuration= LoadBalancerConfiguration.class)
public interface ICustomerPhotoFeign {
    @GetMapping(value = "/photos")
    ResponseEntity<List<CustomerPhoto>> listAllCustomersPhotos();
    @GetMapping(value = "/photos/{id}")
    ResponseEntity<CustomerPhoto> getCustomerPhoto(@PathVariable("id") String id);
    @PostMapping(value="/photos")
    ResponseEntity<CustomerPhoto> createCustomerPhoto(@RequestBody CustomerPhoto customer);
    @PutMapping(value = "/photos/{id}")
    ResponseEntity<CustomerPhoto> updateCustomer(@PathVariable("id") String id, @RequestBody CustomerPhoto customer);
}
