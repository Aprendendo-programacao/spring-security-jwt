package me.gabreuw.springsecurityjwt.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.gabreuw.springsecurityjwt.domain.AppUser;
import me.gabreuw.springsecurityjwt.domain.Role;
import me.gabreuw.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @GetMapping(path = "/token/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token is missing.");
        }

        try {
            String refresh_token = authorizationHeader.substring("Bearer ".length());

            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(refresh_token);

            String username = decodedJWT.getSubject();
            AppUser appUser = USER_SERVICE.getUser(username);

            String access_token = JWT.create()
                    .withSubject(appUser.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1_000))
                    .withIssuer(request.getRequestURI())
                    .withClaim(
                            "roles",
                            appUser.getRoles().stream()
                                    .map(Role::getName)
                                    .collect(Collectors.toList())
                    )
                    .sign(algorithm);

            Map<String, String> tokens = new HashMap<>() {{
                put("access_token", access_token);
                put("refresh_token", refresh_token);
            }};

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        } catch (Exception e) {
            Map<String, String> tokens = new HashMap<>() {{
                put("error", e.getMessage());
            }};

            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(FORBIDDEN.value());
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }
    }

}
