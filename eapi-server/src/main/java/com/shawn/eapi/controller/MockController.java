package com.shawn.eapi.controller;

import com.shawn.eapi.util.ExampleGenerator;
import com.shawn.eapi.service.Swagger2Service;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;
import springfox.documentation.spring.web.json.Json;


import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
public class MockController {

    private static Logger logger = LoggerFactory.getLogger(MockController.class);

    public static final String DEFAULT_URL = "/virtserver/{projectId}/{version}/**";
    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    @Autowired
    private Swagger2Service swagger2Service;

    @ResponseBody
    @RequestMapping(
            value = DEFAULT_URL,
            produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
    public ResponseEntity<Json> mock(@PathVariable("projectId") String projectId,
                                                 @PathVariable("version") String version , HttpServletRequest request) {
        logger.info("projectId:" + projectId + ", version:" + version );
        String requestPath = request.getServletPath().split(projectId + "/" + version)[1];

        String method = request.getMethod().toLowerCase();

        Swagger swagger = swagger2Service.buildSwagger(projectId, Swagger2Service.BuildType.SWAGGER_UI);

        Path swaggerPath = getPath(swagger, requestPath);

        Operation operation = getOperation(swaggerPath, method);

        if (operation == null ) {
            if (swaggerPath.getOperations().size() > 0 )
                return responseEntity(HttpStatus.METHOD_NOT_ALLOWED); // 405
            else
                return responseEntity(HttpStatus.NOT_FOUND); // 404
        }
        // query参数校验
        if (!areAllRequiredParametersPopulated(request, operation)) {
            return responseEntity(HttpStatus.BAD_REQUEST);
        }
        Map<String, Response> responses =  operation.getResponses();

        if (responses !=null) {
            List<String> keys = new ArrayList<>();
            Random random = new Random();

            for (Map.Entry<String, Response> entrySet : responses.entrySet()) {
                keys.add(entrySet.getKey());
            }
            // 随机返回一个成功的响应
            String sucessCode = keys.get(random.nextInt(keys.size()));
            Response response = responses.get(sucessCode);
            Map<String, Object> examples = response.getExamples();

            if (examples == null) {
                List<Map<String, String>> mapList =  new ExampleGenerator(swagger.getDefinitions()).generate(examples, operation.getProduces(), response.getSchema());
                return new ResponseEntity( mapList.get(0).get("example"), HttpStatus.NOT_IMPLEMENTED);
            }
            return new ResponseEntity(examples, HttpStatus.NOT_IMPLEMENTED);
        } else {
            return new ResponseEntity("Could not get response define", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Path getPath (Swagger swagger, String requestPath) {
        Map<String, Path> paths = swagger.getPaths();
        Path swaggerPath = paths.get(requestPath);
        if (swaggerPath == null) {
            for (Map.Entry<String, Path> entrySet : paths.entrySet()) {
                if (isURIMatch(requestPath, entrySet.getKey())) {
                    swaggerPath = entrySet.getValue();
                }
            }
        }
        return  swaggerPath;
    }

    private Operation getOperation (Path swaggerPath, String method ) {

        if (swaggerPath == null){
            return null;
        }

        if (method.equals("get")) {
            return swaggerPath.getGet();
        }
        if (method.equals("post")) {
            return  swaggerPath.getPost();
        }
        if (method.equals("put")) {
            return swaggerPath.getPut();
        }
        if (method.equals("delete")) {
            return swaggerPath.getDelete();
        }

        return null;
    }

    private boolean isURIMatch(String sourceURI, String targetURI) {
        UriTemplate uriTemplate = new UriTemplate(targetURI);
        return uriTemplate.matches(sourceURI);
    }

    private boolean areAllRequiredParametersPopulated(HttpServletRequest req,  Operation operation) {

       List<Parameter> parameters = operation.getParameters();
        logger.info("Parameters:" +req.getParameterMap());

        if (parameters != null) {
            for (Parameter requestParameter : parameters) {
                if (requestParameter.getRequired()
                        && StringUtils.isEmpty(req.getParameter(requestParameter.getName())) && (requestParameter instanceof QueryParameter)) {

                    logger.info("Mandatory Parameter " +requestParameter.getName() + " is not Populated");
                    return false;
                }
            }
        }
        return true;
    }

    private ResponseEntity responseEntity(HttpStatus status) {
        return new ResponseEntity(status.getReasonPhrase(), status);
    }
}
