package com.example.aibouauth.core.user;


import com.example.aibouauth.core.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.aibouauth.core.token.Token;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        tokenRepository = mock(TokenRepository.class);
        usersService = new UsersService(mock(PasswordEncoder.class), userRepository, tokenRepository);
    }


    @Test
    public void shouldThrowExceptionWhenCurrentPasswordIsWrong() {

        changePasswordRequest request = changePasswordRequest.builder()
                .currentPassword("wrongPassword")
                .newPassword("newPassword")
                .confirmationPassword("newPassword")
                .build();

        User user = new User();
        user.setPassword("oldPassword");

        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())).thenReturn(true);


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            usersService.changePassword(request, principal);
        });


        assertEquals("Wrong password", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = usersService.getAllUsers();

        assertEquals(users.size(), result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnUserById() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = usersService.getUserById(1);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void shouldUpdateMontant() {

        User user = new User(1, "Nadia","Jazi", "jazinadia7@gmail.com", "28377510",0.00, BigDecimal.ZERO, Role.USER);

        user.setMontant(BigDecimal.valueOf(100));

        usersService.updateMontant(user, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), user.getMontant());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldUpdateMaxMontant() {
        double maxAmount = 1500.00;
        User user = new User(1, "Nadia","Jazi", "jazinadia7@gmail.com", "28377510",0.00, BigDecimal.ZERO, Role.USER);
        UserDto userDto = new UserDto(1, "Nadia","Jazi", "jazinadia7@gmail.com", "28377510",maxAmount, BigDecimal.ZERO, Role.USER);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(new User(1,"Nadia","Jazi", "jazinadia7@gmail.com", "28377510",maxAmount, BigDecimal.ZERO, Role.USER));

        usersService.updateMaxAmount(userDto,1);

        assertEquals(maxAmount, user.getMaxAmount());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUserById() {
        doNothing().when(tokenRepository).deleteByUserId(1);
        doNothing().when(userRepository).deleteById(1);

        usersService.deleteUserById(1);

        verify(tokenRepository, times(1)).deleteByUserId(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void shouldReturnUserIdByToken() {
        Token token = new Token();
        User user = new User();
        user.setId(1);
        token.setUser(user);
        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(token));

        Integer userId = usersService.getUserIdByToken("Bearer validToken");

        assertEquals(1, userId);
    }

    @Test
    public void shouldReturnUserMontantByToken() {
        Token token = new Token();
        User user = new User();
        user.setMontant(BigDecimal.valueOf(100));
        token.setUser(user);
        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(token));

        BigDecimal montant = usersService.getUserMontant("Bearer validToken");

        assertEquals(BigDecimal.valueOf(100), montant);
    }

    @Test
    public void shouldUpdateUserMontant() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        usersService.updateUserMontant(1, BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(200), user.getMontant());
        verify(userRepository, times(1)).save(user);
    }
}
