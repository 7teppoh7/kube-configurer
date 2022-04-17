package ru.sstu.hobbyboard.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MODERATOR,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }

}
