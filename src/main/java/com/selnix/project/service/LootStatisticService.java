package com.selnix.project.service;

import com.selnix.project.bean.ItemStatisticBean;
import com.selnix.project.bean.LootMessage;
import com.selnix.project.bean.MonsterStatisticBean;
import com.selnix.project.entity.Item;
import com.selnix.project.entity.LootStatistic;
import com.selnix.project.entity.Monster;
import com.selnix.project.repository.LootStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LootStatisticService {

    private final LootStatisticRepository lootStatisticRepository;
    
    public void createLootStatisticsFromLootMessage(LootMessage lootMessage) {
        lootMessage
                .getItems()
                .forEach((item, amount) -> createLootStatistic(lootMessage.getMonster(), item, amount));
    }

    public void createLootStatistic(Monster monster, Item item, int itemsDropped) {
        Optional<LootStatistic> lootStatisticOptional = lootStatisticRepository.findByNameAndItem(monster, item);
        LootStatistic stat;
        if (lootStatisticOptional.isPresent()) {
            stat = lootStatisticOptional.get();
            stat.setAmountOfDroppedItems(stat.getAmountOfDroppedItems() + itemsDropped);

        } else {
            stat = new LootStatistic();
            stat.setItem(item);
            stat.setMonster(monster);
            stat.setAmountOfDroppedItems(itemsDropped);
        }
        lootStatisticRepository.save(stat);
    }

    public MonsterStatisticBean getMonsterStatisticBean(Monster monster) {
        List<LootStatistic> statisticsOfMonster = lootStatisticRepository.getLootStatisticsOfMonster(monster);

        List<ItemStatisticBean> itemStatisticBeanList = statisticsOfMonster.stream()
                .map(lootStatistic -> createItemStatisticBean(lootStatistic, monster))
                .toList();

        MonsterStatisticBean monsterStatisticBean = new MonsterStatisticBean();
        monsterStatisticBean.setMonster(monster);
        monsterStatisticBean.setItemStatisticBeanList(itemStatisticBeanList);
        return monsterStatisticBean;
    }

    private ItemStatisticBean createItemStatisticBean(LootStatistic lootStatistic, Monster monster) {
        Item item = lootStatistic.getItem();
        int dropAmount = lootStatistic.getAmountOfDroppedItems();
        double dropPercentage = (double) monster.getMonstersKilled() / lootStatistic.getAmountOfDroppedItems();
        return new ItemStatisticBean(item, dropAmount, dropPercentage);
    }
}
