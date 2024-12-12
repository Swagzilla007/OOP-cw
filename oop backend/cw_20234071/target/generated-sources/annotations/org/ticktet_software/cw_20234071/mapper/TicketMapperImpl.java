package org.ticktet_software.cw_20234071.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.ticktet_software.cw_20234071.dto.TicketDTO;
import org.ticktet_software.cw_20234071.entity.Ticket;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-12T12:27:35+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public List<TicketDTO> entityToDto(List<Ticket> tickets) {
        if ( tickets == null ) {
            return null;
        }

        List<TicketDTO> list = new ArrayList<TicketDTO>( tickets.size() );
        for ( Ticket ticket : tickets ) {
            list.add( ticketToTicketDTO( ticket ) );
        }

        return list;
    }

    protected TicketDTO ticketToTicketDTO(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketDTO ticketDTO = new TicketDTO();

        ticketDTO.setPrice( ticket.getPrice() );
        ticketDTO.setSoldOut( ticket.isSoldOut() );
        ticketDTO.setEvent( ticket.getEvent() );
        ticketDTO.setVendor( ticket.getVendor() );
        ticketDTO.setTicketPool( ticket.getTicketPool() );
        ticketDTO.setCustomer( ticket.getCustomer() );

        return ticketDTO;
    }
}
