package co.com.pragma.photoservice.infraestructure.repository;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.mapper.ICustomerPhotoMapper;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import co.com.pragma.photoservice.domain.repository.ICustomerPhotoRepository;
import co.com.pragma.photoservice.infraestructure.crud.ICustomerPhotoCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerPhotoRepository implements ICustomerPhotoRepository {

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
    public List<CustomerPhotoDto> findAllByIdIn(List<Long> listIn) {
        List<CustomerPhoto> customers = customerCrudRepository.findAllByIdIn(listIn);
        return customerMapper.toCustomersDto(customers);
    }

    @Override
    public CustomerPhotoDto get(long customerId) {
        return customerMapper.toCustomerPhotoDTo(customerCrudRepository.findById(customerId).get());
    }

    @Override
    public CustomerPhotoDto save(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if(customerBD != null){
            return customerMapper.toCustomerPhotoDTo(customerBD);
        }
        customerBD = customerCrudRepository.save(customerMapper.toCustomer(customerDto));
        return customerMapper.toCustomerPhotoDTo(customerBD);
    }

    @Override
    public CustomerPhotoDto modify(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if(customerBD == null){
            return null;
        }
        customerBD = customerCrudRepository.save(customerMapper.toCustomer(customerDto));
        return customerMapper.toCustomerPhotoDTo(customerBD);
    }

    @Override
    public CustomerPhotoDto delete(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if (customerBD == null){
            return null;
        }
        customerCrudRepository.delete(customerMapper.toCustomer(customerDto));
        return customerMapper.toCustomerPhotoDTo(customerBD);
    }
}
