package me.gabreuw.springsecurityjwt.service;

import me.gabreuw.springsecurityjwt.domain.AppUser;
import me.gabreuw.springsecurityjwt.domain.Role;

import java.util.List;

public interface UserService {

    AppUser saveUser(AppUser appUser);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    AppUser getUser(String username);

    List<AppUser> getUsers();

}
