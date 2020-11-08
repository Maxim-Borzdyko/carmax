package by.bntu.borzdyko.carmax.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {

    USER(Set.of(
            Permission.CARS_READ,
            Permission.USERS_UPDATE,
            Permission.ORDERS_WRITE)
    ),
    ADMIN(Set.of(
            Permission.CARS_READ, Permission.CARS_WRITE, Permission.CARS_DELETE,
            Permission.USERS_READ, Permission.USERS_UPDATE, Permission.USERS_DELETE,
            Permission.ORDERS_READ, Permission.ORDERS_WRITE, Permission.ORDERS_DELETE)
    );

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
