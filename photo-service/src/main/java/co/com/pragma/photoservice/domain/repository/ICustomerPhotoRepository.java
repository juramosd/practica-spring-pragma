package co.com.pragma.photoservice.domain.repository;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ICustomerPhotoRepository {
    List<CustomerPhotoDto> getAll();
    List<CustomerPhotoDto> findAllByIdIn(List<String> listIn);
    Optional<CustomerPhotoDto> getCustomerPhoto(String customerId);
    Optional<CustomerPhotoDto> save(CustomerPhotoDto customerDto);
    Optional<CustomerPhotoDto> modify(CustomerPhotoDto customerDto);
    Optional<CustomerPhotoDto> delete(CustomerPhotoDto customerDto);
}
