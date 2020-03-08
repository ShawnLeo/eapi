package com.shawn.eapi.controller;

import com.shawn.common.BizException;
import com.shawn.common.vo.Response;
import com.shawn.eapi.service.Swagger2Service;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
public class Swagger2Controller {

    public static final String DEFAULT_URL = "/v2/api-docs/{projectId}";
    public static final String EXPORT_URL = "/swagger/export/{projectId}";
    public static final String EXPORT_BYINTERFACE_URL = "/swagger/export/byinteface/{projectId}";
    public static final String GET_BYINTERFACE_URL = "/swagger/get/byinteface/{projectId}";
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
     * @param buildType
     * @return
     */
    @ResponseBody
    @RequestMapping(
            value = DEFAULT_URL,
            method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    public ResponseEntity<Json> getDocumentation(@PathVariable("projectId") String projectId,
                                                 @RequestParam(name = "type", required = false) Swagger2Service.BuildType buildType) {
        if (buildType == null) {
            buildType = Swagger2Service.BuildType.SWAGGER_UI;
        }
        Swagger swagger = swagger2Service.buildSwagger(projectId, buildType);
        return new ResponseEntity(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

    /**
     * 导出swagger.json文档
     *
     * @param projectId
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = EXPORT_URL, method = RequestMethod.GET)
    public void export(@PathVariable("projectId") String projectId, HttpServletResponse response)
            throws IOException {

        Swagger swagger = swagger2Service.buildSwagger(projectId, Swagger2Service.BuildType.SWAGGER_JSON);
        Json json = jsonSerializer.toJson(swagger);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("swagger.json", "utf-8"));
        response.getOutputStream().write(json.value().getBytes());
    }

    /**
     * s
     * 文件导入swagger.json文档
     *
     * @param projectId
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = IMPORT_FILE_URL, method = RequestMethod.POST)
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
    @RequestMapping(value = IMPORT_URL, method = RequestMethod.POST)
    public Response importSwagger(@PathVariable("projectId") String projectId,
                                  @RequestParam("swaggerUrl") String swaggerUrl) throws BizException {
        swagger2Service.importSwaggerFromUrl(swaggerUrl, projectId);
        return Response.success("success");
    }


    /**
     * 根据接口导出
     *
     * @param projectId
     * @param interfaceIds
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = EXPORT_BYINTERFACE_URL, method = RequestMethod.GET)
    public void export(@PathVariable("projectId") String projectId, @RequestParam("interfaceIds[]") List<String> interfaceIds,
                       @RequestParam(name = "type", required = false) Swagger2Service.BuildType buildType,
                       HttpServletResponse response) throws IOException {
        if (buildType == null) {
            buildType = Swagger2Service.BuildType.SWAGGER_UI;
        }
        Swagger swagger = swagger2Service.buildSwagger(projectId, interfaceIds, buildType);
        Json json = jsonSerializer.toJson(swagger);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("swagger.json", "utf-8"));
        response.getOutputStream().write(json.value().getBytes());
    }

    /**
     * 根据接口导出
     *
     * @param projectId
     * @param interfaceIds
     * @throws IOException
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = GET_BYINTERFACE_URL, method = RequestMethod.POST)
    public Response export(@PathVariable("projectId") String projectId, @RequestBody List<String> interfaceIds) {

        Swagger swagger = swagger2Service.buildSwagger(projectId, interfaceIds, Swagger2Service.BuildType.SWAGGER_JSON);
        Json json = jsonSerializer.toJson(swagger);

        return Response.success(json);
    }

}
