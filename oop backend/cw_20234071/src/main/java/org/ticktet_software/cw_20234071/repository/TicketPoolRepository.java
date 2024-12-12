package org.ticktet_software.cw_20234071.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.TicketPool;

public interface TicketPoolRepository extends JpaRepository<TicketPool, Long> {
}
