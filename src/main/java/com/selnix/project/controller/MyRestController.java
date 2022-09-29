package com.selnix.project.controller;

import com.selnix.project.bean.MonsterStatisticBean;
import com.selnix.project.service.LootService;
import com.selnix.project.service.LootStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MyRestController {

    private final LootService lootService;
    private final LootStatisticService lootStatisticService;

    @PostMapping("/statistic")
    public void addNewStatisticToDatabase(@RequestBody String lootLog) {
        lootService.analyzeLoot(lootLog);
    }

    @GetMapping("/statistic/{monster}")
    public ResponseEntity<MonsterStatisticBean> getLootStatisticFromMonsters(@PathVariable String monsterName) {
        Optional<MonsterStatisticBean> monsterStatisticBean = lootStatisticService.getLootStatisticFromMonster(monsterName);
        if (monsterStatisticBean.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(monsterStatisticBean.get());
    }
}
