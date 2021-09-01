package com.boots.pojo;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateRequest {
    @Schema(description = "description", example = "new item")
    private String description;
    @Schema(description = "name", example = "new item")
    private String name;
    @Schema(description = "tags", example = "new item")
    private String tags;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
