package com.selnix.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class LootStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne
    Monster monster;

    @OneToOne
    Item item;

    Integer itemsDropped;
}