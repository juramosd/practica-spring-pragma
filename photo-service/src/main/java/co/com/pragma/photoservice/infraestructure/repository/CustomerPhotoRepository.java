package co.com.pragma.photoservice.infraestructure.repository;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.mapper.ICustomerPhotoMapper;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import co.com.pragma.photoservice.domain.repository.ICustomerPhotoRepository;
import co.com.pragma.photoservice.infraestructure.crud.ICustomerPhotoCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerPhotoRepository implements ICustomerPhotoRepository {
    @Autowired
    ICustomerPhotoCrudRepository customerCrudRepository;

    @Autowired
    ICustomerPhotoMapper customerMapper;

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
        CustomerPhoto customerBD = customerCrudRepository.findById(customerId).filter(f-> !f.getState().equals("DELETED")).orElse(null);
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }

    @Override
    public Optional<CustomerPhotoDto> save(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if(customerBD != null){
            return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
        }
        CustomerPhoto photoDB = customerMapper.toCustomer(customerDto);
        photoDB.setCreateAt(new Date());
        photoDB.setState("CREATED");
        customerBD = customerCrudRepository.save(photoDB);
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }

    @Override
    public Optional<CustomerPhotoDto> modify(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if(customerBD == null){
            return null;
        }
        CustomerPhoto photoDB = customerMapper.toCustomer(customerDto);
        photoDB.setCreateAt(customerBD.getCreateAt());
        photoDB.setState("MODIFY");
        customerBD = customerCrudRepository.save(photoDB);
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }

    @Override
    public Optional<CustomerPhotoDto> delete(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if (customerBD == null){
            return null;
        }
        //customerCrudRepository.delete(customerMapper.toCustomer(customerDto));
        customerBD.setState("DELETED");
        customerBD = customerCrudRepository.save(customerBD);
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }

    @Override
    public Optional<CustomerPhotoDto> deleteBD(CustomerPhotoDto customerDto) {
        CustomerPhoto customerBD = customerCrudRepository.findById(customerDto.getIdPhoto()).orElse(null);
        if (customerBD == null){
            return null;
        }
        customerCrudRepository.delete(customerMapper.toCustomer(customerDto));
        return Optional.ofNullable(customerMapper.toCustomerPhotoDTo(customerBD));
    }
}
