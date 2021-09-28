package co.com.pragma.customerservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPhoto {
    private Long idPhoto;
    private String nameFile;
    private byte[] contentFile;
}
