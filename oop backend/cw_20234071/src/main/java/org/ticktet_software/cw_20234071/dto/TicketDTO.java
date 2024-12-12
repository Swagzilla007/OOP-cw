package org.ticktet_software.cw_20234071.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketDTO {
    private Long ticketID;

    private double price;
    private boolean soldOut;
    private Events event;

    private Vendor vendor;
    private TicketPool ticketPool;
    private Customer customer;


}
