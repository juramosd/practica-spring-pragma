package co.com.pragma.photoservice.application.mainmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPhotoDto {
    @Indexed(unique = true)
    private String idPhoto;

    @Indexed(unique = true)
    private String nameFile;

    private String typeFile;

    private byte[] contentFile;
}
