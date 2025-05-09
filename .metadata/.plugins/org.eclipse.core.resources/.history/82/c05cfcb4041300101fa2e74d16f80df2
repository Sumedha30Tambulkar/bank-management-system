package com.role.controller;

import com.role.model.Role;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private static final Map<Long, Role> roles = new HashMap<>();

    static {
        roles.put(1L, new Role(1L, "ADMIN"));
        roles.put(2L, new Role(2L, "USER"));
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roles.get(id);
    }

    @GetMapping
    public Collection<Role> getAllRoles() {
        return roles.values();
    }
}
