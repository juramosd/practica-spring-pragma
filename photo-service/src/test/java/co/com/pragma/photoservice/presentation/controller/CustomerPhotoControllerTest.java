package co.com.pragma.photoservice.presentation.controller;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.service.CustomerPhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static co.com.pragma.photoservice.Data.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerPhotoController.class)
class CustomerPhotoControllerTest {

    @Autowired
    private MockMvc mvc;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @MockBean
    private CustomerPhotoService customerPhotoService;

    @Test
    void listAllCustomersPhotos() throws Exception {
        when(customerPhotoService.getAll()).thenReturn(createListPhotos());

        mvc.perform(get("/photos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void getCustomerPhoto() throws Exception {
        when(customerPhotoService.getCustomerPhoto("45224800")).thenReturn(createPhoto());

        mvc.perform(get("/photos/45224800").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPhoto").value("45224800"));

        verify(customerPhotoService).getCustomerPhoto("45224800");
    }

    @Test
    void createCustomerPhoto() throws Exception {
        when(customerPhotoService.create(createPhoto().get())).thenReturn(createPhoto());

        CustomerPhotoDto dto = new CustomerPhotoDto();
        dto.setIdPhoto("45224810");
        dto.setNameFile("image.jpg");
        dto.setTypeFile("base64");
        dto.setContentFile("base64".getBytes(StandardCharsets.UTF_8));

        mvc.perform(post("/photos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

    }

    @Test
    void updateCustomer() throws Exception {
        CustomerPhotoDto dto = new CustomerPhotoDto();
        dto.setIdPhoto("45224817");
        dto.setNameFile("image.jpg");
        dto.setTypeFile("base64");
        dto.setContentFile("base64".getBytes(StandardCharsets.UTF_8));

        when(customerPhotoService.update(any(CustomerPhotoDto.class))).thenReturn(java.util.Optional.of(dto));

        mvc.perform(put("/photos/45224817")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteCustomer() throws Exception{
        CustomerPhotoDto dto = new CustomerPhotoDto();
        dto.setIdPhoto("45224800");
        dto.setNameFile("image.jpg");
        dto.setTypeFile("base64");
        dto.setContentFile("base64".getBytes(StandardCharsets.UTF_8));

        when(customerPhotoService.deleteBD(any(CustomerPhotoDto.class))).thenReturn(java.util.Optional.of(dto));

        mvc.perform(delete("/photos/45224817")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}