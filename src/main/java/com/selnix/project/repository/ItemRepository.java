package com.selnix.project.repository;

import com.selnix.project.entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item, Integer> {
    Optional<Item> findByName(String itemName);
}