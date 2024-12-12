package org.ticktet_software.cw_20234071.mapper;

import org.mapstruct.Mapper;
import org.ticktet_software.cw_20234071.dto.TicketDTO;
import org.ticktet_software.cw_20234071.entity.Ticket;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    List<TicketDTO> entityToDto(List<Ticket> tickets);
}
