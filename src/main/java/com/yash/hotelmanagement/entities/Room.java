package com.yash.hotelmanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String roomNumber;

    private String type;        // Deluxe, AC, Non-AC
    private Double price;
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch sbranch;

    public void setBranch(Branch branch) {
    }
}