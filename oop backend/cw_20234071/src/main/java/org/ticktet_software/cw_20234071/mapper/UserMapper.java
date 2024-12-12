package org.ticktet_software.cw_20234071.mapper;

import org.mapstruct.Mapper;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.entity.Vendor;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Vendor vendorDTOToVendor(VendorDTO vendorDTO);
    Customer CustomerDTOToCustomer(CustomerDTO customerDTO);

    VendorDTO vendorToVendorDTO(Vendor vendor);
    CustomerDTO CustomerToCustomerDTO(Customer customer);

}
