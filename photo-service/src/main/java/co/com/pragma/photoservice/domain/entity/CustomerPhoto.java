package co.com.pragma.photoservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


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

    @Field("type_file")
    private String typeFile;

    @Field("state")
    private String state;

    @Field("content_file")
    private byte[] contentFile;

    @Field(name = "create_at")
    private Date createAt;
}
