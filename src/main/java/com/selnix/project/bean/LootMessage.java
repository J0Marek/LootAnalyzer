package com.selnix.project.bean;

import com.selnix.project.entity.Item;
import com.selnix.project.entity.Monster;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class LootMessage {
    private Monster monster;
    private Map<Item, Integer> items;
}