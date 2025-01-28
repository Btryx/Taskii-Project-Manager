package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Project;
import com.webfejl.beadando.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
