package co.com.pragma.customerservice.presentation.web.controller;

import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import co.com.pragma.customerservice.infraestructure.fallback.CustomerHystrixFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Fallback {
    @Autowired
    private CustomerHystrixFactory customerHystrixFactory;

    @GetMapping("/photo-fallback")
    public ResponseEntity<List<CustomerPhoto>> photoFallback(){
          return customerHystrixFactory.listAllCustomersPhotos();
    }
}
