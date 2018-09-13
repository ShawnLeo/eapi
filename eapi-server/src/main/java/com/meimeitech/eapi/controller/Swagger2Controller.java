package com.meimeitech.eapi.controller;

import com.meimeitech.eapi.service.Swagger2Service;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
public class Swagger2Controller {
    public static final String DEFAULT_URL = "/v2/api-docs/{projectId}";
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    @Autowired
    private Swagger2Service swagger2Service;

    @Autowired
    private JsonSerializer jsonSerializer;
    @ResponseBody
    @RequestMapping(
            value = DEFAULT_URL,
            method = RequestMethod.GET,
            produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
    public ResponseEntity<Json> getDocumentation(@PathVariable("projectId") String projectId,  @RequestParam(value = "group",required = false) String swaggerGroup, HttpServletRequest servletRequest) {
        Swagger swagger = swagger2Service.buildSwagger(projectId);
        return new ResponseEntity(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

}
