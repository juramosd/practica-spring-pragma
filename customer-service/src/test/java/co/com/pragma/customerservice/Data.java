package co.com.pragma.customerservice;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.domain.entity.Customer;
import co.com.pragma.customerservice.domain.model.CustomerPhoto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Data {
    public final static List<CustomerDto> PHOTOS = Arrays.asList(
            new CustomerDto(1L,"VASQUEZ","VICTOR","30303030",40,"TRUJILLO", new CustomerPhoto("","image2.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8))),
            new CustomerDto(2L,"RAMOS","JUNIOR","20202020",20,"TRUJILLO", new CustomerPhoto("","image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8))));

    public final static CustomerDto PHOTO = new CustomerDto(1L,"VASQUEZ","JENIFER","40303030",40,"TRUJILLO", new CustomerPhoto("","image2.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)));

    public static List<CustomerDto> createListPhotos(){
        return Arrays.asList(
                new CustomerDto(1L,"VASQUEZ","VICTOR","30303030",40,"TRUJILLO", new CustomerPhoto("","image2.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8))),
                new CustomerDto(2L,"RAMOS","JUNIOR","20202020",20,"TRUJILLO", new CustomerPhoto("","image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8))));

    }

    public static Optional<CustomerDto> createPhoto(){
        return Optional.of(new CustomerDto(1L,"JENIFER","VASQUEZ","40303030",40,"TRUJILLO", new CustomerPhoto("","image2.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8))));
    }

    public static Customer createCustomer(){
        return new Customer((Long)1L,"JENIFER","VASQUEZ","40303030",40,"TRUJILLO","CREATED",new Date());
    }
}


