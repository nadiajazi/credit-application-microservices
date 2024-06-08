package com.example.aibouauth.core.user;


import com.example.aibouauth.core.purchase.Purchase;
import com.example.aibouauth.core.token.Token;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;

    private String lastName;
    private  String email;
    private  String password;
    private String phone;
    private Double maxAmount;
    @Enumerated(EnumType.STRING)
    private  Role  role;

    private BigDecimal montant = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user")
    private  List<Token>tokens;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Purchase> purchases;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role .getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }






}
