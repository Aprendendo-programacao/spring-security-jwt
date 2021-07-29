package me.gabreuw.springsecurityjwt;

import me.gabreuw.springsecurityjwt.domain.AppUser;
import me.gabreuw.springsecurityjwt.domain.Role;
import me.gabreuw.springsecurityjwt.repository.RoleRepository;
import me.gabreuw.springsecurityjwt.repository.UserRepository;
import me.gabreuw.springsecurityjwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(
                    null,
                    "ROLE_USER"
            ));
            userService.saveRole(new Role(
                    null,
                    "ROLE_MANAGER"
            ));
            userService.saveRole(new Role(
                    null,
                    "ROLE_ADMIN"
            ));
            userService.saveRole(new Role(
                    null,
                    "ROLE_SUPER_ADMIN"
            ));

            userService.saveUser(new AppUser(
                    null,
                    "John Travolta",
                    "john",
                    "1234",
                    new ArrayList<>()
            ));
            userService.saveUser(new AppUser(
                    null,
                    "Will Smith",
                    "will",
                    "1234",
                    new ArrayList<>()
            ));
            userService.saveUser(new AppUser(
                    null,
                    "Jim carry",
                    "jim",
                    "1234",
                    new ArrayList<>()
            ));
            userService.saveUser(new AppUser(
                    null,
                    "Arnold Schwarzenegger",
                    "arnold",
                    "1234",
                    new ArrayList<>()
            ));

            userService.addRoleToUser("john", "ROLE_USER");
            userService.addRoleToUser("john", "ROLE_ADMIN");
            userService.addRoleToUser("will", "ROLE_MANAGER");
            userService.addRoleToUser("jim", "ROLE_ADMIN");
            userService.addRoleToUser("arnold", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("arnold", "ROLE_ADMIN");
            userService.addRoleToUser("arnold", "ROLE_USER");
        };
    }

}
