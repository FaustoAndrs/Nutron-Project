package com.lazysyntax.nutron.auth.repository;

import com.lazysyntax.nutron.auth.model.entity.UserSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSetupRepository extends JpaRepository<UserSetup, Long> {
}
