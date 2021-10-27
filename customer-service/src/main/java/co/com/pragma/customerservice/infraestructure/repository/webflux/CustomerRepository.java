package co.com.pragma.customerservice.infraestructure.repository.webflux;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.application.mainmodule.mapper.ICustomerMapper;
import co.com.pragma.customerservice.domain.entity.Customer;
import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import co.com.pragma.customerservice.domain.repository.ICustomerRepository;
import co.com.pragma.customerservice.infraestructure.crud.ICustomerCrudRepository;
import co.com.pragma.customerservice.infraestructure.fallback.CustomerHystrixFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("repositoryFeignWebFlux")
public class CustomerRepository implements ICustomerRepository {

    @Autowired
    private WebClient webClientBuilder;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private CustomerHystrixFactory customerHystrixFactory;

    @Autowired
    private ICustomerCrudRepository customerCrudRepository;

    @Autowired
    private ICustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getAll() {

        List<Customer> customers = (List<Customer>) customerCrudRepository.findAll().stream().filter(f -> !f.getState().equals("DELETED")).collect(Collectors.toList());

        List<CustomerPhoto> images2 = new ArrayList<>();

        images2 = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.get()
                .uri("/photos").retrieve().bodyToFlux(CustomerPhoto.class)
                .collectList().block(), throwable -> customerHystrixFactory.listAllCustomersPhotos().getBody());

        List<CustomerDto> customersDto = customerMapper.toCustomersDto(customers);
        if (!images2.get(0).getIdPhoto().contentEquals("none")) {
            List<CustomerPhoto> finalImages = images2;
            customersDto.forEach(customerDto -> {
                var imageDto = finalImages.stream().filter(g -> g.getIdPhoto().equals(customerDto.getIdentification())).findFirst();
                if (!imageDto.isEmpty()) {
                    customerDto.setPhoto(imageDto.get());
                }
            });
            return customersDto;
        }

