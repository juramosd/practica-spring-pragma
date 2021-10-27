package co.com.pragma.customerservice.presentation.web.controller;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.application.mainmodule.service.ICustomerService;
import co.com.pragma.customerservice.presentation.web.util.UtilityExtensions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RefreshScope
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    ICustomerService customerService;

    @ApiOperation("Get all customers.")
    @ApiResponse(code = 200, message = "SUCCESS")
    @GetMapping
    public ResponseEntity<List<CustomerDto>> listAllCustomers(@RequestParam(name = "maxAge", required = false, defaultValue = "0") int maxAge) {
        List<CustomerDto> customers = new ArrayList<>();
        if (maxAge!=0) {
            customers = customerService.getCustomerAgeMax(maxAge);
        } else {
            customers = customerService.getAll();
        }
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customer, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UtilityExtensions.formatMessage(result));
        }
        Optional<CustomerDto> customerDB = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB.get());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") String id, @RequestBody CustomerDto customer) {

        Optional<CustomerDto> currentCustomer = customerService.getCustomerIdentification(id);

        if (!currentCustomer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        customer.setCustomerId(currentCustomer.get().getCustomerId());
        currentCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(currentCustomer.get());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") String id) {
        Optional<CustomerDto> invoice  = customerService.getCustomerIdentification(id);
        if (invoice == null) {
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(invoice.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable("id") String id) {
        Optional<CustomerDto> customer = customerService.getCustomerIdentification(id);
        if (!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        customer = customerService.deleteCustomer(customer.get());
        return ResponseEntity.ok(customer.get());
    }
}
