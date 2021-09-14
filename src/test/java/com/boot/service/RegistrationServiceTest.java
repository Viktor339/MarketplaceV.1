package com.boot.service;


import com.boot.entity.Role;
import com.boot.entity.User;
import com.boot.pojo.RegistrationRequest;
import com.boot.repository.BasketRepository;
import com.boot.repository.RoleRepository;
import com.boot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserService userService;


    @Test
    public void testRegistrationUser_ShouldReturnHttpStatusOK() {

        when(roleService.getRole((RegistrationRequest) any())).thenReturn(new Role(ROLE_USER));
        when(userService.save(any())).thenReturn(user);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("foo");

        ResponseEntity<?> actualRegistrationUserResult = this.registrationService.registrationUser(registrationRequest);
        assertTrue(actualRegistrationUserResult.hasBody());
        assertTrue(actualRegistrationUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualRegistrationUserResult.getStatusCode());
        verify(this.passwordEncoder).encode((CharSequence) any());
    }
}

