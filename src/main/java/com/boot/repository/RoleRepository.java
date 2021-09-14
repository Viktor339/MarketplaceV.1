package com.boot.repository;

import com.boot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByName(Role.Name name);
}