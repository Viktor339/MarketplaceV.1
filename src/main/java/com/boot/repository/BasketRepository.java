package com.boot.repository;

import com.boot.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Modifying
    @Query(value = "insert into Basket (item_id, user_id) VALUES (?1, ?2)", nativeQuery = true)
    @Transactional
    void insertItem(@Param("item") Long item, @Param("userId") Long userId);


    Boolean existsByItemIdAndUserId(Long itemId,Long userId);

    Boolean existsByItem(String itemName);

    List<Basket> findItemByUserId(Long id);

    @Transactional
    void deleteAllByUserId(Long id);

    Basket findBasketByItem(String item);

    @Modifying
    @Query(value = "update basket set item=?2 where item=?1", nativeQuery = true)
    @Transactional
    void forceUpdateItem(@Param("oldItemName") String oldItemName, @Param("newName") String newItemName);


    @Transactional
    Long deleteByItem(String itemName);


    @Modifying
    @Transactional
    @Query(value = "select item_id from Basket join item on id=basket_id where item_id=?1 and user_id=?2",nativeQuery = true)
    Integer searchItemByUserId(@Param("itemId")Long itemId,@Param("userId")Long userId);

    @Modifying
    @Transactional
    @Query(value = "select item_id from basket_items where item_id=?1 and user_id=?2",nativeQuery = true)
    Integer searchItemByUserId2(@Param("itemId")Long itemId,@Param("userId")Long userId);

}
