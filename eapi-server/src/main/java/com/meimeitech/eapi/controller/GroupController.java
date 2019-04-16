package com.meimeitech.eapi.controller;

import com.meimeitech.common.vo.Response;
import com.meimeitech.eapi.model.GroupReq;
import com.meimeitech.eapi.model.GroupUserReq;
import com.meimeitech.eapi.model.GroupUserUpdateReq;
import com.meimeitech.sys.swagger.controller.BaseController;
import com.meimeitech.eapi.model.GroupVo;

import com.meimeitech.eapi.service.GroupService;
import com.meimeitech.sys.swagger.model.DefaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import javax.validation.Valid;

@RestController
public class GroupController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    /**
     * 添加项目组成员
     *
     * @param groupUserReq
     * @return
     */
    @RequestMapping(value = "/group/user",
            method = RequestMethod.POST)
    public Response addGroupUser(@Valid @RequestBody GroupUserReq groupUserReq) {

        logRequest("addGroupUser" , "groupUserReq", groupUserReq );

        Response response = groupService.addGroupUser(groupUserReq);

        logResponse("addGroupUser", response);

        return response;

    }

    /**
     * 删除项目组
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/group",
        method = RequestMethod.DELETE)
    public Response delete(@NotNull  @Valid @RequestParam(value = "id", required = true) String id) {

        logRequest("delete" , "id", id );

        Response response = groupService.delete(id);

        logResponse("delete", response);

        return response;

    }

    /**
     * 查询项目组
     * 
     * @return
     */
    @RequestMapping(value = "/group",
        method = RequestMethod.GET)
    public Response findById(@NotNull  @Valid @RequestParam(value = "id", required = true) String id) {

        logRequest("findById" );

        Response response = groupService.findById(id);

        logResponse("findById", response);

        return response;

    }

    /**
     * 查询用户
     * 
     * @param username
     * @return
     */
    @RequestMapping(value = "/group/user/query/{username}",
        method = RequestMethod.GET)
    public Response findByUsername( @PathVariable("username") String username, @RequestParam(value = "groupId") String groupId) {

        logRequest("findByUsername" , "username", username, "groupId", groupId );

        Response response = groupService.findByUsername(username, groupId);

        logResponse("findByUsername", response);

        return response;

    }

    /**
     * 添加项目组
     *
     * @param groupReq
     * @return
     */
    @RequestMapping(value = "/group",
            method = RequestMethod.POST)
    public Response groupAdd(@Valid @RequestBody GroupReq groupReq) {

        logRequest("groupAdd" , "groupReq", groupReq );

        Response response = groupService.groupAdd(groupReq);

        logResponse("groupAdd", response);

        return response;

    }

    /**
     * 项目组成员列表
     * 
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/group/user/list",
        method = RequestMethod.GET)
    public Response groupUserList(@NotNull  @Valid @RequestParam(value = "groupId", required = true) String groupId) {

        logRequest("groupUserList" , "groupId", groupId );

        Response response = groupService.groupUserList(groupId);

        logResponse("groupUserList", response);

        return response;

    }

    /**
     * 我的项目组列表
     * 
     * @return
     */
    @RequestMapping(value = "/group/list",
        method = RequestMethod.GET)
    public Response list() {

        logRequest("list" );

        Response response = groupService.list();

        logResponse("list", response);

        return response;

    }

    /**
     * 退出项目组
     * 
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/group/user/quit",
        method = RequestMethod.GET)
    public Response quitGroup(@NotNull  @Valid @RequestParam(value = "groupId") String groupId, @RequestParam(value = "userId", required = false) String userId) {

        logRequest("quitGroup" , "groupId", groupId, "userId", userId);

        Response response = groupService.quitGroup(groupId, userId);

        logResponse("quitGroup", response);

        return response;

    }

    /**
     * 创建者转交项目组
     * 
     * @param transferId
     * @return
     */
    @RequestMapping(value = "/group/transfer",
        method = RequestMethod.GET)
    public Response transfer(@NotNull  @Valid @RequestParam(value = "transferId") String transferId, @RequestParam(value = "groupId") String groupId) {

        logRequest("transfer" , "transferId", transferId , "groupId", groupId);

        Response response = groupService.transfer(transferId, groupId);

        logResponse("transfer", response);

        return response;

    }

    /**
     * 修改项目组
     * 
     * @param groupVo
     * @return
     */
    @RequestMapping(value = "/group",
        method = RequestMethod.PUT)
    public Response update( @Valid @RequestBody GroupVo groupVo) {

        logRequest("update" , "group", groupVo);

        Response response = groupService.update(groupVo);

        logResponse("update", response);

        return response;

    }

    /**
     * 修改项目组成员
     *
     * @param groupUserUpdateReq
     * @return
     */
    @RequestMapping(value = "/group/user/update",
            method = RequestMethod.POST)
    public Response updateGroupUser( @Valid @RequestBody GroupUserUpdateReq groupUserUpdateReq) {

        logRequest("updateGroupUser" , "groupUserUpdateReq", groupUserUpdateReq );

        Response response = groupService.updateGroupUser(groupUserUpdateReq);

        logResponse("updateGroupUser", response);

        return response;

    }


    /**
     * 当前用户在某项目组下的角色
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/group/user/role",
            method = RequestMethod.GET)
    public Response getCurrUserRole(@NotNull  @Valid @RequestParam(value = "groupId", required = true) String groupId) {

        logRequest("getCurrUserRole" , "groupId", groupId );

        Response response = groupService.getCurrUserRole(groupId);

        logResponse("getCurrUserRole", response);

        return response;

    }
}
