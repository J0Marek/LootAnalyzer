package com.selnix.project.service;

import com.selnix.project.entity.Item;
import com.selnix.project.entity.Monster;
import com.selnix.project.repository.ItemRepository;
import com.selnix.project.repository.MonsterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LootService {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final MonsterService monsterService;
    private final MonsterRepository monsterRepository;
    private final LootStatisticService lootStatisticService;

    public void analyzeLoot(String lootMessage) {
        for (String lootLine : getLootMessageList(lootMessage)) {
            String lootLineNoTime = removeTimeStamp(lootLine);
            if (isLootMessage(lootLineNoTime)) {
                String[] splitLootLineNoTime = lootLineNoTime.split(": ");
                String monsterLine = splitLootLineNoTime[0];
                String itemsLine = splitLootLineNoTime[1];

                Monster monster = getMonster(monsterLine);
                Map<Item, Integer> items = getItemsFromLootLine(itemsLine);

                for (Map.Entry<Item, Integer> entry : items.entrySet()) {
                    lootStatisticService.createLootStatistic(monster, entry.getKey(), entry.getValue());
                }
                monster.setMonstersKilled(monster.getMonstersKilled() + 1);
                monsterRepository.save(monster);
            }
        }
    }

    private String[] getLootMessageList(String lootMessage) {
        return lootMessage.split("\n");
    }

    private boolean isLootMessage(String message) {
        boolean check1 = message.contains("Loot of ");
        boolean check2 = message.contains("following items dropped by ");

        return check1 || check2;
    }

    private Monster getMonster(String monsterLine) {
        String monsterName = "";

        if (monsterLine.contains("Loot of a ")) {
            monsterName = monsterLine.split("Loot of a ")[1];

        } else if (monsterLine.contains("Loot of ")) {
            monsterName = monsterLine.split("Loot of ")[1];

        } else if (monsterLine.contains("following items dropped by ")) {
            String monsterNameNoPrefix = monsterLine.split("dropped by ")[1];
            monsterName = monsterNameNoPrefix.split(" are available in your reward chest")[0];
        }

        monsterName = monsterName.substring(0, 1).toUpperCase() + monsterName.substring(1);

        Optional<Monster> optionalMonster = monsterRepository.findByName(monsterName);
        if (optionalMonster.isEmpty()) {
            return monsterService.createMonster(monsterName, 0);
        }
        return optionalMonster.get();
    }

    private String removeTimeStamp(String lootMessages) {
        if (Character.isDigit(lootMessages.charAt(0))) {
            return lootMessages.substring(5);
        } else {
            return lootMessages;
        }
    }

    private int getItemAmount(String dropString) {
        if (Character.isDigit(dropString.charAt(0))) {
            return Integer.parseInt(dropString.split(" ", 2)[0].trim());
        } else {
            return 1;
        }
    }

    private Item getItem(String dropString) {
        String itemString = dropString.trim();

        boolean endsWithDot = itemString.charAt(itemString.length() - 1) == '.';
        if (endsWithDot) {
            itemString = itemString.substring(0, itemString.length() - 1);
        }

        if (Character.isDigit(itemString.charAt(0)) || itemString.startsWith("a ") || itemString.startsWith("an ")) {
            itemString = itemString.split(" ", 2)[1];

            boolean endsWithS = itemString.charAt(itemString.length() - 1) == 's';
            if (endsWithS) {
                itemString = itemString.substring(0, itemString.length() - 1);
            }
            //TODO wenn mehrzahl dann s weg, nicht bei "a " oder "an "
            //TODO Wenn ein Substantiv auf -y endet und vor dem -y ein Konsonant steht, wird das -y bei der Bildung der Pluralform in ein -ie umgewandelt. Dann wird, wie gewohnt, das Plural-s angehängt.
            //TODO https://www.studienkreis.de/englisch/substantive-unregelmaessige-pluralformen/
        }

        Optional<Item> itemOptional = itemRepository.findByName(itemString);
        if (itemOptional.isEmpty()) {
            return itemService.createItem(itemString);
        }
        return itemOptional.get();
    }

    private Map<Item, Integer> getItemsFromLootLine(String itemsLine) {
        Map<Item, Integer> lootList = new HashMap<Item, Integer>();
        String[] itemArray = itemsLine.split(", ");
        for (String drop : itemArray) {
            Item item = getItem(drop);
            int itemAmount = getItemAmount(drop);

            if (lootList.containsKey(item)) {
                lootList.replace(item, lootList.get(item), lootList.get(item) + itemAmount);
            } else {
                lootList.put(getItem(drop), getItemAmount(drop));
            }
        }
        return lootList;
    }
}
   