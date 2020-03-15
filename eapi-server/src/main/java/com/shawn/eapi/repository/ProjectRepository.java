package com.shawn.eapi.repository;

import com.shawn.eapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String>,JpaSpecificationExecutor<Project> {

    @Query("select p from Project p")
    List<Project> findAllGroups();

    List<Project> findByGroupId(String groupId);
}
