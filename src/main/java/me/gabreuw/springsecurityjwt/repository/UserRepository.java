package me.gabreuw.springsecurityjwt.repository;

import me.gabreuw.springsecurityjwt.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

}
