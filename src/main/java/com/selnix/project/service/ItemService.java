package com.selnix.project.service;

import com.selnix.project.entity.Item;
import com.selnix.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ItemService {

    private final ItemRepository itemRepository;

    public Item createItem(String name) {
        Item item = new Item();
        item.setName(name);
        return itemRepository.save(item);
    }
}
