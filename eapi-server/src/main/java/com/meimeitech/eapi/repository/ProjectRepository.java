package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String>,JpaSpecificationExecutor<Project> {

    @Query("select p from Project p where p.groupId in ( select gu.groupId from GroupUser gu where gu.userId = ?1) ")
    List<Project> findAllGroups(String userId);

    List<Project> findByGroupId(String groupId);
}
