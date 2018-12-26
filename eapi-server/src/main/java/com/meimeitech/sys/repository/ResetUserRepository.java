package com.meimeitech.sys.repository;

import com.meimeitech.sys.entity.ResetUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetUserRepository extends JpaRepository<ResetUser, String> {

    ResetUser findByEmail(String email);

    ResetUser findByResetCode(String resetCode);
}
