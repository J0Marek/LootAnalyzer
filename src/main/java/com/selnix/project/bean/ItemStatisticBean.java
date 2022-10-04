package com.selnix.project.bean;

import com.selnix.project.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemStatisticBean {
    private Item item;
    private int dropAmount;
    private double dropPercentage;
}
