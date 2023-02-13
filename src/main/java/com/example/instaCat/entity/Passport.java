package com.example.instaCat.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String series;
    private String number;

//    @OneToOne(optional = false, mappedBy = "passport")
//    private User owner;
}
