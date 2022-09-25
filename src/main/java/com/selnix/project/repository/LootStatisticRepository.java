package com.selnix.project.repository;

import com.selnix.project.entity.Item;
import com.selnix.project.entity.LootStatistic;
import com.selnix.project.entity.Monster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LootStatisticRepository extends CrudRepository<LootStatistic, Integer> {
    @Query("SELECT s FROM LootStatistic s WHERE s.monster = :monster AND s.item = :item")
    Optional<LootStatistic> findByNameAndItem(@Param("monster") Monster monster, @Param("item") Item item);

    @Query("SELECT statistic FROM LootStatistic statistic WHERE statistic.monster = :monster")
    List<LootStatistic> getLootStatisticsOfMonster(@Param("monster") Monster monster);
}