        return customerMapper.toCustomersDto(customers);
    }

    public Optional<CustomerDto> getCustomer(Long customerId) {
        return customerCrudRepository.findById(customerId).map(e -> {
            //var imageCustomer = customerFeign.getCustomerPhoto(e.getIdentification());
            if (e == null) {
                return null;
            }

            if (e.getState().equals("DELETED")) {
                return null;
            }

            var imageCustomer = circuitBreakerFactory.create("circuitbreaker").run(() ->
                            webClientBuilder.get()
                                    .uri("/photos/{id}", e.getIdentification()).retrieve().bodyToMono(CustomerPhoto.class).block()
                    , throwable -> customerHystrixFactory.getCustomerPhoto(e.getIdentification()).getBody());

            var dto = customerMapper.toCustomerDTo(e);
            if (imageCustomer != null) {
                dto.setPhoto((CustomerPhoto) imageCustomer);
            }
            return dto;
        });
    }

    public Optional<CustomerDto> getCustomerIdentification(String customerId) {
        var customerBD = customerCrudRepository.findByIdentification(customerId);
        if (customerBD == null) {
            return null;
        }

        if (customerBD.getState().equals("DELETED")) {
            return null;
        }

        var imageCustomer = circuitBreakerFactory.create("circuitbreaker").run(() ->
                webClientBuilder.get()
                        .uri("/photos/{id}", customerBD.getIdentification()).retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.getCustomerPhoto(customerBD.getIdentification()).getBody());

        var dto = customerMapper.toCustomerDTo(customerBD);
        if (imageCustomer != null) {
            dto.setPhoto((CustomerPhoto) imageCustomer);
        }
        return Optional.ofNullable(dto);

    }

    @Override
    @Transactional
    public Optional<CustomerDto> save(CustomerDto customerDto) {
        Customer customerBD = customerCrudRepository.findByIdentification(customerDto.getIdentification());
        if (customerBD != null) {
            Customer finalCustomerBD = customerBD;
            ResponseEntity images = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.get()
                    .uri("/photos/{id}", finalCustomerBD.getIdentification())
                    .retrieve()
                    .onStatus(status -> status.value() == 404,
                            clientResponse -> Mono.empty())
                    .toEntity(CustomerPhoto.class)
                    .block(), throwable -> customerHystrixFactory.getCustomerPhoto(finalCustomerBD.getIdentification()));

            var photo = images.getStatusCodeValue() == 404 ? null : (CustomerPhoto) images.getBody();
            if (photo != null && !photo.getIdPhoto().contentEquals("none")) {
                photo.setIdPhoto(customerDto.getIdentification());
                photo.setNameFile(customerDto.getPhoto().getNameFile());
                photo.setTypeFile(customerDto.getPhoto().getTypeFile());
                photo.setContentFile(customerDto.getPhoto().getContentFile());
                customerDto = customerMapper.toCustomerDTo(customerBD);
                customerDto.setPhoto(photo);
            } else {
                if (photo == null) {
                    photo = new CustomerPhoto();
                    photo.setIdPhoto(customerDto.getIdentification());
                    photo.setNameFile(customerDto.getPhoto().getNameFile());
                    photo.setTypeFile(customerDto.getPhoto().getTypeFile());
                    photo.setContentFile(customerDto.getPhoto().getContentFile());

                    customerDto = customerMapper.toCustomerDTo(customerBD);
                    CustomerPhoto finalPhoto = photo;
                    CustomerPhoto finalPhoto1 = photo;
                    customerDto.setPhoto(circuitBreakerFactory.create("circuitbreaker").run(() ->  webClientBuilder.post()
                            .uri("/photos")
                            .body(Mono.just(finalPhoto1), CustomerPhoto.class)
                            .retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.createCustomerPhoto(finalPhoto).getBody()));
                }
            }

            return Optional.ofNullable(customerDto);
        }
        customerBD = customerMapper.toCustomer(customerDto);
        customerBD.setState("CREATED");
        customerBD.setCreateAt(new Date());
        customerBD = customerCrudRepository.save(customerBD);
        //CustomerPhoto photo = customerFeign.getCustomerPhoto(customerDto.getIdentification()).getBody();
        Customer finalCustomerBD1 = customerBD;
        ResponseEntity images = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.get()
                .uri("/photos/{id}", finalCustomerBD1.getIdentification())
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.empty())
                .toEntity(CustomerPhoto.class)
                .block(), throwable -> customerHystrixFactory.getCustomerPhoto(finalCustomerBD1.getIdentification()));
        var photo = images.getStatusCodeValue() == 404 ? null : (CustomerPhoto) images.getBody();
        if (photo != null && !photo.getIdPhoto().contentEquals("none")) {
            photo.setNameFile(customerDto.getPhoto().getNameFile());
            photo.setTypeFile(customerDto.getPhoto().getTypeFile());
            photo.setContentFile(customerDto.getPhoto().getContentFile());

            CustomerPhoto finalPhoto = photo;
            photo = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.put()
                    .uri("/photos", finalPhoto.getIdPhoto())
                    .body(Mono.just(finalPhoto), CustomerPhoto.class)
                    .retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.createCustomerPhoto(finalPhoto).getBody());
        } else {
            photo = new CustomerPhoto();
            photo.setIdPhoto(customerDto.getIdentification());
            photo.setNameFile(customerDto.getPhoto().getNameFile());
            photo.setTypeFile(customerDto.getPhoto().getTypeFile());
            photo.setContentFile(customerDto.getPhoto().getContentFile());

            CustomerPhoto finalPhoto  = photo;
            photo = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.post()
                    .uri("/photos")
                    .body(Mono.just(finalPhoto), CustomerPhoto.class)
                    .retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.createCustomerPhoto(finalPhoto).getBody());
        }
        Optional<CustomerDto> dto = Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
        dto.get().setPhoto(photo);
        return dto;
    }

    @Override
    @Transactional
    public Optional<CustomerDto> modify(CustomerDto customerDto) {
        Customer customerBD = customerCrudRepository.findByIdentification(customerDto.getIdentification());
        Customer customerBDAux = customerBD;
        if (customerBD == null) {
            return null;
        }
        customerBD = customerMapper.toCustomer(customerDto);
        customerBD.setState("MODIFY");
        customerBD.setCreateAt(customerBDAux.getCreateAt());
        customerBD = customerCrudRepository.save(customerBD);

        var photo = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.get()
                .uri("/photos/{id}", customerDto.getIdentification()).retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.getCustomerPhoto(customerDto.getIdentification()).getBody());
        photo.setIdPhoto(customerDto.getIdentification());
        photo.setNameFile(customerDto.getPhoto().getNameFile());
        photo.setTypeFile(customerDto.getPhoto().getTypeFile());
        photo.setContentFile(customerDto.getPhoto().getContentFile());
        if (photo != null) {
            CustomerPhoto finalPhoto = photo;
            photo = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.put()
                    .uri("/photos/{id}", finalPhoto.getIdPhoto()).body(Mono.just(finalPhoto), CustomerPhoto.class)
                    .retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.updateCustomer(finalPhoto.getIdPhoto(), finalPhoto).getBody());
        } else {
            CustomerPhoto finalPhoto1 = photo;
            photo = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.post()
                    .uri("/photos")
                    .body(Mono.just(finalPhoto1), CustomerPhoto.class)
                    .retrieve().bodyToMono(CustomerPhoto.class).block(), throwable -> customerHystrixFactory.createCustomerPhoto(finalPhoto1).getBody());
        }
        Optional<CustomerDto> dto = Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
        dto.get().setPhoto(photo);
        return dto;
    }

    @Override
    public Optional<CustomerDto> delete(CustomerDto customerDto) {
        Customer customerBD =
                customerCrudRepository.findByIdentification(customerDto.getIdentification());
        if (customerBD == null) {
            return null;
        }

        customerBD.setState("DELETED");

        //customerCrudRepository.delete(customerMapper.toCustomer(customerBD.get()));
        customerBD = customerCrudRepository.save(customerBD);
        return Optional.ofNullable(customerMapper.toCustomerDTo(customerBD));
    }

    @Override
    public Optional<CustomerDto> deleteBD(CustomerDto customerDto) {
        Optional<CustomerDto> customerBD = getCustomerIdentification(customerDto.getIdentification());
        if (customerBD == null) {
            return null;
        }
        customerCrudRepository.delete(customerMapper.toCustomer(customerBD.get()));
        return customerBD;
    }

    @Override
    public List<CustomerDto> findByAgeGreaterThanEqual(int age) {
        List<Customer> customers = (List<Customer>) customerCrudRepository.findByAgeGreaterThanEqual(age).stream().filter(f -> !f.getState().equals("DELETED")).collect(Collectors.toList());

        List<CustomerPhoto> images = circuitBreakerFactory.create("circuitbreaker").run(() -> webClientBuilder.get()
                .uri("/photos/").retrieve().bodyToFlux(CustomerPhoto.class).collectList().block(), throwable -> customerHystrixFactory.listAllCustomersPhotos().getBody());
        List<CustomerDto> customersDto = customerMapper.toCustomersDto(customers);
        if (!images.isEmpty()) {
            return customersDto.stream().map(f -> {
                var imageDto = images.stream().filter(g -> g.getIdPhoto().contentEquals(f.getIdentification())).findFirst();
                if (!imageDto.isEmpty()) {
                    f.setPhoto(imageDto.get());
                }
                return f;
            }).collect(Collectors.toList());
        }

        return customerMapper.toCustomersDto(customers);
    }
}