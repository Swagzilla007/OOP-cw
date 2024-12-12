package org.ticktet_software.cw_20234071.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.entity.Vendor;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-12T12:27:35+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public Vendor vendorDTOToVendor(VendorDTO vendorDTO) {
        if ( vendorDTO == null ) {
            return null;
        }

        Vendor vendor = new Vendor();

        vendor.setVendorID( vendorDTO.getVendorID() );
        vendor.setVendorName( vendorDTO.getVendorName() );
        vendor.setPassword( vendorDTO.getPassword() );
        vendor.setEventNum( vendorDTO.getEventNum() );
        vendor.setTicketAmount( vendorDTO.getTicketAmount() );
        vendor.setReleaseInterval( vendorDTO.getReleaseInterval() );
        vendor.setTicketsPerRelease( vendorDTO.getTicketsPerRelease() );

        return vendor;
    }

    @Override
    public Customer CustomerDTOToCustomer(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setCustomerID( customerDTO.getCustomerID() );
        customer.setCustomerName( customerDTO.getCustomerName() );
        customer.setPassword( customerDTO.getPassword() );
        customer.setEventNum( customerDTO.getEventNum() );
        customer.setTicketAmount( customerDTO.getTicketAmount() );
        customer.setRetrievalInterval( customerDTO.getRetrievalInterval() );
        customer.setTicketPerRetrieve( customerDTO.getTicketPerRetrieve() );

        return customer;
    }

    @Override
    public VendorDTO vendorToVendorDTO(Vendor vendor) {
        if ( vendor == null ) {
            return null;
        }

        VendorDTO vendorDTO = new VendorDTO();

        vendorDTO.setVendorID( vendor.getVendorID() );
        vendorDTO.setVendorName( vendor.getVendorName() );
        vendorDTO.setPassword( vendor.getPassword() );
        vendorDTO.setEventNum( vendor.getEventNum() );
        vendorDTO.setTicketAmount( vendor.getTicketAmount() );
        vendorDTO.setReleaseInterval( vendor.getReleaseInterval() );
        vendorDTO.setTicketsPerRelease( vendor.getTicketsPerRelease() );

        return vendorDTO;
    }

    @Override
    public CustomerDTO CustomerToCustomerDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setCustomerID( customer.getCustomerID() );
        customerDTO.setCustomerName( customer.getCustomerName() );
        customerDTO.setPassword( customer.getPassword() );
        customerDTO.setEventNum( customer.getEventNum() );
        customerDTO.setTicketAmount( customer.getTicketAmount() );
        customerDTO.setRetrievalInterval( customer.getRetrievalInterval() );
        customerDTO.setTicketPerRetrieve( customer.getTicketPerRetrieve() );

        return customerDTO;
    }
}
