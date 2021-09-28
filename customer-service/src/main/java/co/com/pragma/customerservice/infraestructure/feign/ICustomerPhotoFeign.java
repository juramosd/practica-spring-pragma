package co.com.pragma.customerservice.infraestructure.feign;

import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "photo-service",url = "http://localhost:8002/",path = "")
@RequestMapping("/clientes-fotos")
public interface ICustomerPhotoFeign {
    @GetMapping
    List<CustomerPhoto> listAllCustomersPhotos();
    @GetMapping(value = "/{id}")
    CustomerPhoto getProductCustomerPhoto(@PathVariable("id") Long id);
}
