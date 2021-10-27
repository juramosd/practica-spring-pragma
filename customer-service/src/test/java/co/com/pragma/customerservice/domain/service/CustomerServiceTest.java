package co.com.pragma.customerservice.domain.service;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.application.mainmodule.mapper.ICustomerMapperImpl;
import co.com.pragma.customerservice.infraestructure.repository.webflux.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static co.com.pragma.customerservice.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceTest {
    @Mock
    CustomerRepository repository;
    @InjectMocks
    CustomerService service;
    @Mock
    ICustomerMapperImpl mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        when(repository.getAll()).thenReturn(createListPhotos());
        List<CustomerDto> photos = service.getAll();

        assertEquals(2,photos.size());
    }

    @Test
    void createCustomer() {
        when(repository.save(any(CustomerDto.class))).thenReturn(createPhoto());
        Optional<CustomerDto> photo = service.createCustomer(createPhoto().get());

        assertNotNull(photo);
        assertEquals("40303030",photo.get().getIdentification());
    }

    @Test
    void updateCustomer() {
        when(repository.getAll()).thenReturn(createListPhotos());
        List<CustomerDto> photos = service.getAll();

        CustomerDto newPhoto = photos.get(1);
        newPhoto.setAge(60);
        when(repository.modify(any(CustomerDto.class))).thenReturn(Optional.of(newPhoto));
        //when
        Optional<CustomerDto> photo = service.updateCustomer(newPhoto);

        assertNotNull(photo);
        assertEquals("20202020",photo.get().getIdentification());
        assertEquals(60,photo.get().getAge());
    }

    @Test
    void deleteCustomer() {
        when(repository.save(any(CustomerDto.class))).thenReturn(createPhoto());
        Optional<CustomerDto> photo = service.createCustomer(createPhoto().get());

        Optional<CustomerDto> photoBD = service.deleteCustomer(photo.orElseThrow());

        assertThrows(NoSuchElementException.class,() -> {
            service.getCustomerIdentification(photoBD.orElseThrow().getIdentification());
        });

        assertEquals(0,service.getAll().size());
    }

    @Test
    void getCustomer() {
        when(repository.getCustomer(any(Long.class))).thenReturn(createPhoto());
        CustomerDto photo = service.getCustomer(1L).get();

        assertNotNull(photo);
        assertEquals("40303030",photo.getIdentification());
    }

    @Test
    void getCustomerIdentification() {
        when(repository.getCustomerIdentification(any(String.class))).thenReturn(createPhoto());
        CustomerDto photo = service.getCustomerIdentification("40303030").get();

        assertNotNull(photo);
        assertEquals("40303030",photo.getIdentification());
    }

    @Test
    void getCustomerAgeMax() {
        when(repository.findByAgeGreaterThanEqual(any(Integer.class))).thenReturn(createListPhotos());
        List<CustomerDto> photos = service.getCustomerAgeMax(30);

        assertEquals(2,photos.size());
        assertTrue(photos.size()>0);
    }
}