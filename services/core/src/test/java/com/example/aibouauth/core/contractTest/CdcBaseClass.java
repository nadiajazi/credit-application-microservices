package com.example.aibouauth.core.contractTest;

import com.example.aibouauth.core.CoreApplication;
import com.example.aibouauth.core.user.*;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;


@SpringBootTest
@ContextConfiguration(classes = {UserController.class, UsersService.class, UserRepository.class})
@ActiveProfiles("test")
public class CdcBaseClass {

    @Autowired
    private UserController userController;

    @MockBean
    private UsersService usersService;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(userController);


        Mockito.when(usersService.getUserById(Mockito.eq(1)))
                .thenReturn(new User(1, "Nadia","Jazi","jazinadia@gmail.com",  "1234567890"));

        Mockito.when(usersService.getUserIdByToken(Mockito.anyString()))
                .thenReturn(1);

        Mockito.when(usersService.getUserMontant(Mockito.anyString()))
                .thenReturn(BigDecimal.valueOf(100.0));

        Mockito.doNothing().when(usersService).updateUserMontant(Mockito.eq(1), Mockito.any(BigDecimal.class));
    }
}
