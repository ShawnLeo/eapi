<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="mysql" targetRuntime="MyBatis3">
        <property name="autoDelimitKeywords" value="true"/>
        <property name="beginningDelimiter" value='`'/>
        <property name="endingDelimiter" value='`'/>
        <property name="javaFileEncoding" value="UTF-8"/>

    <#if javaVoGeneratorFlag == "true">
        <plugin type="com.meimeitech.generator.tools.mybatis.plugin.VOPlugin">
            <property name="targetProject" value="${targetProject}"/>
            <property name="targetPackage" value="${targetPackage}.VO"/>
            <property name="classSuffix" value="VO"/>
        </plugin>
    </#if>
        <plugin type="com.meimeitech.generator.tools.mybatis.plugin.LombokPlugin">
        </plugin>

        <plugin type="com.meimeitech.generator.tools.mybatis.plugin.JsonPlugin">
            <property name="targetProject" value="${targetProject}"/>
        </plugin>

        <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin"/>

        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin">
            <property name="useToStringFromRoot" value="true"/>
        </plugin>

        <commentGenerator type="com.meimeitech.generator.tools.mybatis.generator.CommentGeneratorHelper">
            <property name="suppressAllComments" value="fase"/>
            <property name="suppressDate" value="true"/>
        </commentGenerator>

        <jdbcConnection driverClass="${dbDriverClass}"
                        connectionURL="${stringEscapeXml(dbConnectionURL)}"
                        userId="${dbUsername}"
                        password="${dbPassword}">
            <property name="useInformationSchema" value="true"></property>
        </jdbcConnection>

        <javaTypeResolver type="org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl">
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

<#if javaModelGeneratorFlag == "true">
        <javaModelGenerator targetPackage="${targetPackage}.dao.DO"
                            targetProject="${targetProject}">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
</#if>

<#if sqlMapGeneratorFlag == "true">
        <sqlMapGenerator targetPackage="${targetPackage}.dao.xml"
                         targetProject="${targetProject}">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
</#if>

<#if javaClientGeneratorFlag == "true">
        <javaClientGenerator type="MIXEDMAPPER"
                             targetPackage="${targetPackage}.dao.mapper"
                             targetProject="${targetProject}">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
</#if>


<#list tableList as table>
        <table tableName="${table.tableName}" mapperName="${table.domainObjectName}DAO" domainObjectName="${table.domainObjectName}"
               >
            <property name="useActualColumnNames" value="false"/>
<#list table.ignoreColumns as ignoreColumn>
            <ignoreColumn column="${ignoreColumn}" />
</#list>
        </table>
</#list>

    </context>
</generatorConfiguration>

