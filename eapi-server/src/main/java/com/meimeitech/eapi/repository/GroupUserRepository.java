package com.meimeitech.eapi.repository;

import com.meimeitech.eapi.entity.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupUserRepository extends JpaRepository<GroupUser, String>,JpaSpecificationExecutor<GroupUser> {

    List<GroupUser> findByGroupIdOrderByJoinTime(String groupId);

    void deleteByUserIdAndGroupId(String id, String groupId);

    void deleteByGroupId(String groupId);

    GroupUser findByGroupIdAndUserIdAndRole(String groupId, String id, String roleOwner);

    GroupUser findByUserId(String transferId);

    GroupUser findByUserIdAndGroupId(String userId, String groupId);

    @Query("select g.userId from GroupUser g where g.groupId = ?1 ")
    List<String> findExistUsernames(String groupId);
}
