package org.ticktet_software.cw_20234071.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.Optional;

@Entity
@Table(name = "Tickets")
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;


    private double price;
    private boolean soldOut;
    private Events event;

    @ManyToOne
    @JoinColumn(name = "vendor_id") // Specify the column for the foreign key
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "TicketPool_id")
    private TicketPool ticketPool;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
