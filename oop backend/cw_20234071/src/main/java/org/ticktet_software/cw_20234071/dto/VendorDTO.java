package org.ticktet_software.cw_20234071.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ticktet_software.cw_20234071.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorDTO {
    private Long vendorID;

    private String vendorName;
    private String password;


    private int eventNum;
    private int ticketAmount;
    private int releaseInterval;
    private int ticketsPerRelease;

}
