package com.meimeitech.generator.controller;

import com.meimeitech.common.vo.Response;
import com.meimeitech.generator.tools.ZipFileUtil;
import com.meimeitech.generator.tools.mybatis.util.DatabaseUtil;
import com.meimeitech.generator.tools.mybatis.util.MyBatisGeneratorUtil;
import com.meimeitech.generator.tools.mybatis.util.MybatisGeneratorConfigModel;
import com.meimeitech.generator.tools.swagger.Generator;
import com.meimeitech.generator.tools.swagger.vo.SwaggerConfigVO;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                return Response.error("数据库连接失败！");
            }
            Map<DatabaseUtil.Table, List<DatabaseUtil.Column>> columns = DatabaseUtil.info(mysqlGeneratorModel);

            ArrayList<DatabaseUtil.Table> tablesMap = new ArrayList<>();
            Map<String, List<DatabaseUtil.Column>> columnsMap = new HashMap<>();
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
            // TODO 可能多个数据源，可根据数据库名称配置
            String databaseName = "mybatis";

            String root = codeGenerateLocation() + File.separator + databaseName;

            mysqlGeneratorModel.setTargetProject(root);

            File genFolder = new File(root);

            // 删除源文件
            FileUtils.deleteDirectory(genFolder);

            // 创建文件夹
            FileUtils.forceMkdir(genFolder);

            MyBatisGeneratorUtil.generator(mysqlGeneratorModel);

            // 根据项目l压缩一份
            ZipFileUtil.compressFiles2Zip(root);

            return Response.success(databaseName);

        } catch (Exception e) {
            return Response.error("生成失败:" + e.getMessage());
        }
    }


    @RequestMapping(value = "/database/gen", method = RequestMethod.GET)
    public ResponseEntity download(@RequestParam String databaseName) throws IOException {
        return zip(databaseName);
    }

    @RequestMapping(value = "/swagger/gen", method = RequestMethod.POST)
    public Response swagger(@RequestBody SwaggerConfigVO swaggerConfigVO) {
        try {
            String root = codeGenerateLocation() + File.separator + swaggerConfigVO.getTargetProjectId();
            String targetProject = root + File.separator + swaggerConfigVO.getLang();

            FileUtils.deleteDirectory(new File(root));

            Generator.builder()
                    .swaggerJson(swaggerConfigVO.getTargetProject())
                    .apiPackage(swaggerConfigVO.getApiPackage())
                    .modelPackage(swaggerConfigVO.getModelPackage())
                    .targetProject(targetProject)
                    .lang(swaggerConfigVO.getLang())
                    .library(swaggerConfigVO.getLibrary())
                    .generateSupportingFiles(false)
                    .build().generatorController();

            // 根据项目Id压缩一份
            ZipFileUtil.compressFiles2Zip(root);

            return Response.success("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("生成失败:" + e.getMessage());
        }
    }

    @RequestMapping(value = "/swagger/gen", method = RequestMethod.GET)
    public ResponseEntity swaggerDownload(@RequestParam String targetProjectId) throws IOException {
        return zip(targetProjectId);
    }

    private static ResponseEntity zip(String name) throws IOException {

        // 直接下载zip
        String codePath = codeGenerateLocation() + File.separator + name  + ".zip";

        HttpHeaders headers = new HttpHeaders();
        File file = new File(codePath);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", name + ".zip");

        return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);

//        String codePath = codeGenerateLocation() + File.separator + uuid;
//        File testFile = new File(codePath);
//        if (!(testFile.exists() && testFile.isDirectory())) {
//            throw new IllegalArgumentException();
//        }
//        ZipFileUtil.compressFiles2Zip(testFile.getPath());
//        HttpHeaders headers = new HttpHeaders();
//        File file = new File(testFile.getPath() + ".zip");
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", uuid + ".zip");
//        try {
//            return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
//                    headers, HttpStatus.CREATED);
//        } finally {
//            file.delete();
//            FileUtils.deleteDirectory(testFile);
//        }
    }

}
