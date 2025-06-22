package com.webfejl.beadando.repository;

import com.webfejl.beadando.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatusRepository extends JpaRepository<Status, String> {

}