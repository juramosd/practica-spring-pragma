package co.com.pragma.customerservice.infraestructure.repository.webflux;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.domain.entity.Customer;
import co.com.pragma.customerservice.domain.model.CustomerPhoto;
import co.com.pragma.customerservice.domain.repository.ICustomerRepository;
import co.com.pragma.customerservice.infraestructure.crud.ICustomerCrudRepository;
import co.com.pragma.customerservice.infraestructure.fallback.CustomerHystrixFactory;
import co.com.pragma.customerservice.infraestructure.feign.ICustomerPhotoFeign;
import co.com.pragma.customerservice.presentation.web.config.SecurityPermitAllConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Replace;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static co.com.pragma.customerservice.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ComponentScan(value = "co.com.pragma.customerservice")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles(value = "test")
class CustomerRepositoryTest {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
    public static MockWebServer mockBackEnd;
    @MockBean
    public static CircuitBreaker circuitBreaker;

    @Autowired
    @Qualifier(value="repositoryFeignWebFlux")
    private CustomerRepository repository;

    @MockBean
    private CircuitBreakerFactory circuitBreakerFactory;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ICustomerCrudRepository customerCrudRepository;



    @Autowired
    private CustomerHystrixFactory customerHystrixFactory;

    @MockBean
    private ICustomerPhotoFeign customerFeign;

    @Autowired
    private WebClient webClientBuilder;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
//        String selectQuery = "alter table tbl_customer  AUTO_INCREMENT=1;";
//
//        jdbcTemplate.execute(selectQuery);
//
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());

        webClientBuilder = WebClient.create(baseUrl);
        circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
//        createListPhotos().forEach(f->repository.save(f));

    }

//    @AfterEach
//    void cleaner() {
//        //createListPhotos().forEach(f->repository.deleteBD(f));
//        //CustomerDto photo = new CustomerDto("45224800", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8));
//        //repository.deleteBD(photo);
//    }

    @Test
    void getAll() {
        List<CustomerDto> photos = repository.getAll();

        assertTrue(photos.size()>0);
    }

    @Test
    void getCustomer() {

        ResponseEntity<CustomerPhoto> entity = ResponseEntity.status(HttpStatus.OK).build();
        when(customerCrudRepository.findById(1L)).thenReturn(Optional.of(createCustomer()));
        Optional<CustomerDto> photo = repository.getCustomer(1L);
        when(customerHystrixFactory.getCustomerPhoto("")).thenReturn(entity);
        mockBackEnd.enqueue(new MockResponse()
                 .setBody("1")
                .addHeader("Content-Type", "application/json"));

        assertTrue(photo.isPresent());
        assertEquals("VICTOR",photo.orElseThrow().getFirstName());
    }

    @Test
    void getCustomerIdentification() {
        Optional<CustomerDto> photo = repository.getCustomerIdentification("20202020");

        assertTrue(photo.isPresent());
        assertEquals("JUNIOR",photo.orElseThrow().getFirstName());
    }

    @Test
    void save() {
        CustomerDto photo = new CustomerDto(3L,"CARLOS","VASQUEZ","80303030",45,"TRUJILLO", new CustomerPhoto("","image2.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)));

        Optional<CustomerDto> photoBD = repository.save(photo);
        assertTrue(photoBD.isPresent());
        assertNotEquals("45224800",photoBD.orElseThrow().getIdentification());
    }

    @Test
    void modify() {
        Optional<CustomerDto> photo = repository.getCustomerIdentification("20202020");
        photo.get().setAge(50);
        Optional<CustomerDto> photoBD = repository.modify(photo.get());
        assertTrue(photoBD.isPresent());
        assertEquals(50,photoBD.orElseThrow().getAge());
    }

    @Test
    void delete() {
        List<CustomerDto> photos = repository.getAll();
        assertEquals(2,photos.size());
        Optional<CustomerDto> photo = repository.getCustomerIdentification("20202020");
        Optional<CustomerDto> photoBD = repository.delete(photo.orElseThrow());
        Optional<CustomerDto> photo2 = repository.getCustomerIdentification("20202020");
        assertTrue(photoBD.isPresent());
        assertThrows(NullPointerException.class,
        () -> {
            assertTrue(photo2.isPresent());
        });
    }

    @Test
    void findByAgeGreaterThanEqual() {
        List<CustomerDto> photos = repository.findByAgeGreaterThanEqual(30);

        assertTrue(photos.size()>0);
}
}