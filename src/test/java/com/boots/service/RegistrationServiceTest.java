package com.boots.service;

import com.boots.entity.ERole;
import com.boots.entity.Role;
import com.boots.entity.User;
import com.boots.pojo.MessageResponse;
import com.boots.pojo.RegistrationRequest;
import com.boots.repository.BasketRepository;
import com.boots.repository.RoleRepository;
import com.boots.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@SpringBootTest
public class RegistrationServiceTest {
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

    private static RegistrationRequest registrationRequest = new RegistrationRequest();
    private static User user = new User();


    public static void setUp(){
        registrationRequest.setEmail("user.user@gmail.com");
        registrationRequest.setPassword("user");
        registrationRequest.setUsername("v");
        registrationRequest.setRole(ERole.ROLE_USER.name());


        user.setEmail("user.user@gmail.com");
        user.setPassword("user");
        user.setUsername("user");
        user.setId(123L);
        user.setRoles(new HashSet<Role>());
    }

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
//        SignupRequest signupRequest = new SignupRequest();
//        signupRequest.setEmail("user.user@gmail.com");
//        signupRequest.setPassword("user");
//        signupRequest.setUsername("user");
//        signupRequest.setRole(ERole.ROLE_USER.name());


        //<Optional>Role;

//        Role userRole = roleRepository
//                .findByName(ERole.ROLE_USER)
//                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));

        when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));

        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.userRepository.existsByEmail((String) any())).thenReturn(false);
        when(this.userRepository.existsByUsername((String) any())).thenReturn(false);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("foo");

        ResponseEntity<?> actualRegistrationUserResult = this.registrationService.registrationUser(registrationRequest);
        assertTrue(actualRegistrationUserResult.hasBody());
        assertTrue(actualRegistrationUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualRegistrationUserResult.getStatusCode());
        assertEquals("User CREATED", ((MessageResponse) actualRegistrationUserResult.getBody()).getMessage());
        verify(this.userRepository).existsByEmail((String) any());
        verify(this.userRepository).existsByUsername((String) any());
        verify(this.userRepository).save((User) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
    }
}

