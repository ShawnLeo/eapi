package com.meimeitech.generator.tools.mybatis.util;

import freemarker.template.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/4/16
 */
public class MybatisGeneratorConfigUtil {
    public static void main(String[] args) throws IOException, TemplateException {

        MybatisGeneratorConfigModel mysqlGeneratorModel = MybatisGeneratorConfigModel.builder()
                .targetPackage("com.zhijiansihang.gen")
                .targetProject("C:\\pengrun\\work\\code\\git\\mybatis-generator\\gen")
                .dbDriverClass("com.mysql.jdbc.Driver")
                .dbConnectionURL("jdbc:mysql://127.0.0.1:3306/ApolloConfigDB?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
                .dbUsername("root")
                .dbPassword("root")
                .build();

        mysqlGeneratorModel.getTableList().add(
                MybatisGeneratorConfigModel.TableInfo.builder()
                        .domainObjectName("User")
                        .generatedKey("user_id")
                        .tableName("user").build()
        );
        mysqlGeneratorModel.getTableList().add(
                MybatisGeneratorConfigModel.TableInfo.builder()
                        .domainObjectName("User1")
                        .ignoreColumns(Collections.singletonList("hello"))
                        .tableName("user1").build()
        );
        String generator = MybatisGeneratorConfigUtil.generator(mysqlGeneratorModel);
        System.out.println(generator);
    }

    public static String generator(MybatisGeneratorConfigModel mybatisGeneratorConfigModel) {
        if (mybatisGeneratorConfigModel.getTemplateName() == null) {
            mybatisGeneratorConfigModel.setTemplateName("generatorConfig-mysql.ftl");
        }
        if (mybatisGeneratorConfigModel.getTemplateLocation() == null) {
            mybatisGeneratorConfigModel.setTemplateLocation("/freemarker/mybatis");
        }

        Template template = geTemplate(mybatisGeneratorConfigModel.getTemplateName(), mybatisGeneratorConfigModel.getTemplateLocation());
        if (template == null) {
            throw new IllegalArgumentException();
        }
        try {
            StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
            template.process(mybatisGeneratorConfigModel, stringBuilderWriter);
            return stringBuilderWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Template geTemplate(String name, String basePackagePath) {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), basePackagePath);
            cfg.setSharedVariable("stringEscapeXml", new StringEscapeXml10TemplateMethodModelEx());
            cfg.setSharedVariable("originalTemplateParameter", new OriginalTemplateParameterMethodModelEx());
            Template temp = cfg.getTemplate(name);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class StringEscapeXml10TemplateMethodModelEx implements TemplateMethodModelEx {
        @Override
        public Object exec(List list) throws TemplateModelException {
            if (list == null || list.size() != 1) {
                throw new TemplateModelException();
            }
            String value = list.get(0).toString();
            return StringEscapeUtils.escapeXml10(value);
        }
    }

    private static class OriginalTemplateParameterMethodModelEx implements TemplateMethodModelEx {
        @Override
        public Object exec(List list) throws TemplateModelException {
            if (list == null || list.size() != 1) {
                throw new TemplateModelException();
            }
            String value = list.get(0).toString();
            return "${" + value + "}";
        }
    }


}
