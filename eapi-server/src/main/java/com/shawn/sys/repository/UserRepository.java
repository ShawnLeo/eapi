package com.shawn.sys.repository;

import com.shawn.sys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{


}
