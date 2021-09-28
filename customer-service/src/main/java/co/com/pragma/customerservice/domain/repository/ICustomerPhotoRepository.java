package co.com.pragma.customerservice.domain.repository;

import co.com.pragma.customerservice.domain.model.CustomerPhoto;

import java.util.List;

public interface ICustomerPhotoRepository {
    List<CustomerPhoto> getAll();
}
