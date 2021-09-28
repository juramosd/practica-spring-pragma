package co.com.pragma.photoservice.domain.service;


import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.service.ICustomerPhotoService;
import co.com.pragma.photoservice.domain.repository.ICustomerPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerPhotoService implements ICustomerPhotoService {

    @Autowired
    ICustomerPhotoRepository customerRepository;

    @Override
    public List<CustomerPhotoDto> getAll() {
        return customerRepository.getAll();
    }

    @Override
    public List<CustomerPhotoDto> getAllForId(List<Long> ids) {
        return customerRepository.findAllByIdIn(ids);
    }

    @Override
    public CustomerPhotoDto create(CustomerPhotoDto customer) {
        return customerRepository.save(customer);
    }

    @Override
    public CustomerPhotoDto get(Long id) {
        return customerRepository.get(id);
    }

    @Override
    public CustomerPhotoDto update(CustomerPhotoDto customer) {
        return customerRepository.modify(customer);
    }

    @Override
    public CustomerPhotoDto delete(CustomerPhotoDto customer) {
        return customerRepository.delete(customer);
    }
}
