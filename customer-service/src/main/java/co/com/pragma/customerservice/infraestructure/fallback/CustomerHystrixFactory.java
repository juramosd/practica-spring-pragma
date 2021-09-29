package co.com.pragma.customerservice.infraestructure.fallback;

import co.com.pragma.customerservice.domain.entity.Customer;
import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import co.com.pragma.customerservice.infraestructure.feign.ICustomerPhotoFeign;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerHystrixFactory implements ICustomerPhotoFeign {

    @Override
    public ResponseEntity<List<CustomerPhoto>> listAllCustomersPhotos() {
        CustomerPhoto customer = CustomerPhoto.builder()
                .idPhoto("none")
                .contentFile(null)
                .nameFile("none").build();
        List<CustomerPhoto> lista = new ArrayList<>();
        lista.add(customer);
        return ResponseEntity.ok(lista);
    }

    @Override
    public ResponseEntity<CustomerPhoto> getCustomerPhoto(String id) {
        CustomerPhoto customer = CustomerPhoto.builder()
                .idPhoto("none")
                .contentFile(null)
                .nameFile("none").build();
        return ResponseEntity.ok(customer);
    }

    @Override
    public ResponseEntity<CustomerPhoto> createCustomerPhoto(CustomerPhoto customer) {
        customer = CustomerPhoto.builder()
                .idPhoto("none")
                .contentFile(null)
                .nameFile("none").build();
        return ResponseEntity.ok(customer);
    }

    @Override
    public ResponseEntity<CustomerPhoto> updateCustomer(String id, CustomerPhoto customer) {
        customer = CustomerPhoto.builder()
                .idPhoto("none")
                .contentFile(null)
                .nameFile("none").build();
        return ResponseEntity.ok(customer);
    }
}
