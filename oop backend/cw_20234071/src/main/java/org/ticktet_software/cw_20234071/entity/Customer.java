package org.ticktet_software.cw_20234071.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@Component  // Add @Component here to make TicketPool a Spring Bean
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;

    private String customerName;
    private String password;

//    private int buyFromVendor;

//    private int typeNum;
    private int eventNum;
    private int ticketAmount;
    private int retrievalInterval;
    private int ticketPerRetrieve;

//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Ticket> tickets = new ArrayList<>();

}
