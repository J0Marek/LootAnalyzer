package com.selnix.project.bean;

import com.selnix.project.entity.Monster;
import lombok.Data;

import java.util.List;

@Data
public class MonsterStatisticBean {
    private Monster monster;
    private List<ItemDropBean> itemDropBeanList;
}
