package org.ticktet_software.cw_20234071.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@NoArgsConstructor
public class CustomerDTO {
    private Long customerID;

    private String customerName;
    private String password;

    private int buyFromVendor;

//    private int typeNum;
    private int eventNum;
    private int ticketAmount;
    private int retrievalInterval;
    private int ticketPerRetrieve;

//    private List<Ticket> tickets = new ArrayList<>();
}
