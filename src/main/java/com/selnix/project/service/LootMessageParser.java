package com.selnix.project.service;

import com.selnix.project.bean.LootMessage;
import com.selnix.project.entity.Item;
import com.selnix.project.entity.Monster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LootMessageParser {

    private final ItemService itemService;
    private final MonsterService monsterService;


    public List<LootMessage> parse(String lootMessage) {
        return getLootMessageList(lootMessage).stream()
                .map(this::removeTimeStamp)
                .filter(this::isLootMessage)
                .map(this::createLootMessage)
                .toList();
    }

    private List<String> getLootMessageList(String lootMessage) {
        return List.of(lootMessage.split("\n"));
    }

    private String removeTimeStamp(String lootMessages) {
        if (Character.isDigit(lootMessages.charAt(0))) {
            return lootMessages.substring(5);
        } else {
            return lootMessages;
        }
    }

    private boolean isLootMessage(String message) {
        boolean check1 = message.contains("Loot of ");
        boolean check2 = message.contains("following items dropped by ");

        return check1 || check2;
    }

    private LootMessage createLootMessage(String messageString) {
        String[] splitLootLineNoTime = messageString.split(": ");
        String monsterLine = splitLootLineNoTime[0];
        String itemsLine = splitLootLineNoTime[1];

        Monster monster = getMonster(monsterLine);
        Map<Item, Integer> itemsWithAmount = getItemsWithDropAmountFromLootLine(itemsLine);

        return new LootMessage(monster, itemsWithAmount);
    }

    private Monster getMonster(String monsterLine) {
        String monsterName = getMonsterNameFromMonsterLine(monsterLine);

        Optional<Monster> optionalMonster = monsterService.findMonsterByName(monsterName);
        if (optionalMonster.isEmpty()) {
            return monsterService.createMonster(monsterName, 0);
        }
        return optionalMonster.get();
    }

    private String getMonsterNameFromMonsterLine(String monsterLine) {
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
        return monsterName;
    }

    private Map<Item, Integer> getItemsWithDropAmountFromLootLine(String itemsLine) {
        Map<Item, Integer> itemsWithAmounts = new HashMap<>();
        String[] itemArray = itemsLine.split(", ");
        for (String drop : itemArray) {
            Item item = getItem(drop);
            int itemAmount = getItemAmount(drop);
            itemsWithAmounts.merge(item, itemAmount, Integer::sum);
        }
        return itemsWithAmounts;
    }

    private Item getItem(String dropString) {
        String itemString = getItemNameFromDropString(dropString.trim());

        Optional<Item> itemOptional = itemService.findItemByName(itemString);
        if (itemOptional.isEmpty()) {
            return itemService.createItem(itemString);
        }
        return itemOptional.get();
    }

    private String getItemNameFromDropString(String dropString) {
        String itemString = dropString;
        if (endsWithChar(itemString, '.')) {
            itemString = removeLastCharFromString(itemString);
        }

        if (Character.isDigit(itemString.charAt(0)) || itemString.startsWith("a ") || itemString.startsWith("an ")) {
            itemString = itemString.split(" ", 2)[1];

            if (endsWithChar(itemString, 's')) {
                itemString = removeLastCharFromString(itemString);
            }
            //TODO wenn mehrzahl dann s weg, nicht bei "a " oder "an "
            //TODO Wenn ein Substantiv auf -y endet und vor dem -y ein Konsonant steht, wird das -y bei der Bildung der Pluralform in ein -ie umgewandelt. Dann wird, wie gewohnt, das Plural-s angehängt.
            //TODO https://www.studienkreis.de/englisch/substantive-unregelmaessige-pluralformen/
        }
        return itemString;
    }

    private boolean endsWithChar(String itemString, char lastChar) {
        return itemString.charAt(itemString.length() - 1) == lastChar;
    }

    private String removeLastCharFromString(String itemString) {
        return itemString.substring(0, itemString.length() - 1);
    }

    private int getItemAmount(String dropString) {
        if (Character.isDigit(dropString.charAt(0))) {
            return Integer.parseInt(dropString.split(" ", 2)[0].trim());
        } else {
            return 1;
        }
    }
}
