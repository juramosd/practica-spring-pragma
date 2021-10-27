package co.com.pragma.photoservice.infraestructure.repository;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.application.mainmodule.mapper.ICustomerPhotoMapper;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import co.com.pragma.photoservice.infraestructure.crud.ICustomerPhotoCrudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static co.com.pragma.photoservice.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@TestPropertySource(locations = "classpath:test.properties")
@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class CustomerPhotoRepositoryTest {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    @Mock
    ICustomerPhotoMapper mapper;
    @Mock
    ICustomerPhotoCrudRepository customerCrudRepository;
    @InjectMocks
    CustomerPhotoRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createListPhotosBD().forEach(f->customerCrudRepository.save(f));
    }

    @Test
    void getAll() {
        when(customerCrudRepository.findAll()).thenReturn(createListPhotosBD());
        when(mapper.toCustomersDto(any(List.class))).thenReturn(createListPhotos());
        List<CustomerPhotoDto> photos = repository.getAll();

        assertTrue(photos.size()>0);
    }

    @Test
    void getCustomerPhoto() {
        var algo = createPhotoBD();
        algo.get().setId("45224817");
        var dto = createPhoto();
        dto.get().setIdPhoto("45224817");
        when(customerCrudRepository.findById(any(String.class))).thenReturn(algo);
        when(mapper.toCustomerPhotoDTo(any(CustomerPhoto.class))).thenReturn(dto.get());
        Optional<CustomerPhotoDto> photo = repository.getCustomerPhoto("45224817");

        assertTrue(photo.isPresent());
        assertEquals("45224817",photo.orElseThrow().getIdPhoto());
    }

    @Test
    void saveExist() {
        var algo = createPhotoBD();
        algo.get().setId("45224817");
        var dto = createPhoto();
        dto.get().setIdPhoto("45224817");
        when(customerCrudRepository.findById(any(String.class))).thenReturn(algo);
        when(mapper.toCustomerPhotoDTo(any(CustomerPhoto.class))).thenReturn(dto.get());

        CustomerPhotoDto photo = new CustomerPhotoDto("45224817", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8));

        Optional<CustomerPhotoDto> photoBD = repository.save(photo);
        assertTrue(photoBD.isPresent());
        assertEquals("45224817",photoBD.orElseThrow().getIdPhoto());
    }

    @Test
    void saveNotExist() {
        CustomerPhoto newPhoto = new CustomerPhoto();
        when(customerCrudRepository.findById(any(String.class))).thenReturn(Optional.empty());
        when(mapper.toCustomerPhotoDTo(any(CustomerPhoto.class))).thenReturn(createPhoto().get());
        when(customerCrudRepository.save(any(CustomerPhoto.class))).thenReturn(createPhotoBD().get());
        when(mapper.toCustomer(any(CustomerPhotoDto.class))).thenReturn(createPhotoBD().get());
        CustomerPhotoDto photo = new CustomerPhotoDto("45224800", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8));

        Optional<CustomerPhotoDto> photoBD = repository.save(photo);
        assertTrue(photoBD.isPresent());
        assertEquals("45224800",photoBD.orElseThrow().getIdPhoto());
    }

    @Test
    void modify() {
        var algo = createPhotoBD();
        algo.get().setId("45224817");
        var dto = createPhoto();
        dto.get().setIdPhoto("45224817");
        when(customerCrudRepository.findById(any(String.class))).thenReturn(algo);
        when(mapper.toCustomer(any(CustomerPhotoDto.class))).thenReturn(algo.get());
        when(mapper.toCustomerPhotoDTo(any(CustomerPhoto.class))).thenReturn(dto.get());
        when(customerCrudRepository.save(any(CustomerPhoto.class))).thenReturn(algo.get());
        Optional<CustomerPhotoDto> photo = repository.getCustomerPhoto("45224817");
        photo.get().setNameFile("image3.jpg");
        Optional<CustomerPhotoDto> photoBD = repository.modify(photo.get());
        assertTrue(photoBD.isPresent());
        assertEquals("image3.jpg",photoBD.orElseThrow().getNameFile());
    }
//
    @Test
    void delete() {
        var algo = createPhotoBD();
        algo.get().setId("45224817");
        var dto = createPhoto();
        dto.get().setIdPhoto("45224817");
        when(customerCrudRepository.findById(any(String.class))).thenReturn(algo);
        when(mapper.toCustomer(any(CustomerPhotoDto.class))).thenReturn(algo.get());
        when(mapper.toCustomerPhotoDTo(any(CustomerPhoto.class))).thenReturn(dto.get());
        when(customerCrudRepository.save(any(CustomerPhoto.class))).thenReturn(algo.get());

        CustomerPhotoDto photo3 = new CustomerPhotoDto("45224800", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8));

        Optional<CustomerPhotoDto> photoB1 = repository.save(photo3);
        List<CustomerPhotoDto> photos = repository.getAll();
        assertEquals(0,photos.size());
        Optional<CustomerPhotoDto> photo = repository.getCustomerPhoto("45224816");
        Optional<CustomerPhotoDto> photoBD = repository.delete(photo.orElseThrow());
        Optional<CustomerPhotoDto> photo2 = repository.getCustomerPhoto("45224816");
        assertTrue(photoBD.isPresent());
        assertFalse(photo2.isPresent());
    }
}