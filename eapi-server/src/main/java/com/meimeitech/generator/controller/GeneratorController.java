package com.meimeitech.generator.controller;

import com.alibaba.fastjson.JSON;
import com.meimeitech.common.vo.Response;
import com.meimeitech.eapi.service.Swagger2Service;
import com.meimeitech.generator.model.Field;
import com.meimeitech.generator.tools.ZipFileUtil;
import com.meimeitech.generator.tools.mybatis.util.DatabaseUtil;
import com.meimeitech.generator.tools.mybatis.util.MyBatisGeneratorUtil;
import com.meimeitech.generator.tools.mybatis.util.MybatisGeneratorConfigModel;
import com.meimeitech.generator.tools.swagger.Generator;
import com.meimeitech.generator.tools.swagger.vo.SwaggerConfigVO;
import org.apache.commons.io.FileUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.JsonSerializer;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
    public Response swagger(@RequestBody SwaggerConfigVO swaggerConfigVO) {
        try {
            long l = System.currentTimeMillis();
            String root = codeGenerateLocation() + File.separator + l;
//            File swaggerFile = new File(root + File.separator + "Swagger.json");
            String targetProject = root + File.separator + swaggerConfigVO.getLang();
//            String axios = root + File.separator + "Axios"+ File.separator + "gen";

//            Swagger swagger = swagger2Service.buildSwagger(swaggerConfigVO.getTargetProject(), Swagger2Service.BuildType.SWAGGER_JSON);
//            Json json = jsonSerializer.toJson(swagger);

//            FileUtils.writeStringToFile(swaggerFile, json.value(), "UTF-8");
//            String swaggerFile = swaggerConfigVO.getTargetProject();

            Generator.builder()
                    .swaggerJson(swaggerConfigVO.getTargetProject())
                    .apiPackage(swaggerConfigVO.getApiPackage())
                    .modelPackage(swaggerConfigVO.getModelPackage())
                    .targetProject(targetProject)
                    .lang(swaggerConfigVO.getLang())
                    .library(swaggerConfigVO.getLibrary())
                    .generateSupportingFiles(false)
                    .build().generatorController();

//            Generator.builder()
//                    .swaggerJson("file:///" + swaggerFile.getPath())
//                    .targetProject(axios)
//                    .build().generatorAxiosClient();

            return Response.success(l + "");
        } catch (Exception e) {
            e.printStackTrace();
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

    @RequestMapping(value = "/vue/table/{vueName}/{rowNum}", method = RequestMethod.POST)
    public Response<String> generateTable(@PathVariable String vueName,
                                        @PathVariable Integer rowNum,
                                        @RequestBody List<Field> fields) throws IOException {

        try {
            String result = generate("table.btl", vueName, rowNum, fields);
            return Response.success(result);
        }catch (IllegalArgumentException e){
            return Response.error(e.getMessage());
        }
    }

    public String generate(String template, String vueName, Integer rowNum, List<Field> fields) throws IOException {
        // 模板路径
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("\\beetl\\vue","utf-8");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

        Template tableTemplate = gt.getTemplate(template);
        // 排序
        Collections.sort(fields, Comparator.comparing(Field::getSortOrder));
        // 绑定变量
        tableTemplate.binding("vueName", vueName);
        tableTemplate.binding("fields", fields);
        // 判断有无upload和日期范围搜索
        Boolean upload = false;
        for(Field f:fields){
            if("upload".equals(f.getType())){
                upload = true;
            }
        }
        tableTemplate.binding("upload", upload);
        if("table.btl".equals(template)){
            // 判断有无upload和日期范围搜索
            Boolean daterangeSearch = false;
            for(Field f:fields){
                if(f.getSearchable()&&"daterange".equals(f.getSearchType())){
                    daterangeSearch = true;
                }
            }
            tableTemplate.binding("daterangeSearch", daterangeSearch);
            // 统计搜索栏个数 判断是否隐藏搜索栏
            Boolean hideSearch = false;
            List<Field> firstTwo = new ArrayList<>();
            List<Field> rest = new ArrayList<>();
            Integer count = 0;
            for(Field f:fields){
                if(f.getSearchable()){
                    count++;
                    if(count<=2){
                        firstTwo.add(f);
                    }else{
                        rest.add(f);
                    }
                }
            }
            if(count>=4){
                hideSearch = true;
                tableTemplate.binding("firstTwo", firstTwo);
                tableTemplate.binding("rest", rest);
            }
            tableTemplate.binding("searchSize", count);
            tableTemplate.binding("hideSearch", hideSearch);
            // 获取默认排序字段
            String defaultSort = "", defaultSortType = "";
            for(Field f:fields){
                if(f.getDefaultSort()){
                    defaultSort = f.getField();
                    defaultSortType = f.getDefaultSortType();
                    break;
                }
            }
            tableTemplate.binding("defaultSort", defaultSort);
            tableTemplate.binding("defaultSortType", defaultSortType);
        }
        // 一行几列
        tableTemplate.binding("rowNum", rowNum);
        if(rowNum==1){
            tableTemplate.binding("modalWidth", 500);
            tableTemplate.binding("width", "100%");
            tableTemplate.binding("editWidth", "100%");
            tableTemplate.binding("itemWidth", "");
            tableTemplate.binding("span", "9");
        }else if(rowNum==2){
            tableTemplate.binding("modalWidth", 770);
            tableTemplate.binding("width", "250px");
            tableTemplate.binding("editWidth", "250px");
            tableTemplate.binding("itemWidth", "350px");
            tableTemplate.binding("span", "17");
        }else if(rowNum==3){
            tableTemplate.binding("modalWidth", 980);
            tableTemplate.binding("width", "200px");
            tableTemplate.binding("editWidth", "200px");
            tableTemplate.binding("itemWidth", "300px");
            tableTemplate.binding("span", "17");
        }else if(rowNum==4){
            tableTemplate.binding("modalWidth", 1130);
            tableTemplate.binding("width", "160px");
            tableTemplate.binding("editWidth", "160px");
            tableTemplate.binding("itemWidth", "260px");
            tableTemplate.binding("span", "17");
        }else{
            throw new IllegalArgumentException("rowNum仅支持数字1-4");
        }
        // 生成代码
        String result = tableTemplate.render();
        System.out.println(result);
        return result;
    }

    @RequestMapping(value = "/vue/json", method = RequestMethod.POST)
    public Response<String> getEntityData(String json) {
        Map parse = null;
        try {
            parse = JSON.parseObject(json, Map.class);
        }catch (Exception e){
            return Response.error("JSON格式错误");
        }
        return Response.success(gengerateEntityData(parse));
    }

    public String gengerateEntityData(Map json){
        List<Map> result = new ArrayList<Map>();
        final AtomicInteger index = new AtomicInteger(1);
        json.forEach((key,value)->{
            String field_json = "\n        {\n" +
                    "            \"sortOrder\": " + index.getAndIncrement() + ",\n" +
                    "            \"field\": \"" + key + "\",\n" +
                    "            \"name\": \"" + value + "\",\n" +
                    "            \"level\": \"2\",\n" +
                    "            \"tableShow\": true,\n" +
                    "            \"editable\": true,\n" +
                    "            \"type\": \"text\",\n" +
                    "            \"searchType\": \"text\",\n" +
                    "            \"searchLevel\": \"2\",\n" +
                    "            \"validate\": false,\n" +
                    "            \"searchable\": true,\n" +
                    "            \"sortable\": false,\n" +
                    "            \"defaultSort\": false,\n" +
                    "            \"defaultSortType\": \"desc\"\n" +
                    "        }";
            result.add(JSON.parseObject(field_json, Map.class));
        });

        Map<String,Object> map= new HashMap<>();
        map.put("data",result);
        return JSON.toJSONString(map);
    }

}
