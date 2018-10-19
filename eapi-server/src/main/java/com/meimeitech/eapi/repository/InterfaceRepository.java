package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.Interface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InterfaceRepository extends JpaRepository<Interface, String>,JpaSpecificationExecutor<Interface> {

    List<Interface> findAllByProjectIdOrderByPath(String projectId);

}
