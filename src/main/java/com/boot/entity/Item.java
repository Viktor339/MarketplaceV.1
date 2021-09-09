package com.boot.entity;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")})
public class Item {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    private String description;
    @Column(name = "tags")
    private String tags;


    public Item() {
    }

    public Item(Long id,String name, String description, String tags) {
        this.id=id;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id.equals(item.id) && getName().equals(item.getName()) && getDescription().equals(item.getDescription()) && getTags().equals(item.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getDescription(), getTags());
    }
}
