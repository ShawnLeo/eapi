package com.shawn.generator.tools.mybatis.plugin;

import com.alibaba.fastjson.JSON;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.*;

/**
 * Created by paul on 2018/12/22.
 */
public class JsonPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        Iterator var11 = introspectedTable.getAllColumns().iterator();
        Map<String,Map<String,String>> result = new HashMap<>();
        Map<String,String> field = new HashMap<>();
        Map<String,String> desc = new HashMap<>();
        while (var11.hasNext()) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn) var11.next();
            //String shortName = introspectedColumn.getFullyQualifiedJavaType().getShortName();
            String format = String.format("表[%s]列[%s]java类型[%s]描述:%s", new Object[]{introspectedTable.getTableConfiguration().getTableName(), introspectedColumn.getActualColumnName(),introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters(),introspectedColumn.getRemarks()});
            desc.put(introspectedColumn.getJavaProperty(),format);
            field.put(introspectedColumn.getJavaProperty(),introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters());
        }
        result.put("field",field);
        result.put("fieldDesc",desc);
        String string = JSON.toJSONString(result);
        System.out.println(string);
        return null;
    }

    private String targetProject;
    private String targetPackage;
    private String classSuffix;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        targetProject = properties.getProperty("targetProject").trim();
    }
}
