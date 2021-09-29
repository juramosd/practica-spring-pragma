package co.com.pragma.photoservice.domain.model;

import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long customerId;
    private String lastName;
    private String firstName;
    private String identification;
    private int age;
    private CustomerPhoto photo;
}
