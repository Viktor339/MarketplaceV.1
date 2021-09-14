package com.boot.service;

import com.boot.entity.Role;
import com.boot.pojo.RegistrationRequest;
import com.boot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getRole(RegistrationRequest registrationRequest) {
        if ("admin".equals(registrationRequest.getRole())) {
            return roleRepository.findByName(Role.Name.ROLE_ADMIN);
        } else return roleRepository.findByName(Role.Name.ROLE_USER);
    }
}
