package co.com.pragma.photoservice.presentation.controller;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.service.ICustomerPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes-fotos")
public class CustomerPhotoController {
    @Autowired
    ICustomerPhotoService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerPhotoDto>> listAllCustomersPhotos() {
        List<CustomerPhotoDto> customers = customerService.getAll();

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerPhotoDto> getProductCustomerPhoto(@PathVariable("id") Long id) {
        CustomerPhotoDto customers = customerService.get(id);

        if (customers == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerPhotoDto> createCustomerPhoto(@RequestBody CustomerPhotoDto customer){
        CustomerPhotoDto customerDB = customerService.create(customer);
        return  ResponseEntity.status( HttpStatus.CREATED).body(customerDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") long id, @RequestBody CustomerPhotoDto customer) {
        CustomerPhotoDto currentCustomer = customerService.get(id);
        if ( null == currentCustomer ) {
            return  ResponseEntity.notFound().build();
        }
        customer.setIdPhoto(id);
        currentCustomer=customerService.update(customer);
        return  ResponseEntity.ok(currentCustomer);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomerPhotoDto> deleteCustomer(@PathVariable("id") long id) {
        CustomerPhotoDto customer = customerService.get(id);
        if ( null == customer ) {
            return  ResponseEntity.notFound().build();
        }
        customer = customerService.delete(customer);
        return  ResponseEntity.ok(customer);
    }
}
