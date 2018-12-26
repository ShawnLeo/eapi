package com.meimeitech.sys.repository;

import com.meimeitech.sys.entity.RegUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegUserRepository extends JpaRepository<RegUser, String> {


    RegUser findByEmail(String email);

    RegUser findByActivateCode(String activateCode);

}
