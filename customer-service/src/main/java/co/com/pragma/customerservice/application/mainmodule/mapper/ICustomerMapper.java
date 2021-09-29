package co.com.pragma.customerservice.application.mainmodule.mapper;

import co.com.pragma.customerservice.application.mainmodule.dto.CustomerDto;
import co.com.pragma.customerservice.domain.entity.Customer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICustomerMapper {
    @Mappings({
            @Mapping(source = "id", target = "customerId"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "identification", target = "identification"),
            @Mapping(source = "age", target = "age"),
            @Mapping(source = "city", target = "city"),
            @Mapping(target = "photo", ignore = true)
    })
    CustomerDto toCustomerDTo(Customer customer);

    List<CustomerDto> toCustomersDto(List<Customer> customers);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "state", ignore = true)
    })
    Customer toCustomer(CustomerDto customerDto);
}
