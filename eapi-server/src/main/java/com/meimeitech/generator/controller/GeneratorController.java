package com.meimeitech.generator.controller;

import com.meimeitech.common.vo.Response;
import com.meimeitech.eapi.service.Swagger2Service;
import com.meimeitech.generator.tools.ZipFileUtil;
import com.meimeitech.generator.tools.mybatis.util.DatabaseUtil;
import com.meimeitech.generator.tools.mybatis.util.MyBatisGeneratorUtil;
import com.meimeitech.generator.tools.mybatis.util.MybatisGeneratorConfigModel;
import com.meimeitech.generator.tools.swagger.Generator;
import io.swagger.models.Swagger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/generator")
public class GeneratorController {

    /**
     * 添加新数据模型
     */
    @RequestMapping(value = "/database/all", method = RequestMethod.POST)
    public Response create(@RequestBody MybatisGeneratorConfigModel mysqlGeneratorModel) {
        try {
            boolean b = DatabaseUtil.testConnection(mysqlGeneratorModel);
            if (!b) {
                return Response.error("请检查数据库配置");
            }
            Map<DatabaseUtil.Table, List<DatabaseUtil.Column>> columns = DatabaseUtil.info(mysqlGeneratorModel);

            ArrayList<DatabaseUtil.Table> tablesMap = new ArrayList<>();
            Map<String, List<DatabaseUtil.Column>> columnsMap = new HashMap<>();
            AtomicInteger integer = new AtomicInteger(1);
            columns.forEach((k, v) -> {
                tablesMap.add(k);
                columnsMap.put(k.getTableName(), v);
            });
            Map<String, Object> all = new HashMap<>();
            all.put("table", tablesMap);
            all.put("column", columnsMap);
            return Response.success(all);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    /**
     * 添加新数据模型
     */
    @RequestMapping(value = "/database/test", method = RequestMethod.POST)
    public Response test(@RequestBody MybatisGeneratorConfigModel mysqlGeneratorModel) {
        try {
            boolean b = DatabaseUtil.testConnection(mysqlGeneratorModel);
            return Response.success(true);
        } catch (Exception e) {
            return Response.success(false);
        }
    }

    public static String codeGenerateLocation() {
        String property = System.getProperty("user.dir");
        File file = new File(property);
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            return parentFile.getPath() + File.separator + "code_gen_temp";
        } else {
            return file.getPath();
        }
    }

    /**
     * 添加新数据模型
     */
    @RequestMapping(value = "/database/gen", method = RequestMethod.POST)
    public Response gen(@RequestBody MybatisGeneratorConfigModel mysqlGeneratorModel) {
        try {
            long l = System.currentTimeMillis();
            mysqlGeneratorModel.setTargetProject(codeGenerateLocation() + File.separator + l);
            FileUtils.forceMkdir(new File(mysqlGeneratorModel.getTargetProject()));

            MyBatisGeneratorUtil.generator(mysqlGeneratorModel);
            return Response.success(l + "");
        } catch (Exception e) {
            return Response.error("生成失败:" + e.getMessage());
        }
    }


    @RequestMapping(value = "/database/gen", method = RequestMethod.GET)
    public ResponseEntity download(@RequestParam Long uuid, HttpServletResponse response) throws IOException {
        return zip(uuid, response);
    }

    @Autowired
    private Swagger2Service swagger2Service;
    @Autowired
    private JsonSerializer jsonSerializer;

    @RequestMapping(value = "/swagger/gen", method = RequestMethod.POST)
    public Response swagger(@RequestBody MybatisGeneratorConfigModel mysqlGeneratorModel) {
        try {
            long l = System.currentTimeMillis();
            String root = codeGenerateLocation() + File.separator + l;
            File swaggerFile = new File(root + File.separator + "Swagger.json");
            String springBoot = root + File.separator + "SpringBoot";
            String axios = root + File.separator + "Axios"+ File.separator + "gen";

            Swagger swagger = swagger2Service.buildSwagger(mysqlGeneratorModel.getTargetProject());
            Json json = jsonSerializer.toJson(swagger);

            FileUtils.writeStringToFile(swaggerFile, json.value(), "UTF-8");

            Generator.builder()
                    .swaggerJson("file:///" + swaggerFile.getPath())
                    .targetPackage(mysqlGeneratorModel.getTargetPackage())
                    .targetProject(springBoot)
                    .build().generatorController();

            Generator.builder()
                    .swaggerJson("file:///" + swaggerFile.getPath())
                    .targetProject(axios)
                    .build().generatorAxiosClient();

            return Response.success(l + "");
        } catch (Exception e) {
            return Response.error("生成失败:" + e.getMessage());
        }
    }


    @RequestMapping(value = "/swagger/gen", method = RequestMethod.GET)
    public ResponseEntity swaggerDownload(@RequestParam Long uuid, HttpServletResponse response) throws IOException {
        return zip(uuid, response);
    }

    private static ResponseEntity zip(Long uuid, HttpServletResponse response) throws IOException {
        String codePath = codeGenerateLocation() + File.separator + uuid;
        File testFile = new File(codePath);
        if (!(testFile.exists() && testFile.isDirectory())) {
            throw new IllegalArgumentException();
        }
        String zipFilePath = ZipFileUtil.compressFiles2Zip(testFile.getPath());
        HttpHeaders headers = new HttpHeaders();
        File file = new File(zipFilePath);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", uuid + ".zip");
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.CREATED);
        } finally {
            file.delete();
            testFile.delete();
        }
    }

}
