package com.meimeitech.eapi.controller;

import com.meimeitech.common.BizException;
import com.meimeitech.common.vo.Response;
import com.meimeitech.eapi.service.Swagger2Service;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
public class Swagger2Controller {

    public static final String DEFAULT_URL = "/v2/api-docs/{projectId}";
    public static final String EXPORT_URL = "/swagger/export/{projectId}";
    public static final String IMPORT_URL = "/swagger/import/url/{projectId}";
    public static final String IMPORT_FILE_URL = "/swagger/import/file/{projectId}";
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    @Autowired
    private Swagger2Service swagger2Service;

    @Autowired
    private JsonSerializer jsonSerializer;


    /**
     * 生成swagger.json文档
     *
     * @param projectId
     * @param swaggerGroup
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(
            value = DEFAULT_URL,
            method = RequestMethod.GET,
            produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
    public ResponseEntity<Json> getDocumentation(@PathVariable("projectId") String projectId,  @RequestParam(value = "group",required = false) String swaggerGroup, HttpServletRequest servletRequest) {
        Swagger swagger = swagger2Service.buildSwagger(projectId, Swagger2Service.BuildType.SWAGGER_UI);
        return new ResponseEntity(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

    /**
     * 导出swagger.json文档
     *
     * @param projectId
     * @param swaggerGroup
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = EXPORT_URL,method = RequestMethod.GET)
    public void export(@PathVariable("projectId") String projectId,
                       @RequestParam(value = "group",required = false) String swaggerGroup, HttpServletResponse response)
            throws IOException {

        Swagger swagger = swagger2Service.buildSwagger(projectId, Swagger2Service.BuildType.SWAGGER_JSON);
        Json json = jsonSerializer.toJson(swagger);
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("swagger.json", "utf-8"));
        response.getOutputStream().write(json.value().getBytes());

    }


    /**
     * 文件导入swagger.json文档
     *
     * @param projectId
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = IMPORT_FILE_URL,method = RequestMethod.POST)
    public Response importSwagger(@PathVariable("projectId") String projectId, @RequestParam("file") MultipartFile file) throws IOException, BizException {
        swagger2Service.importSwaggerFromFile(new String(file.getBytes()), projectId);
        return Response.success("success");
    }

    /**
     * url导入swagger.json文档
     *
     * @param projectId
     * @param swaggerUrl
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = IMPORT_URL,method = RequestMethod.POST)
    public Response importSwagger(@PathVariable("projectId") String projectId, @RequestParam("swaggerUrl") String swaggerUrl) throws BizException {
        swagger2Service.importSwaggerFromUrl(swaggerUrl, projectId);
        return Response.success("success");
    }


}
