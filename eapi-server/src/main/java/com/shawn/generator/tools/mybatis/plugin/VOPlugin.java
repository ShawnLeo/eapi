package com.shawn.generator.tools.mybatis.plugin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.PluginAggregator;
import org.mybatis.generator.plugins.EqualsHashCodePlugin;
import org.mybatis.generator.plugins.SerializablePlugin;
import org.mybatis.generator.plugins.ToStringPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by paul on 2018/12/22.
 */
public class VOPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();

        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(targetPackage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName() + classSuffix);
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(sb.toString());

        AbstractJavaGenerator javaGenerator = new SampleVOGenerator(type);
        javaGenerator.setContext(context);
        javaGenerator.setIntrospectedTable(introspectedTable);
        List<CompilationUnit> compilationUnits = javaGenerator
                .getCompilationUnits();
        for (CompilationUnit compilationUnit : compilationUnits) {
            GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                    targetProject,
                    context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                    context.getJavaFormatter());
            generatedJavaFiles.add(gjf);
        }
        return generatedJavaFiles;
    }

    private String targetProject;
    private String targetPackage;
    private String classSuffix;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        targetProject = properties.getProperty("targetProject").trim();
        targetPackage = properties.getProperty("targetPackage").trim();
        classSuffix = properties.getProperty("classSuffix").trim();
    }



    public class SampleVOGenerator extends AbstractJavaGenerator {

        private FullyQualifiedJavaType fullyQualifiedJavaType;

        public SampleVOGenerator(FullyQualifiedJavaType fullyQualifiedJavaType) {
            super();
            this.fullyQualifiedJavaType = fullyQualifiedJavaType;
        }

        @Override
        public List<CompilationUnit> getCompilationUnits() {
            CommentGenerator commentGenerator = context.getCommentGenerator();
            TopLevelClass topLevelClass = new TopLevelClass(fullyQualifiedJavaType);
            topLevelClass.setVisibility(JavaVisibility.PUBLIC);
            commentGenerator.addJavaFileComment(topLevelClass);
            FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            if (superClass != null) {
                topLevelClass.setSuperClass(superClass);
                topLevelClass.addImportedType(superClass);
            }
            commentGenerator.addModelClassComment(topLevelClass, introspectedTable);
            List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
            try {
                Field plugins = PluginAggregator.class.getDeclaredField("plugins");
                plugins.setAccessible(true);
                List<Plugin> results = (List<Plugin>) plugins.get(context.getPlugins());
                boolean success = true;
                for (Plugin plugin : results) {
                    if (
                            (plugin instanceof EqualsHashCodePlugin || plugin instanceof ToStringPlugin || plugin instanceof SerializablePlugin)
                                    &&
                                    !plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
                            ) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    answer.add(topLevelClass);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return answer;
        }
    }

    public static void main(String[] args) {
//java.math.BigDecimal,
//java.lang.Float,
//java.lang.Long,
//java.lang.Double,
//java.lang.Short,
//java.util.Date,
//java.lang.Boolean,
//java.lang.Object,
//java.lang.String,
//java.lang.Byte,
//java.lang.Integer
//byte[];

    }

}
