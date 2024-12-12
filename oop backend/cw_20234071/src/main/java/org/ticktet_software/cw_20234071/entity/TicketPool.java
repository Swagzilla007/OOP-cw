package org.ticktet_software.cw_20234071.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TicketPool")
@Getter
@Setter
@Component  // Add @Component here to make TicketPool a Spring Bean
@NoArgsConstructor
public class TicketPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int ticketInPool;
    private int ticketsSold;
    private Events event;

}
