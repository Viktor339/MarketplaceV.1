package com.boots.repository;

import com.boots.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User findUserById(Long id);

//    @Modifying
//    @Transactional
//    @Query(value = "select id from users where username =?1", nativeQuery = true)
//    Integer findUserByUsername(@Param("username") String username);

    User findAllByUsername(String username);


    @Modifying
    @Query(value = "insert into Users (email,password,username) VALUES (?1,?2,?3)", nativeQuery = true)
    @Transactional
    void addUser(@Param("email") String email,@Param("password") String password,@Param("username")String username);

    @Modifying
    @Query(value = "insert into Basket (item, user_id) VALUES (?1, ?2)", nativeQuery = true)
    @Transactional
    void addUser2(@Param("itemName") String itemName, @Param("id") Long userId);
}
