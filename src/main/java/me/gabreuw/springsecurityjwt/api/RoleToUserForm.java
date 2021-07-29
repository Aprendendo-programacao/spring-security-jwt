package me.gabreuw.springsecurityjwt.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleToUserForm implements Serializable {

    private String username;
    private String roleName;

}
