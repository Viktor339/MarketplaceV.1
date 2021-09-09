package com.boot.pojo;


import io.swagger.v3.oas.annotations.media.Schema;

public class ChangeRequest {
    @Schema(description = "itemName", example = "ASUS TUF Gaming F15 FX506HC-HN011")
    private String itemName;
    @Schema(description = "newItemName", example = "Intel")
    private String newItemName;
    @Schema(description = "newItemDescription", example = "Intel")
    private String newItemDescription;
    @Schema(description = "newItemName", example = "Intel")
    private String newItemTags;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNewItemName() {
        return newItemName;
    }

    public void setNewItemName(String newItemName) {
        this.newItemName = newItemName;
    }

    public String getNewItemDescription() {
        return newItemDescription;
    }

    public void setNewItemDescription(String newItemDescription) {
        this.newItemDescription = newItemDescription;
    }

    public String getNewItemTags() {
        return newItemTags;
    }

    public void setNewItemTags(String newItemTags) {
        this.newItemTags = newItemTags;
    }
}
