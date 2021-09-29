package co.com.pragma.customerservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPhoto {
    private String idPhoto;
    private String nameFile;
    private byte[] contentFile;
}
