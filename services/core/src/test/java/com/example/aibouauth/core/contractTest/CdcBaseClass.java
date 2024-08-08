package com.example.aibouauth.core.contractTest;


import com.example.aibouauth.core.CoreApplication;
import com.example.aibouauth.core.user.Role;
import com.example.aibouauth.core.user.User;
import com.example.aibouauth.core.user.UserController;
import com.example.aibouauth.core.user.UsersService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest(classes = CoreApplication.class)
public class CdcBaseClass {

    @Autowired
    UserController userController;

    @MockBean
    UsersService usersService;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(userController);


        User mockUser = new User(1, "Nadia", "Jazi", "jazinadia@gmail.com", "password", "1234567890", 1000.0, BigDecimal.valueOf(100.00), Role.USER);
        Mockito.when(usersService.getUserById(Mockito.eq(1)))
                .thenReturn(mockUser);

        Mockito.when(usersService.getUserIdByToken(Mockito.anyString()))
                .thenReturn(1);

        Mockito.when(usersService.getUserMontant(Mockito.anyString()))
                .thenReturn(BigDecimal.valueOf(100.00));

        Mockito.doNothing().when(usersService).updateUserMontant(Mockito.eq(1), Mockito.any(BigDecimal.class));
    }
}
