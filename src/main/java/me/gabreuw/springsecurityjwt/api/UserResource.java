package me.gabreuw.springsecurityjwt.api;

import lombok.RequiredArgsConstructor;
import me.gabreuw.springsecurityjwt.domain.AppUser;
import me.gabreuw.springsecurityjwt.domain.Role;
import me.gabreuw.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResource {

    private final UserService USER_SERVICE;

    @GetMapping(path = "/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        List<AppUser> users = USER_SERVICE.getUsers();

        return ResponseEntity
                .ok()
                .body(users);
    }

    @PostMapping(path = "/users")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser appUser) {
        AppUser savedAppUser = USER_SERVICE.saveUser(appUser);
        URI savedAppUserUri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users")
                .toUriString());

        return ResponseEntity
                .created(savedAppUserUri)
                .body(savedAppUser);
    }

    @PostMapping(path = "/roles")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        Role savedRole = USER_SERVICE.saveRole(role);
        URI savedRoleUri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/roles")
                .toUriString());

        return ResponseEntity
                .created(savedRoleUri)
                .body(savedRole);
    }

    @PostMapping(path = "/roles/addtouser")
    public ResponseEntity<Void> addRoleToUser(@RequestBody RoleToUserForm form) {
        USER_SERVICE.addRoleToUser(form.getUsername(), form.getRoleName());

        return ResponseEntity
                .noContent()
                .build();
    }

}
