package com.boot.pojo;

import com.boot.entity.Item;
import java.util.List;

public class ItemInfoMessage {

    private String resultMessage = "the products you bought: ";

    public ItemInfoMessage(List<Item> itemList) {
        itemList.forEach(item -> resultMessage += "\n" + item.getName() + item.getDescription());
    }

    @Override
    public String toString() {
        return resultMessage.replace("[", "").replace("]", "");
    }
}