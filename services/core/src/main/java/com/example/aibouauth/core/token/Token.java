package com.example.aibouauth.core.token;


import com.example.aibouauth.core.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor



@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String token;

    @Enumerated(EnumType.STRING)
    private  TokenType tokenType;

private boolean expired;
private  boolean revoked;


@ManyToOne
@JoinColumn(name = "user_id")
private User user;
}
