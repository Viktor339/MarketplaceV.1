package com.boot.pojo;

import io.swagger.v3.oas.annotations.media.Schema;

public class SearchRequest {
    @Schema(description = "tags", example = "[\"intel\"]")
    private String[] tags;
    @Schema(description = "description", example = "[\"видеокарта\"]")
    private String description;

    public SearchRequest(){

    }

    public SearchRequest(String[] tags,String description){
        this.tags = tags;
        this.description = description;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTags() {
        return tags;
    }


    public String getDescription() {
        return description;
    }

}
