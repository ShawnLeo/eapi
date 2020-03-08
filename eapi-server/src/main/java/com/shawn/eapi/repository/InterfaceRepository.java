package com.shawn.eapi.repository;

import com.shawn.eapi.entity.Interface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterfaceRepository extends JpaRepository<Interface, String>,JpaSpecificationExecutor<Interface> {

    List<Interface> findAllByProjectIdOrderByPath(String projectId);

    @Modifying
    @Query("update  Interface i set i.status = ?2  where i.id = ?1 ")
    void changeStatus(String id, short status);
}
