package com.mecklon.backend.model;

import com.mecklon.backend.security.RolePermissionMapping;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<RoleType> roles = new HashSet<>();

    //@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.stream().map(role ->
//                new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet());

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(
                role->{
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
                    authorities.addAll(RolePermissionMapping.getAuthorities(
                            role
                    ));
                }
        );
        return authorities;
    }
}