package com.example.aletheia.pretest.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.aletheia.pretest.models.enums.Rol;

import lombok.Getter;

@Getter
public class CustomUserDetails extends User {

    private final Integer id;
    private final String nombres;
    private final Rol rol;

    public CustomUserDetails(Integer id, String username, String password,
                              String nombres, Rol rol,
                              Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.nombres = nombres;
        this.rol = rol;
    }
}