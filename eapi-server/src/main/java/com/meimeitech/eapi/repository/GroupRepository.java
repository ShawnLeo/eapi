package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String>,JpaSpecificationExecutor<Group> {

    @Query("select g from Group g where g.id in ( select gu.groupId from GroupUser gu where gu.userId = ?1) order by g.createTime ")
    List<Group> findAllByUserIdOrderByCreateTime(String id);
}
