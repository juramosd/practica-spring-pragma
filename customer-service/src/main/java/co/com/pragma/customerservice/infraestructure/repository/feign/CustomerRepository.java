package co.com.pragma.customerservice.infraestructure.repository.feign;

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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("repositoryFeign")
public class CustomerRepository implements ICustomerRepository {
    @Autowired
    private ICustomerPhotoFeign customerFeign;
    @Autowired
    private ICustomerCrudRepository customerCrudRepository;

    @Autowired
    private ICustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getAll() {
        List<Customer> customers = (List<Customer>)customerCrudRepository.findAll();
        //List<CustomerPhoto> images = Arrays.asList(restTemplatePhoto.getForObject("http://localhost:8002/clientes-fotos/", CustomerPhoto[].class));
        List<CustomerPhoto> images = customerFeign.listAllCustomersPhotos().getBody();
        List<CustomerDto> customersDto =  customerMapper.toCustomersDto(customers);
        if(!images.isEmpty()){
           return customersDto.stream().map(f-> {
               var imageDto = images.stream().filter(g->g.getIdPhoto().contentEquals(f.getIdentification())).findFirst();
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
            var imageCustomer = customerFeign.getCustomerPhoto(e.getIdentification());
            var dto = customerMapper.toCustomerDTo(e);
            if(imageCustomer!=null){
                dto.setPhoto(imageCustomer.getBody());
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
        customerBD = customerMapper.toCustomer(customerDto);
        customerBD.setState("CREATED");
        customerBD.setCreateAt(new Date());
        customerBD = customerCrudRepository.save(customerBD);
        CustomerPhoto photo = customerFeign.getCustomerPhoto(customerDto.getIdentification()).getBody();
        photo.setIdPhoto(customerDto.getIdentification());
        photo.setNameFile(customerDto.getPhoto().getNameFile());
        photo.setContentFile(customerDto.getPhoto().getContentFile());
        if(photo !=null){
            photo = customerFeign.updateCustomer(photo.getIdPhoto(),photo).getBody();
        }else{
            photo = customerFeign.createCustomerPhoto(photo).getBody();
        }
        Optional<CustomerDto> dto = Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
        dto.get().setPhoto(photo);
        return  dto;
    }

    @Override
    public Optional<CustomerDto> modify(CustomerDto customerDto) {
        Customer customerBD = customerCrudRepository.findByIdentification(customerDto.getIdentification());
        if(customerBD == null){
            return null;
        }
        customerBD = customerMapper.toCustomer(customerDto);
        customerBD.setState("MODIFY");
        customerBD = customerCrudRepository.save(customerBD);

        CustomerPhoto photo = customerFeign.getCustomerPhoto(customerDto.getIdentification()).getBody();
        photo.setIdPhoto(customerDto.getIdentification());
        photo.setNameFile(customerDto.getPhoto().getNameFile());
        photo.setContentFile(customerDto.getPhoto().getContentFile());
        if(photo !=null){
            photo = customerFeign.updateCustomer(photo.getIdPhoto(),photo).getBody();
        }else{
            photo = customerFeign.createCustomerPhoto(photo).getBody();
        }
        Optional<CustomerDto> dto = Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
        dto.get().setPhoto(photo);
        return dto;
    }

    @Override
    public Optional<CustomerDto> delete(CustomerDto customerDto) {
        Optional<CustomerDto> customerBD = getCustomer(customerDto.getCustomerId());
        if(customerBD == null){
            return null;
        }
        Customer customer = customerMapper.toCustomer(customerBD.get());
        customer.setState("DELETED");
        //customerCrudRepository.delete(customerMapper.toCustomer(customerBD.get()));
        customer = customerCrudRepository.save(customer);
        return Optional.ofNullable(customerMapper.toCustomerDTo(customer));
    }

    @Override
    public List<CustomerDto> findByAgeGreaterThanEqual(int age) {
        List<Customer> customers = (List<Customer>)customerCrudRepository.findByAgeGreaterThanEqual(age);

        List<CustomerPhoto> images = customerFeign.listAllCustomersPhotos().getBody();
        List<CustomerDto> customersDto =  customerMapper.toCustomersDto(customers);
        if(!images.isEmpty()){
            return customersDto.stream().map(f-> {
                var imageDto = images.stream().filter(g->g.getIdPhoto().contentEquals(f.getIdentification())).findFirst();
                if(!imageDto.isEmpty()) {
                    f.setPhoto(imageDto.get());
                }
                return f;
            }).collect(Collectors.toList());
        }

        return customerMapper.toCustomersDto(customers);
    }
}
