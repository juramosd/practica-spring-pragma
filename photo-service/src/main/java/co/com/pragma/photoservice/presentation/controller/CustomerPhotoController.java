package co.com.pragma.photoservice.presentation.controller;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.service.ICustomerPhotoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/photos")
public class CustomerPhotoController {
    @Autowired
    ICustomerPhotoService customerService;

    @ApiOperation("Get all photos.")
    @ApiResponse(code = 200, message = "SUCCESS")
    @GetMapping
    public ResponseEntity<List<CustomerPhotoDto>> listAllCustomersPhotos() {
        List<CustomerPhotoDto> customers = customerService.getAll();

        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerPhotoDto> getProductCustomerPhoto(@PathVariable("id") String id) {
        Optional<CustomerPhotoDto> customers = customerService.getCustomerPhoto(id);

        if (customers == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customers.get());
    }

    @PostMapping
    public ResponseEntity<CustomerPhotoDto> createCustomerPhoto(@RequestBody CustomerPhotoDto customer){
        Optional<CustomerPhotoDto> customerDB = customerService.create(customer);
        return  ResponseEntity.status( HttpStatus.CREATED).body(customerDB.get());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") String id, @RequestBody CustomerPhotoDto customer) {
        Optional<CustomerPhotoDto> currentCustomer = customerService.getCustomerPhoto(id);
        if ( null == currentCustomer ) {
            return  ResponseEntity.notFound().build();
        }
        customer.setIdPhoto(id);
        currentCustomer=customerService.update(customer);
        return  ResponseEntity.ok(currentCustomer);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomerPhotoDto> deleteCustomer(@PathVariable("id") String id) {
        Optional<CustomerPhotoDto> customer = customerService.getCustomerPhoto(id);
        if ( null == customer ) {
            return  ResponseEntity.notFound().build();
        }
        customer = customerService.delete(customer.get());
        return  ResponseEntity.ok(customer.get());
    }
}
