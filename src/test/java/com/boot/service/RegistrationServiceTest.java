package com.boot.service;


import com.boot.entity.Role;
import com.boot.entity.User;
import com.boot.pojo.MessageResponse;
import com.boot.pojo.RegistrationRequest;
import com.boot.repository.BasketRepository;
import com.boot.repository.RoleRepository;
import com.boot.repository.UserRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.HashSet;
import java.util.Optional;

import static com.boot.entity.Role.Name.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest
public class RegistrationServiceTest {

    private static User user = new User();
    private static final RegistrationRequest registrationRequest = new RegistrationRequest();

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRegistrationUser_ShouldReturnHttpStatusBadRequest() {
        when(this.userRepository.existsByUsername((String) any())).thenReturn(true);

        ResponseEntity<?> actualRegistrationUserResult = this.registrationService.registrationUser(registrationRequest);
        assertTrue(actualRegistrationUserResult.hasBody());
        assertTrue(actualRegistrationUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualRegistrationUserResult.getStatusCode());
        assertEquals("Error: Username is exist", ((MessageResponse) actualRegistrationUserResult.getBody()).getMessage());
        verify(this.userRepository).existsByUsername((String) any());
    }

    @Test
    public void testRegistrationUser_ShouldReturnHttpStatusBadRequest_EmailIsExist() {

        when(this.userRepository.existsByEmail((String) any())).thenReturn(true);
        when(this.userRepository.existsByUsername((String) any())).thenReturn(false);

        ResponseEntity<?> actualRegistrationUserResult = this.registrationService.registrationUser(registrationRequest);
        assertTrue(actualRegistrationUserResult.hasBody());
        assertTrue(actualRegistrationUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualRegistrationUserResult.getStatusCode());
        assertEquals("Error: Email is exist", ((MessageResponse) actualRegistrationUserResult.getBody()).getMessage());
        verify(this.userRepository).existsByEmail((String) any());
        verify(this.userRepository).existsByUsername((String) any());
    }

    @Test
    public void testRegistrationUser_ShouldReturnHttpStatusOK() {

        registrationRequest.setRole(ROLE_USER.name());

        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(ROLE_USER)));
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.userRepository.existsByEmail((String) any())).thenReturn(false);
        when(this.userRepository.existsByUsername((String) any())).thenReturn(false);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("foo");

        ResponseEntity<?> actualRegistrationUserResult = this.registrationService.registrationUser(registrationRequest);
        assertTrue(actualRegistrationUserResult.hasBody());
        assertTrue(actualRegistrationUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualRegistrationUserResult.getStatusCode());
        verify(this.userRepository).existsByEmail((String) any());
        verify(this.userRepository).existsByUsername((String) any());
        verify(this.userRepository).save((User) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
    }
}

