package com.yash.hotelmanagement.entities;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.yash.hotelmanagement.enums.RoomType;

@Entity
@Data
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double price;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer totalRoom;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer available;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;


}