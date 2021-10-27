package co.com.pragma.photoservice;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Data {
    public final static List<CustomerPhotoDto> PHOTOS = Arrays.asList(
            new CustomerPhotoDto("45224817", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)),
            new CustomerPhotoDto("45224816", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)));

    public final static CustomerPhotoDto PHOTO = new CustomerPhotoDto("45224817", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8));

    public static List<CustomerPhotoDto> createListPhotos(){
        return Arrays.asList(
                new CustomerPhotoDto("45224817", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)),
                new CustomerPhotoDto("45224816", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)));

    }

    public static List<CustomerPhoto> createListPhotosBD(){
        return Arrays.asList(
                new CustomerPhoto("45224817", "image.jpg", "base64","CREATED", "123123123".getBytes(StandardCharsets.UTF_8),new Date()),
                new CustomerPhoto("45224816", "image.jpg", "base64","CREATED", "123123123".getBytes(StandardCharsets.UTF_8),new Date()));

    }

    public static Optional<CustomerPhotoDto> createPhoto(){
        return Optional.of(new CustomerPhotoDto("45224800", "image.jpg", "base64", "123123123".getBytes(StandardCharsets.UTF_8)));
    }

    public static Optional<CustomerPhoto> createPhotoBD(){
        return Optional.of(new CustomerPhoto("45224800", "image.jpg", "base64","CREATED", "123123123".getBytes(StandardCharsets.UTF_8),new Date()));
    }
}


