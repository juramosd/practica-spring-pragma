package co.com.pragma.photoservice.application.mainmodule.mapper;

import co.com.pragma.photoservice.application.mainmodule.dto.CustomerPhotoDto;
import co.com.pragma.photoservice.domain.entity.CustomerPhoto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICustomerPhotoMapper {
    @Mappings({
            @Mapping(source = "id", target = "idPhoto"),
            @Mapping(source = "nameFile", target = "nameFile"),
            @Mapping(source = "contentFile", target = "contentFile"),
    })
    CustomerPhotoDto toCustomerPhotoDTo(CustomerPhoto customer);

    List<CustomerPhotoDto> toCustomersDto(List<CustomerPhoto> customers);

    @InheritInverseConfiguration
    CustomerPhoto toCustomer(CustomerPhotoDto customerDto);
}
