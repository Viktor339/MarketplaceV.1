package com.boots.pojo;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteRequest {
    @Schema(description = "itemName", example = "ASUS TUF Gaming F15 FX506HC-HN011")
    private String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
