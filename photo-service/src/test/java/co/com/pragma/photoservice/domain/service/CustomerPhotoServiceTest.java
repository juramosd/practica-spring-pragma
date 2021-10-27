package co.com.pragma.photoservice.domain.service;

import co.com.pragma.photoservice.Data;
import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.mapper.ICustomerPhotoMapperImpl;
import co.com.pragma.photoservice.application.mainmodule.service.ICustomerPhotoService;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import co.com.pragma.photoservice.infraestructure.repository.CustomerPhotoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static co.com.pragma.photoservice.Data.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest
class CustomerPhotoServiceTest {
    @Mock
    CustomerPhotoRepository repository;
    @InjectMocks
    CustomerPhotoService service;
    @Mock
    ICustomerPhotoMapperImpl mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        when(repository.getAll()).thenReturn(createListPhotos());
        List<CustomerPhotoDto> photos = service.getAll();

        assertEquals(2,photos.size());
    }

    @Test
    void create() {
        when(repository.save(any(CustomerPhotoDto.class))).thenReturn(createPhoto());
        Optional<CustomerPhotoDto> photo = service.create(createPhoto().get());

        assertNotNull(photo);
        assertEquals("45224800",photo.get().getIdPhoto());

    }

    @Test
    void getCustomerPhoto() {
        when(repository.getCustomerPhoto("45224800")).thenReturn(createPhoto());
        CustomerPhotoDto photo = service.getCustomerPhoto("45224800").get();

        assertNotNull(photo);
        assertEquals("45224800",photo.getIdPhoto());
    }

    @Test
    void update() {
        //given
        when(repository.getAll()).thenReturn(createListPhotos());
        List<CustomerPhotoDto> photos = service.getAll();

        CustomerPhotoDto newPhoto = photos.get(1);
        newPhoto.setNameFile("image2.jpg");
        when(repository.modify(any(CustomerPhotoDto.class))).thenReturn(Optional.of(newPhoto));
        //when
        Optional<CustomerPhotoDto> photo = service.update(photos.get(1));

        assertNotNull(photo);
        assertEquals("45224816",photo.get().getIdPhoto());
        assertEquals("image2.jpg",photo.get().getNameFile());
    }

    @Test
    void delete() {
        when(repository.save(any(CustomerPhotoDto.class))).thenReturn(createPhoto());
        Optional<CustomerPhotoDto> photo = service.create(createPhoto().get());

        Optional<CustomerPhotoDto> photoBD = service.delete(photo.orElseThrow());

        assertThrows(NoSuchElementException.class,() -> {
            service.getCustomerPhoto(photoBD.orElseThrow().getIdPhoto());
                });

        assertEquals(0,service.getAll().size());

    }
}