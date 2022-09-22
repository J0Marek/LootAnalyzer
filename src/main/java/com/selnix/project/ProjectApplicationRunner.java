package com.selnix.project;

import com.selnix.project.service.LootService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectApplicationRunner implements CommandLineRunner {

    private final LootService lootService;

    @Override
    public void run(String... args) throws Exception {

    }
}