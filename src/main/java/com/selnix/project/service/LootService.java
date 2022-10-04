package com.selnix.project.service;

import com.selnix.project.bean.LootMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LootService {

    private final LootMessageParser lootMessageParser;
    private final MonsterService monsterService;
    private final LootStatisticService lootStatisticService;

    public void analyzeLoot(String lootMessage) {
        List<LootMessage> lootMessages = lootMessageParser.parse(lootMessage);

        lootMessages.forEach(lootStatisticService::createLootStatisticsFromLootMessage);
        lootMessages.forEach(monsterService::increaseMonsterKillCount);
    }
}
   