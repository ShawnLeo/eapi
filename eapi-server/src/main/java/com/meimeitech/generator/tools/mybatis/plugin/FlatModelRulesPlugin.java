package com.meimeitech.generator.tools.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/4/18
 */
public class FlatModelRulesPlugin extends PluginAdapter {

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setRules(new FlatModelRulesEx(introspectedTable));
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
}
