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
    public ResponseEntity<List<CustomerDto>> listAllCustomers(@RequestParam(name = "maxAge", required = false, defaultValue = "false") boolean maxAge) {
        List<CustomerDto> customers = new ArrayList<>();
        if (maxAge) {
            customers = customerService.getCustomerAgeMax(18);
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
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerDto customer) {

        Optional<CustomerDto> currentCustomer = customerService.getCustomer(id);

        if (null == currentCustomer) {
            return ResponseEntity.notFound().build();
        }
        customer.setCustomerId(id);
        currentCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(currentCustomer.get());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") Long id) {
        Optional<CustomerDto> invoice  = customerService.getCustomer(id);
        if (null == invoice) {
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(invoice.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable("id") Long id) {
        Optional<CustomerDto> customer = customerService.getCustomer(id);
        if (null == customer) {
            return ResponseEntity.notFound().build();
        }
        customer = customerService.deleteCustomer(customer.get());
        return ResponseEntity.ok(customer.get());
    }
}
