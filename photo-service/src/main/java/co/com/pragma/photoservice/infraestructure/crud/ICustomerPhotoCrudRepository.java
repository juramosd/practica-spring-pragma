package co.com.pragma.photoservice.infraestructure.crud;

import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ICustomerPhotoCrudRepository extends MongoRepository<CustomerPhoto,Long> {
    List<CustomerPhoto> findAllByIdIn(List<Long> listIn);
}
