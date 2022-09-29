package com.selnix.project.service;

import com.selnix.project.bean.ItemDropBean;
import com.selnix.project.bean.MonsterStatisticBean;
import com.selnix.project.entity.Item;
import com.selnix.project.entity.LootStatistic;
import com.selnix.project.entity.Monster;
import com.selnix.project.repository.LootStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LootStatisticService {

    private final LootStatisticRepository lootStatisticRepository;

    private final MonsterService monsterService;

    public void createLootStatistic(Monster monster, Item item, int itemsDropped) {
        Optional<LootStatistic> lootStatisticOptional = lootStatisticRepository.findByNameAndItem(monster, item);
        LootStatistic stat;
        if (lootStatisticOptional.isPresent()) {
            stat = lootStatisticOptional.get();
            stat.setItemsDropped(stat.getItemsDropped() + itemsDropped);

        } else {
            stat = new LootStatistic();
            stat.setItem(item);
            stat.setMonster(monster);
            stat.setItemsDropped(itemsDropped);
        }
        lootStatisticRepository.save(stat);
    }

    public MonsterStatisticBean getMonsterStatistic(Monster monster) {
        List<LootStatistic> statisticsOfMonster = lootStatisticRepository.getLootStatisticsOfMonster(monster);

        List<ItemDropBean> itemDropBeanList = new ArrayList<>();
        for (LootStatistic lootStatistic : statisticsOfMonster) {
            Item item = lootStatistic.getItem();
            int dropAmount = lootStatistic.getItemsDropped();
            double dropPercentage = (double) monster.getMonstersKilled() / lootStatistic.getItemsDropped();
            ItemDropBean itemDropBean = new ItemDropBean(item, dropAmount, dropPercentage);

            itemDropBeanList.add(itemDropBean);
        }

        MonsterStatisticBean monsterStatisticBean = new MonsterStatisticBean();
        monsterStatisticBean.setMonster(monster);
        monsterStatisticBean.setItemDropBeanList(itemDropBeanList);
        return monsterStatisticBean;
    }

    public Optional<MonsterStatisticBean> getLootStatisticFromMonster(String monsterName) {
        Optional<Monster> searchedMonster = monsterService.findMonsterByName(monsterName);
        if (searchedMonster.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getMonsterStatistic(searchedMonster.get()));
    }
}
