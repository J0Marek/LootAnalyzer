package com.selnix.project.service;

import com.selnix.project.entity.Item;
import com.selnix.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ItemService {

    private final ItemRepository itemRepository;

    public Item createItem(String name) {
        Item item = new Item();
        item.setName(name);
        return itemRepository.save(item);
    }

    public Optional<Item> findItemByName(String itemName) {
        return itemRepository.findByName(itemName);
    }
}
