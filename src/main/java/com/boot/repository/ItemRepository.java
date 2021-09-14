package com.boot.repository;

import com.boot.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Boolean existsByTags(String tags);

    List<Item> findByTagsContainingIgnoreCase(String tags);

    List<Item> findByDescriptionContainingIgnoreCase(String description);

    Boolean existsByName(String name);

    Item findAllByName(String itemName);

    @Modifying
    @Query(value = "insert into item(name,description,tags) values (?1,?2,?3)", nativeQuery = true)
    @Transactional
    void addNewItem(@Param("name") String name, @Param("description") String description, @Param("tags") String tags);


    @Modifying
    @Query(value = "update item set name=?2,description=?3,tags=?4 where name=?1", nativeQuery = true)
    @Transactional
    void changeItem(@Param("oldName") String oldName, @Param("newName") String newName, @Param("newDescription") String newDescription, @Param("newTags") String newTags);


    @Transactional
    Long deleteByName(String name);
}
