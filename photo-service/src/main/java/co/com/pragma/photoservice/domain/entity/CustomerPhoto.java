package co.com.pragma.photoservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "customer_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPhoto {
    @Id
    @Field("id_image")
    private String id;

    @Indexed(unique = true)
    @Field("name_file")
    private String nameFile;

    @Field("content_file")
    private byte[] contentFile;
}
