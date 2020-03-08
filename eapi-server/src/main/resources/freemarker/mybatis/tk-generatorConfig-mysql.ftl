<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<!--suppress MybatisGenerateCustomPluginInspection -->
<generatorConfiguration>
    <context id="Mysql" targetRuntime="MyBatis3" defaultModelType="flat">
        <property name="javaFileEncoding" value="UTF-8"/>
        <property name="useMapperCommentGenerator" value="true"/>

    <#if javaVoGeneratorFlag == "true">
        <plugin type="com.shawn.generator.tools.mybatis.plugin.VOPlugin">
            <property name="targetProject" value="${targetProject}"/>
            <property name="targetPackage" value="${targetPackage}.VO"/>
            <property name="classSuffix" value="VO"/>
        </plugin>
    </#if>
        <plugin type="com.shawn.generator.tools.mybatis.plugin.FlatModelRulesPlugin"/>

        <plugin type="com.shawn.generator.tools.mybatis.plugin.JsonPlugin">
            <property name="targetProject" value="${targetProject}"/>
        </plugin>

        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="tk.mybatis.mapper.common.Mapper"/>
            <property name="caseSensitive" value="true"/>
            <property name="forceAnnotation" value="true"/>
            <property name="generateColumnConsts" value="true"/>
            <property name="generateDefaultInstanceMethod" value="true"/>
            <property name="beginningDelimiter" value="`"/>
            <property name="endingDelimiter" value="`"/>
        </plugin>

        <plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
            <property name="templateFormatter"
                      value="tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter"/>
            <property name="targetProject" value="${targetProject}"/>
            <property name="targetPackage" value="${targetPackage}.dao.mapper"/>
            <property name="templatePath" value="freemarker/mybatis/mapper.ftl"/>
            <property name="mapperSuffix" value="DAO"/>
            <property name="fileName"
                      value="${originalTemplateParameter("tableClass.shortClassName")}${originalTemplateParameter("mapperSuffix")}.java"/>
        </plugin>

        <!--mapper.xml-->
        <plugin type="tk.mybatis.mapper.generator.TemplateFilePlugin">
            <property name="templateFormatter"
                      value="tk.mybatis.mapper.generator.formatter.FreemarkerTemplateFormatter"/>
            <property name="targetProject" value="${targetProject}"/>
            <property name="targetPackage" value="${targetPackage}.dao.xml"/>
            <property name="mapperPackage" value="${targetPackage}.dao.mapper"/>
            <property name="templatePath" value="freemarker/mybatis/mapperXml.ftl"/>
            <property name="mapperSuffix" value="DAO"/>
            <property name="fileName"
                      value="${originalTemplateParameter("tableClass.shortClassName")}${originalTemplateParameter("mapperSuffix")}.xml"/>
        </plugin>

        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin">
            <property name="useToStringFromRoot" value="true"/>
        </plugin>

        <jdbcConnection driverClass="${dbDriverClass}"
                        connectionURL="${stringEscapeXml(dbConnectionURL)}"
                        userId="${dbUsername}"
                        password="${dbPassword}">
            <property name="useInformationSchema" value="true"></property>
        </jdbcConnection>

        <javaModelGenerator targetPackage="${targetPackage}.dao.DO" targetProject="${targetProject}"/>

    <#list tableList as table>
        <table tableName="${table.tableName}" domainObjectName="${table.domainObjectName}">
            <property name="useActualColumnNames" value="false"/>
            <#if table.generatedKey??>
                <generatedKey column="${table.generatedKey}" sqlStatement="MySql" identity="true"/>
            </#if>
            <#list table.ignoreColumns as ignoreColumn>
                <ignoreColumn column="${ignoreColumn}"/>
            </#list>
        </table>
    </#list>
    </context>
</generatorConfiguration>