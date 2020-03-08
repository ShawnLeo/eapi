package com.shawn.generator.tools.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.rules.FlatModelRules;

/**
 * @author paul
 * @description
 * @date 2019/4/18
 */
public class FlatModelRulesEx  extends FlatModelRules {
    /**
     * Instantiates a new conditional model rules.
     *
     * @param introspectedTable the introspected table
     */
    public FlatModelRulesEx(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }

    @Override
    public boolean generateExampleClass(){
        return true;
    }



}
