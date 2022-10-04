package com.selnix.project.service;

import com.selnix.project.bean.LootMessage;
import com.selnix.project.entity.Monster;
import com.selnix.project.repository.MonsterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final MonsterRepository monsterRepository;

    public Monster createMonster(String name, int kills) {
        Monster monster = new Monster();
        monster.setName(name);
        monster.setMonstersKilled(kills);
        return monsterRepository.save(monster);
    }

    public Optional<Monster> findMonsterByName(String monsterName) {
        return monsterRepository.findByName(monsterName);
    }

    public void increaseMonsterKillCount(LootMessage lootMessage) {
        Monster monster = lootMessage.getMonster();
        monster.setMonstersKilled(monster.getMonstersKilled() + 1);
        monsterRepository.save(monster);
    }
}
