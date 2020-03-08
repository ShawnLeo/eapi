package com.shawn.eapi.controller;

import com.shawn.common.vo.Response;
import com.shawn.eapi.entity.Tag;
import com.shawn.eapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 更新标签
     *
     * @param tag
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response update(@RequestBody Tag tag) {
        return Response.success("SUCCESS");
    }

    /**
     * 根据ID获取标签
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response get(@PathVariable String id) {
        return Response.success("SUCCESS");
    }

    /**
     * 获取标签列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Response list(String projectId) {
        return tagService.list(projectId);
    }

    /**
     * 添加新标签
     *
     * @param tag
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response create(@RequestBody Tag tag) {
        return tagService.create(tag);
    }

    /**
     * 检查标签名称是否已存在
     *
     * @param tag
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Response checkExists(@RequestBody Tag tag) {
        return tagService.checkExists(tag);
    }

    /**
     * 批量删除标签
     *
     * @param tags
     * @return
     */
    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public Response deletes(@RequestBody List<Tag> tags) {
        return tagService.deleteInBatch(tags);
    }

}
