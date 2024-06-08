package com.example.aibouauth.core.user;

import com.example.aibouauth.core.token.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor

public class UsersService {

    private final PasswordEncoder passwordEncoder;
    private  final UserRepository repository;
    private final TokenRepository tokenRepository;

    public void changePassword(changePasswordRequest request, Principal connectedUser) {

        var user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());

        //check if the current password is correct
        if (passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        //check if the two new password are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {

            throw new IllegalStateException("Password are not the same ");
        }
        // update the password
        user.setPassword(passwordEncoder.encode((request.getNewPassword())));
        //save the new password
        repository.save(user);
    }



    public List<User> getAllUsers(){
        return  repository.findAll();
    }

    public User getUserById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void updateMontant(User user, BigDecimal amount) {
        BigDecimal currentMontant = user.getMontant();

        if (currentMontant == null) {
            currentMontant = BigDecimal.ZERO;
        }

        user.setMontant(currentMontant.add(amount));
        repository.save(user);
    }


    public User updateMaxAmount(UserDto newUser, Integer id) {
        return repository.findById(id)
                .map(user->{
                    user.setMaxAmount(newUser.getMaxAmount());
                    return repository.save(user);
                })
                .orElseThrow(()->new UserNotFoundException(id));
    }


    @Transactional
    public void deleteUserById(Integer id) {

        tokenRepository.deleteByUserId(id);


        // Then delete the user
        repository.deleteById(id);
    }

    public Integer getUserIdByToken(String authHeader) {

        String token = authHeader.substring(7);

        return tokenRepository.findByToken(token)
                .orElseThrow()
                .getUser()
                .getId();

    }

    public BigDecimal getUserMontant(String authHeader) {
        String token = authHeader.substring(7);

        return tokenRepository.findByToken(token)
                .orElseThrow()
                .getUser()
                .getMontant();
    }

    @Transactional
    public void updateUserMontant(Integer userId, BigDecimal newMontant) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setMontant(newMontant);
        repository.save(user);
    }
}

