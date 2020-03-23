package com.shawn.eapi.controller;

import com.shawn.common.vo.Response;
import com.shawn.eapi.entity.DataModel;
import com.shawn.eapi.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datamodel")
public class DataModelController {

    @Autowired
    private DataModelService dataModelService;


    /**
     * 更新数据模型
     *
     * @param dataModel
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response update(@RequestBody DataModel dataModel) {
        return dataModelService.update(dataModel);
    }

    /**
     * 添加新数据模型
     *
     * @param dataModel
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response create(@RequestBody DataModel dataModel) {
        return dataModelService.create(dataModel);
    }

    /**
     * 检查数据名称是否已存在
     *
     * @param dataModel
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Response checkExists(@RequestBody DataModel dataModel) {
        return dataModelService.checkExists(dataModel);
    }

    /**
     * 基础数据模型列表
     *
     * @returnzxc
     */
    @RequestMapping(value = "/{type}/list", method = RequestMethod.GET)
    public Response sysList(@PathVariable String type, @RequestParam(defaultValue = "") String projectId) {
        return dataModelService.findByTypeAndProjectId(type, projectId);
    }

    /**
     * 根据ID获取数据模型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response get(@PathVariable String id) {
        return dataModelService.findById(id);
    }

    /**
     * 批量删除接口
     *
     * @param dataModels
     * @return
     */
    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public Response deletes(@RequestBody List<DataModel> dataModels) {
        return dataModelService.deleteInBatch(dataModels);
    }
}
