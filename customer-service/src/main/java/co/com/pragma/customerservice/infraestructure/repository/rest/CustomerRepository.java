package co.com.pragma.customerservice.infraestructure.repository.rest;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.application.mainmodule.mapper.ICustomerMapper;
import co.com.pragma.customerservice.domain.entity.Customer;
import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import co.com.pragma.customerservice.domain.repository.ICustomerRepository;
import co.com.pragma.customerservice.infraestructure.crud.ICustomerCrudRepository;
import co.com.pragma.customerservice.infraestructure.feign.ICustomerPhotoFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("repositoryRest")
public class CustomerRepository implements ICustomerRepository {
    @Autowired
    private RestTemplate restTemplatePhoto;

    @Autowired
    private ICustomerCrudRepository customerCrudRepository;

    @Autowired
    private ICustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getAll() {
        List<Customer> customers = (List<Customer>)customerCrudRepository.findAll();
        List<CustomerPhoto> images = Arrays.asList(restTemplatePhoto.getForObject("http://localhost:8002/clientes-fotos/", CustomerPhoto[].class));

        List<CustomerDto> customersDto =  customerMapper.toCustomersDto(customers);
        if(!images.isEmpty()){
           return customersDto.stream().map(f-> {
               var imageDto = images.stream().filter(g->g.getIdPhoto()==f.getIdentification()).findFirst();
               if(!imageDto.isEmpty()) {
                   f.setPhoto(imageDto.get());
               }
               return f;
           }).collect(Collectors.toList());
        }

        return customerMapper.toCustomersDto(customers);
    }

    public Optional<CustomerDto> getCustomer(Long customerId) {
        return  customerCrudRepository.findById(customerId).map(e -> {
            Optional<CustomerPhoto> imageCustomer = Arrays.asList(restTemplatePhoto.getForObject("http://localhost:8002/clientes-fotos/{id}", CustomerPhoto[].class,customerId)).stream().findFirst();
            var dto = customerMapper.toCustomerDTo(e);
            if(imageCustomer!=null){
                dto.setPhoto(imageCustomer.get());
            }
            return dto;
        });
    }

    @Override
    public Optional<CustomerDto> save(CustomerDto customerDto) {
        Customer customerBD = customerCrudRepository.findByIdentification(customerDto.getIdentification());
        if(customerBD != null){
            return Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
        }

        customerBD.setCreateAt(new Date());
        customerBD = customerCrudRepository.save(customerMapper.toCustomer(customerDto));
        return Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
    }

    @Override
    public Optional<CustomerDto> modify(CustomerDto customerDto) {
        Customer customerBD = customerCrudRepository.findByIdentification(customerDto.getIdentification());
        if(customerBD == null){
            return null;
        }
        customerBD = customerCrudRepository.save(customerMapper.toCustomer(customerDto));
        return Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
    }

    @Override
    public Optional<CustomerDto> delete(CustomerDto customerDto) {
        Optional<CustomerDto> customerBD = getCustomer(customerDto.getCustomerId());
        if(customerBD == null){
            return null;
        }
        customerCrudRepository.delete(customerMapper.toCustomer(customerBD.get()));
        return customerBD;
    }

    @Override
    public List<CustomerDto> findByAgeGreaterThanEqual(int age) {
        List<Customer> customers = (List<Customer>)customerCrudRepository.findByAgeGreaterThanEqual(age);
        return customerMapper.toCustomersDto(customers);
    }

    @Override
    public Optional<CustomerDto> getCustomerIdentification(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<CustomerDto> deleteBD(CustomerDto customerDto) {
        return Optional.empty();
    }
}
