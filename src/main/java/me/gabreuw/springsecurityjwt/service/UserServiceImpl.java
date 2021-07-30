package me.gabreuw.springsecurityjwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gabreuw.springsecurityjwt.domain.AppUser;
import me.gabreuw.springsecurityjwt.domain.Role;
import me.gabreuw.springsecurityjwt.repository.RoleRepository;
import me.gabreuw.springsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository USER_REPOSITORY;
    private final RoleRepository ROLE_REPOSITORY;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = USER_REPOSITORY.findByUsername(username);

        if (appUser == null) {
            log.error("User not found in the database!");
            throw new UsernameNotFoundException("User not found in the database!");
        }

        log.info("User found in the database: {}", appUser);

        List<SimpleGrantedAuthority> authorities = appUser.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                authorities
        );
    }

    @Override
    public AppUser saveUser(AppUser appUser) {
        log.info("Saving new user {} to the database.", appUser.getName());

        return USER_REPOSITORY.save(appUser);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database.", role.getName());

        return ROLE_REPOSITORY.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);

        AppUser appUser = USER_REPOSITORY.findByUsername(username);
        Role role = ROLE_REPOSITORY.findByName(roleName);

        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("Fetching user {}", username);

        return USER_REPOSITORY.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users");

        return USER_REPOSITORY.findAll();
    }
}
