package com.boots.repository;

import com.boots.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Modifying
    @Query(value = "insert into Basket (item, user_id) VALUES (?1, ?2)", nativeQuery = true)
    @Transactional
    void insertItem(@Param("itemName") String itemName, @Param("id") Long userId);

    Boolean existsByItemAndUserId(String name,Long id);

    Boolean existsByItem(String itemName);

    List<Basket> findItemByUserId(Long id);

    @Transactional
    void deleteAllByUserId(Long id);

//    @Modifying
//    @Transactional
//    @Query(value = "select user_id from basket where item =?1", nativeQuery = true)
//    Integer getUserIdByItem(@Param("item") String item);

    Basket findBasketByItem(String item);

    @Modifying
    @Query(value = "update basket set item=?2 where item=?1", nativeQuery = true)
    @Transactional
    void forceUpdateItem(@Param("oldItemName") String oldItemName, @Param("newName") String newItemName);


    @Transactional
    Long deleteByItem(String itemName);


}
