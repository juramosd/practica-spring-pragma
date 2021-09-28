package co.com.pragma.photoservice.application.mainmodule.service;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;

import java.util.List;

public interface ICustomerPhotoService {

    List<CustomerPhotoDto> getAll();
    List<CustomerPhotoDto> getAllForId(List<Long> ids);
    CustomerPhotoDto create(CustomerPhotoDto customer);
    CustomerPhotoDto get(Long id);
    CustomerPhotoDto update(CustomerPhotoDto customer);
    CustomerPhotoDto delete(CustomerPhotoDto customer);

}
