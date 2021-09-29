package co.com.pragma.photoservice.infraestructure.repository;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.mapper.ICustomerPhotoMapper;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import co.com.pragma.photoservice.domain.model.Customer;
import co.com.pragma.photoservice.domain.repository.ICustomerPhotoRepository;
import co.com.pragma.photoservice.infraestructure.crud.ICustomerPhotoCrudRepository;
import co.com.pragma.photoservice.infraestructure.feign.ICustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerPhotoRepository implements ICustomerPhotoRepository {
    @Autowired
    private ICustomerFeign customerFeign;
    @Autowired
    private ICustomerPhotoCrudRepository customerCrudRepository;

    @Autowired
    private ICustomerPhotoMapper customerMapper;

    @Override
    public List<CustomerPhotoDto> getAll() {
        List<CustomerPhoto> customers = customerCrudRepository.findAll();
        return customerMapper.toCustomersDto(customers);
    }

    @Override
    public List<CustomerPhotoDto> findAllByIdIn(List<String> listIn) {
        List<CustomerPhoto> customers = customerCrudRepository.findAllByIdIn(listIn);
        return customerMapper.toCustomersDto(customers);
    }

    @Override
    public Optional<CustomerPhotoDto> getCustomerPhoto(String customerId) {
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerCrudRepository.findById(customerId).get()));
    }

    @Override
    public Optional<CustomerPhotoDto> save(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if(customerBD != null){
            return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
        }
        customerBD = customerCrudRepository.save(customerMapper.toCustomer(customerDto));
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }

    @Override
    public Optional<CustomerPhotoDto> modify(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if(customerBD == null){
            return null;
        }
        customerBD = customerCrudRepository.save(customerMapper.toCustomer(customerDto));
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }

    @Override
    public Optional<CustomerPhotoDto> delete(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if (customerBD == null){
            return null;
        }
        customerCrudRepository.delete(customerMapper.toCustomer(customerDto));
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }
}
