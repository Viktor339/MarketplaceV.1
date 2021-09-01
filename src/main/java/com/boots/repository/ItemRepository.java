package com.boots.repository;

import com.boots.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Boolean existsByTags(String tags);

    List<Item> findByTagsContaining(String director);

    List<Item> findByDescriptionContaining(String description);

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


//    @Modifying
//    @Query(value = "select tags from item where name=?1",nativeQuery = true)
//    @Transactional
//    String getTags(@Param("name") String name);
//
//
//
//    @Modifying
//    @Query(value = "select description from item where name=?1",nativeQuery = true)
//    @Transactional
//    String getDescription(@Param("name") String name);

    @Transactional
    Long deleteByName(String name);
}
