package co.com.pragma.customerservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "tbl_customer")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Long id;
    @NotEmpty(message="El Apellido no puede ser vacio")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @NotEmpty(message ="El Nombre no puede ser vacio")
    @Column(name = "first_name", nullable = false)
    @Size(min = 3, message = "Tiene que tener mayor de 3 caracteres")
    private String firstName;
    @NotEmpty
    @Size(min = 8, max=10, message = "Tiene que ser mayor o igual a 8 caracteres")
    @Column(unique = true,nullable = false)
    private String identification;
    @Min(value = 0, message = "Tiene que ser entero positivo")
    private int age;
    private String city;
    private String state;
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;
}
