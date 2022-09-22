package com.selnix.project.repository;

import com.selnix.project.entity.Monster;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MonsterRepository extends CrudRepository<Monster, Integer> {
    Optional<Monster> findByName(String monsterName);
}