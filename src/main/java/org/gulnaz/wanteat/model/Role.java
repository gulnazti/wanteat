package org.gulnaz.wanteat.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author gulnaz
 */
public enum Role implements GrantedAuthority {
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
