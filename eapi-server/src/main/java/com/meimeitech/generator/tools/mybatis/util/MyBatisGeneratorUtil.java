package com.meimeitech.generator.tools.mybatis.util;

import com.meimeitech.generator.tools.mybatis.generator.CustomMyBatisGenerator;
import com.meimeitech.generator.tools.mybatis.util.compiler.Compiler;
import com.meimeitech.generator.tools.mybatis.util.compiler.JdkCompiler;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author paul
 * @description
 * @date 2019/4/17
 */
public class MyBatisGeneratorUtil {
    public static List<Class> getGeneratorModelClass(MybatisGeneratorConfigModel mybatisGeneratorConfigModel) {
        mybatisGeneratorConfigModel.onlyModel();

        ObjectRefUtil<CustomMyBatisGenerator> objectRefUtil = new ObjectRefUtil<>();

        generator(mybatisGeneratorConfigModel, (v) -> {
            try {
                v.generateAndNoWriteFiles();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, objectRefUtil);
        CustomMyBatisGenerator value = objectRefUtil.getValue();
        List<GeneratedJavaFile> generatedJavaFiles = value.getGeneratedJavaFiles();
        MyBatisGeneratorClassLoader myBatisGeneratorClassLoader = new MyBatisGeneratorClassLoader(Thread.currentThread().getContextClassLoader());
        List<Class> classes = new ArrayList<>();
        if (generatedJavaFiles == null || generatedJavaFiles.size() == 0) {
            return classes;
        }

        Compiler compiler = new JdkCompiler();
        for (GeneratedJavaFile generatedJavaFile : generatedJavaFiles) {
            classes.add(compiler.compile(generatedJavaFile.getFormattedContent(), myBatisGeneratorClassLoader));
        }
        return classes;
    }


    public static boolean generator(MybatisGeneratorConfigModel mybatisGeneratorConfigModel) {
        return generator(mybatisGeneratorConfigModel, (v) -> {
            try {
                v.generate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, null);
    }

    public static boolean generateAndNoWriteFiles(MybatisGeneratorConfigModel mybatisGeneratorConfigModel) {

        return generator(mybatisGeneratorConfigModel, (v) -> {
            try {
                v.generateAndNoWriteFiles();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, null);
    }


    private static boolean generator(MybatisGeneratorConfigModel mybatisGeneratorConfigModel, Consumer<CustomMyBatisGenerator> consumer, ObjectRefUtil<CustomMyBatisGenerator> objectRefUtil) {
        mybatisGeneratorConfigModel.genCheck();
        List<MybatisGeneratorConfigModel.TableInfo> tableList = mybatisGeneratorConfigModel.getTableList();
        if (tableList == null || tableList.size() == 0){
            throw new IllegalArgumentException();
        }
        try {
            List<String> warnings = new ArrayList<String>();
            String generatorConfig = MybatisGeneratorConfigUtil.generator(mybatisGeneratorConfigModel);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(new StringReader(generatorConfig));
            DefaultShellCallback callback = new DefaultShellCallback(true);
            CustomMyBatisGenerator myBatisGenerator = new CustomMyBatisGenerator(config, callback, warnings);
            consumer.accept(myBatisGenerator);
            for (String warning : warnings) {
                System.out.println(warning);
            }
            if (objectRefUtil != null) {
                objectRefUtil.setValue(myBatisGenerator);
            }
            copyUserInfo(mybatisGeneratorConfigModel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void copyUserInfo(MybatisGeneratorConfigModel mybatisGeneratorConfigModel){
        Template template = MybatisGeneratorConfigUtil.geTemplate("readme.ftl", "/freemarker/mybatis");
        StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
        try {
            template.process(mybatisGeneratorConfigModel,stringBuilderWriter);
            String targetProject = mybatisGeneratorConfigModel.getTargetProject();
            targetProject = targetProject.endsWith("/")?targetProject.substring(0,targetProject.length()-1):targetProject;
            String path = targetProject + "/" + mybatisGeneratorConfigModel.getTargetPackage().replaceAll("\\.", "/") + "/dao/";
            FileUtils.writeStringToFile(new File(path+"readme.txt"),stringBuilderWriter.toString(), Charset.forName("UTF-8"));
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("freemarker/mybatis/mybatis-config.xml");
            FileUtils.copyToFile(resourceAsStream,new File(path+"mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
