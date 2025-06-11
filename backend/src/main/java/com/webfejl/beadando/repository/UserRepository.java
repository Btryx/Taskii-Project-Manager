package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String userName);
}
