package com.mecklon.backend.repo;

import com.mecklon.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String username);

    Optional<User> findByUsername(String username);


}
