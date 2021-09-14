package com.boot.pojo;

import com.boot.entity.Item;
import java.util.List;

public class ItemsMessage {

    private String resultMessage = "the products you bought: ";

    public ItemsMessage(List<Item> itemList) {
        itemList.forEach(item -> resultMessage += "\n" + item.getName() + item.getDescription());
    }

    @Override
    public String toString() {
        return resultMessage.replace("[", "").replace("]", "");
    }
}