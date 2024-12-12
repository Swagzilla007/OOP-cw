package org.ticktet_software.cw_20234071.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Vendors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorID;

    private String vendorName;
    private String password;


    private int eventNum;
    private int ticketAmount;
    private int releaseInterval;
    private int ticketsPerRelease;}


