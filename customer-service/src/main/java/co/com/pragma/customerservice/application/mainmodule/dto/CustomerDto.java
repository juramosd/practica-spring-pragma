package co.com.pragma.customerservice.application.mainmodule.dto;

import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long customerId;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @Size(min = 3, message = "Tiene que tener mayor de 3 caracteres")
    private String firstName;
    @NotEmpty
    @Size(min = 8, max=10, message = "Tiene que ser mayor o igual a 8 caracteres")
    private String identification;
    @Min(value = 0, message = "Tiene que ser entero positivo")
    private int age;
    private String city;
    private CustomerPhoto photo;
}
