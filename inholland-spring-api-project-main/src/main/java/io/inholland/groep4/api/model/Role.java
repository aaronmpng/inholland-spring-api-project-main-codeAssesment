package io.inholland.groep4.api.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_EMPLOYEE;

    @Override
    public String getAuthority() {
        return name();
    }
}
