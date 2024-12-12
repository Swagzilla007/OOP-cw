package org.ticktet_software.cw_20234071.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.Vendor;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByVendor(Vendor vendor);
}
