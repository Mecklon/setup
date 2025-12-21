package com.mecklon.backend.security;

import com.mecklon.backend.model.PermissionType;
import com.mecklon.backend.model.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RolePermissionMapping {
    public static Map<RoleType, Set<PermissionType>> map = new HashMap<>();

    public static Set<SimpleGrantedAuthority> getAuthorities(RoleType role) {
        return map.get(role).stream().map(
                permission -> new SimpleGrantedAuthority(
                        permission.name())).collect(Collectors.toSet());
    }


}
