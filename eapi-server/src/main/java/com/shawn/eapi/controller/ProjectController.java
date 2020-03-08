package com.shawn.eapi.controller;

import com.shawn.common.vo.Response;
import com.shawn.eapi.entity.Project;
import com.shawn.eapi.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * Find project by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response get(@PathVariable String id) {
        return projectService.findById(id);
    }


    /**
     * Create new project
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response create(@RequestBody Project project) {
        return projectService.create(project);
    }

    /**
     * Update project by id
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response update(@RequestBody Project project) {
        return projectService.update(project);
    }

    /**
     * Get project list
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Response list( @RequestParam(value = "groupId") String groupId) {
        return projectService.list(groupId);
    }

    /**
     * Check if project name already exists
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Response checkExists(@RequestBody Project project) {
        return projectService.checkExists(project);
    }

    /**
     * Delete by id
     *
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Response delete(@PathVariable String id) {
        return projectService.delete(id);
    }

    /**
     * Publish project
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Response publish(@RequestBody Project project) {
        return projectService.publish(project);
    }


}
