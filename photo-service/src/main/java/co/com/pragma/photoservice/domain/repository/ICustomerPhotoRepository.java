package co.com.pragma.photoservice.domain.repository;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ICustomerPhotoRepository {
    List<CustomerPhotoDto> getAll();
    List<CustomerPhotoDto> findAllByIdIn(List<Long> listIn);
    CustomerPhotoDto get(long customerId);
    CustomerPhotoDto save(CustomerPhotoDto customerDto);
    CustomerPhotoDto modify(CustomerPhotoDto customerDto);
    CustomerPhotoDto delete(CustomerPhotoDto customerDto);
}
