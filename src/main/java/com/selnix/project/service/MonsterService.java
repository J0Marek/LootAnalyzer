package com.selnix.project.service;

import com.selnix.project.entity.Monster;
import com.selnix.project.repository.MonsterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
