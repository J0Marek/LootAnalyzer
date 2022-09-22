package com.selnix.project.bean;

import com.selnix.project.entity.Item;
import lombok.Data;

@Data
public class ItemDropBean {
    private Item item;
    private int dropAmount;
    private double dropPercentage;
}
