package co.com.pragma.photoservice.application.mainmodule.service;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;

import java.util.List;
import java.util.Optional;

public interface ICustomerPhotoService {

    List<CustomerPhotoDto> getAll();
    List<CustomerPhotoDto> getAllForId(List<String> ids);
    Optional<CustomerPhotoDto> create(CustomerPhotoDto customer);
    Optional<CustomerPhotoDto> getCustomerPhoto(String id);
    Optional<CustomerPhotoDto> update(CustomerPhotoDto customer);
    Optional<CustomerPhotoDto> delete(CustomerPhotoDto customer);
    Optional<CustomerPhotoDto> deleteBD(CustomerPhotoDto customer);

}
