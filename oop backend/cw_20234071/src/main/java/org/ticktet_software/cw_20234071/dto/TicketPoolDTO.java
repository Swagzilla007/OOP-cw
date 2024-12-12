package org.ticktet_software.cw_20234071.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketPoolDTO {

    private Long id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int ticketsSold;
    private Events event;

    private List<Ticket> tickets = new ArrayList<>();

}
