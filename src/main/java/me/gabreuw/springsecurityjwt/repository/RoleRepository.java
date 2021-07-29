package me.gabreuw.springsecurityjwt.repository;

import me.gabreuw.springsecurityjwt.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
