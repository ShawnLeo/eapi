package com.shawn.eapi.service;

import com.google.common.collect.Lists;
import com.shawn.common.RetCode;
import com.shawn.common.util.UserContextHolder;
import com.shawn.common.vo.Response;
import com.shawn.common.vo.UserSession;
import com.shawn.eapi.entity.Group;
import com.shawn.eapi.entity.GroupUser;
import com.shawn.eapi.entity.Project;
import com.shawn.eapi.model.*;
import com.shawn.eapi.repository.GroupRepository;
import com.shawn.eapi.repository.GroupUserRepository;
import com.shawn.sys.entity.UserAuth;
import com.shawn.sys.service.UserAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class GroupService {



    private static final String ROLE_OWNER = "1";
    private static final String ROLE_MANAGER = "2";
    private static final String ROLE_DEVELOPER = "3";
    private static final String ROLE_OBSERVER = "4";

    private static final String DEFAULT_VERIFY = "1"; // 默认需要验证

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private UserAuthService userAuthService;

    public Response addGroupUser(GroupUserReq groupUserReq ) {

        List<GroupUser> groupUsers = Lists.newArrayList();

        groupUserReq.getUsers().forEach(userName -> {
            UserAuth userAuth = userAuthService.findByAuthId(userName);
            GroupUser groupUser = new GroupUser();
            groupUser.setUserName(userName);
            groupUser.setUserId(userAuth.getUser().getId().toString());
            groupUser.setGroupId(groupUserReq.getGroupId());
            groupUser.setJoinTime(new Date());
            groupUser.setRole(groupUserReq.getRole());

            groupUsers.add(groupUser);
        });

        groupUserRepository.saveAll(groupUsers);

        return Response.success("success");
    }

    @Autowired
    private ProjectService projectService;


    @Transactional
    public Response delete(String id) {

        groupRepository.deleteById(id);

        groupUserRepository.deleteByGroupId(id);

        List<Project> projects = (List<Project>) projectService.list(id).getBody();

        projects.forEach(project -> projectService.delete(project.getId()));

        return Response.success("success");
    }

    public Response findById(String id) {

       Group group = groupRepository.findById(id).get();

        return Response.success(group);
    }

    public Response findByUsername(String username, String groupId) {

        List<GroupUser> groupUsers = Lists.newArrayList();
        List<String> groupUserIds = groupUserRepository.findExistUsernames(groupId);
        // 搜索用户
        List<UserAuth> userAuths  = userAuthService.findByUsername(username, groupUserIds);

        userAuths.forEach(userAuth ->{
            GroupUser groupUser = new GroupUser();
            groupUser.setUserName(userAuth.getAuthId());
            groupUser.setUserId(userAuth.getUser().getId().toString());

            groupUsers.add(groupUser);
        });

        return Response.success(groupUsers);
    }

    public Response groupAdd(GroupReq groupReq) {
        UserSession user = UserContextHolder.getContext();

        Group group = new Group();
        group.setName(groupReq.getName());
        group.setDescription(groupReq.getDescription());
        group.setVerify(DEFAULT_VERIFY);
//        group.setVerifyRole(ROLE_DEVELOPER); // 默认自动通过为开发者
        group.setCreateTime(new Date());
        group.setCreater(user.getId().toString());
        group.setCreaterUserName(user.getLoginName());

        group = groupRepository.save(group);

        // 添加当前用户为创建者
        GroupUser owner = new GroupUser();
        owner.setUserId(user.getId().toString());
        owner.setUserName(user.getLoginName());
        owner.setRole(ROLE_OWNER);
        owner.setGroupId(group.getId());
        owner.setJoinTime(new Date());

        groupUserRepository.save(owner);

        return Response.success(group);
    }

    public Response groupUserList(String groupId) {
        return Response.success(groupUserRepository.findByGroupIdOrderByJoinTime(groupId));
    }

    public Response list() {
        UserSession user = UserContextHolder.getContext();

        List<Group> groups = groupRepository.findAllByUserIdOrderByCreateTime(user.getId().toString());

        return Response.success(groups);
    }

    @Transactional
    public Response quitGroup(String groupId, String userId) {
        UserSession user = UserContextHolder.getContext();

        String deleteUserId = user.getId().toString(); // 自己退出

        if (!StringUtils.isEmpty(userId)) {
            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(user.getId().toString(), groupId);
            if (!(groupUser.getRole().equals(ROLE_MANAGER) || groupUser.getRole().equals(ROLE_OWNER))) {
                return Response.response(RetCode.NOPERMISSION);
            }
            // 管理员删除
            deleteUserId = userId;
        }
        groupUserRepository.deleteByUserIdAndGroupId(deleteUserId, groupId);

        return Response.success("success");
    }

    @Transactional
    public Response transfer(String transferId, String groupId) {

        UserSession user = UserContextHolder.getContext();

        // 项目所有者 和  移交者
        GroupUser owner = groupUserRepository.findByGroupIdAndUserIdAndRole(groupId, user.getId().toString(), ROLE_OWNER);
        GroupUser transfer = groupUserRepository.findByUserIdAndGroupId(transferId, groupId);
        //  更新项目所有者 为transferId
        transfer.setRole(ROLE_OWNER);
        // 更新前项目所有者为观察者
        owner.setRole(ROLE_OBSERVER);

        groupUserRepository.save(owner);
        groupUserRepository.save(transfer);

        return Response.success("success");
    }

    public Response update(GroupVo groupVo) {

        Group group = groupRepository.getOne(groupVo.getId());

        if (group == null) return Response.success(RetCode.VALIDATEERROR);

        BeanUtils.copyProperties(groupVo, group);

        return Response.success(groupRepository.save(group));
    }

    public Response updateGroupUser(GroupUserUpdateReq groupUserUpdateReq ) {

        GroupUser groupUser = groupUserRepository.findById(groupUserUpdateReq.getGroupUserId()).get();

        if (groupUser == null) return Response.success(RetCode.VALIDATEERROR);

        groupUser.setRole(groupUserUpdateReq.getRole());
        groupUserRepository.save(groupUser);

        return Response.success("success");
    }

    public Response getCurrUserRole(String groupId) {
        UserSession user = UserContextHolder.getContext();
        GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(user.getId().toString(), groupId);
        return Response.success(groupUser.getRole());
    }
}
