package com.meimeitech.generator.tools.mybatis;

import com.meimeitech.generator.tools.mybatis.generator.CustomMyBatisGenerator;
import com.meimeitech.generator.tools.mybatis.util.MyBatisGeneratorUtil;
import com.meimeitech.generator.tools.mybatis.util.MybatisGeneratorConfigModel;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Paul Zhang
 * @date: 16:21 2017/12/27
 */
public class Generator {

    public static boolean run(MybatisGeneratorConfigModel model) {
        if (model == null) {
            return false;
        }
        return MyBatisGeneratorUtil.generator(model);
    }



    /**
     * 读取resource目录下的配置文件  注意生成是xml 要删除  否则生成的会追加报错
     */
    private static final String CONFIG_MYSQL = "generatorConfig-mysql.xml";
    private static final String CONFIG_ORACLE = "generatorConfig-oracle.xml";

    private static CustomMyBatisGenerator test(boolean isWriteFile) {
        CustomMyBatisGenerator myBatisGenerator = null;
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            //属性进行判断，是否合并（即追加）
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(CONFIG_MYSQL);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(is);

            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            myBatisGenerator = new CustomMyBatisGenerator(config, callback, warnings);

            if (isWriteFile) {
                myBatisGenerator.generate();
            } else {
                myBatisGenerator.generateAndNoWriteFiles();
            }
            for (String warning : warnings) {
                System.out.println(warning);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("success!!!");
        return myBatisGenerator;
    }

}
