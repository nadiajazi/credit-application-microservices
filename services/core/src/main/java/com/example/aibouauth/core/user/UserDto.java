package com.example.aibouauth.core.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String firstName;
    @Getter
    private String lastName;
    private String email;
    private String tel;
    private Double maxAmount;
    private BigDecimal montant = BigDecimal.ZERO;
    private Role role;



    // Convert User entity to UserDTO
    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getMaxAmount(),
                user.getMontant(),
                user.getRole()
        );

    }

    // Convert a list of User entities to a list of UserDTOs
    public static List<UserDto> fromEntityList(List<User> users) {
        return users.stream().map(UserDto::fromEntity).toList();
    }
}
