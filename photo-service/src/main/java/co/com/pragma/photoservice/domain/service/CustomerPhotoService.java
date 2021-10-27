package co.com.pragma.photoservice.domain.service;


import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.service.ICustomerPhotoService;
import co.com.pragma.photoservice.domain.repository.ICustomerPhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerPhotoService implements ICustomerPhotoService {

    @Autowired
    private ICustomerPhotoRepository customerRepository;

    @Override
    public List<CustomerPhotoDto> getAll() {
        return customerRepository.getAll();
    }

    @Override
    public List<CustomerPhotoDto> getAllForId(List<String> ids) {
        return customerRepository.findAllByIdIn(ids);
    }

    @Override
    public Optional<CustomerPhotoDto> create(CustomerPhotoDto customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<CustomerPhotoDto> getCustomerPhoto(String id) {
        return customerRepository.getCustomerPhoto(id);
    }

    @Override
    public Optional<CustomerPhotoDto> update(CustomerPhotoDto customer) {
        return customerRepository.modify(customer);
    }

    @Override
    public Optional<CustomerPhotoDto> delete(CustomerPhotoDto customer) {
        return customerRepository.delete(customer);
    }

    @Override
    public Optional<CustomerPhotoDto> deleteBD(CustomerPhotoDto customer) {
        return customerRepository.deleteBD(customer);
    }
}
