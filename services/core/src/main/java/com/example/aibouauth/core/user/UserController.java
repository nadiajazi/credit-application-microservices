package com.example.aibouauth.core.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UsersService service;


    @PatchMapping
    public ResponseEntity<?> changePassword(
       @RequestBody changePasswordRequest request,
       Principal connectedUser
    ){
        service.changePassword(request,connectedUser);
        return  ResponseEntity.ok().build();
    }

    @PutMapping("/user/maxAmount/{id}")
    public ResponseEntity<UserDto> updateMaxAmount(@RequestBody UserDto updatedUserData, @PathVariable Integer id) {
        return Optional.ofNullable(service.getUserById(id))
                .map(existingUser -> {
                    service.updateMaxAmount(updatedUserData, id);
                    return ResponseEntity.ok(UserDto.fromEntity(existingUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/byToken")
    public ResponseEntity<Integer> findUserIdByToken(
            @RequestHeader("Authorization") String authHeader
    ){
        return ResponseEntity.ok(service.getUserIdByToken(authHeader));
    }

    @GetMapping("/user/montant")
    public ResponseEntity<BigDecimal> getUserMontant(
            @RequestHeader("Authorization") String authHeader
    ){
        return ResponseEntity.ok(service.getUserMontant(authHeader));
    }


    @GetMapping("/user/maxAmount/{id}")
    public ResponseEntity<Double> getMaxAmount(@PathVariable Integer id) {
        Optional<User> userOptional = Optional.ofNullable(service.getUserById(id));

        return userOptional.map(user -> ResponseEntity.ok(user.getMaxAmount()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/montant/{id}")
    public ResponseEntity<BigDecimal> getMontant(@PathVariable Integer id) {
        Optional<User> userOptional = Optional.ofNullable(service.getUserById(id));

        return userOptional.map(user -> ResponseEntity.ok(user.getMontant()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/updateMontant")
    public ResponseEntity<Void> updateUserMontant(@RequestHeader("Authorization") String token, @RequestBody UpdateMontantRequest request) {
        Integer userId = service.getUserIdByToken(token);
        service.updateUserMontant(userId, request.newMontant());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CustomerResponse> getUserById(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer id) {
        User user = service.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(CustomerResponse.fromEntity(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
