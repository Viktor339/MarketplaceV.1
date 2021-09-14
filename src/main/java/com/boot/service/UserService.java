package com.boot.service;

import com.boot.entity.User;
import com.boot.exeption.MyCustomUniqueExceptionThatCanBeCatchViaExceptionHandlerAndProperMessageToCustomerGenerated;
import com.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new MyCustomUniqueExceptionThatCanBeCatchViaExceptionHandlerAndProperMessageToCustomerGenerated(
                    "User with this name or email already exists");
        }
    }
}