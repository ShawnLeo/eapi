package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository extends JpaRepository<Project, String>,JpaSpecificationExecutor<Project> {

}
