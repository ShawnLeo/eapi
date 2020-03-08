package com.shawn.sys.repository;

import com.shawn.sys.entity.RegUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegUserRepository extends JpaRepository<RegUser, String> {


    RegUser findByEmail(String email);

    RegUser findByActivateCode(String activateCode);

}
