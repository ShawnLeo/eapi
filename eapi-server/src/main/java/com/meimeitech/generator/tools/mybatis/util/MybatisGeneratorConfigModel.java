package com.meimeitech.generator.tools.mybatis.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/4/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MybatisGeneratorConfigModel {
    private String templateName;
    private String templateLocation;

    /**
     * 是否生成vo
     */
    @Builder.Default
    private String javaVoGeneratorFlag = "true";
    /**
     * 是否生成model
     */
    @Builder.Default
    private String javaModelGeneratorFlag = "true";
    /**
     * 是否生成sqlMap
     */
    @Builder.Default
    private String sqlMapGeneratorFlag = "true";
    /**
     * 是否生成javaClient
     */
    @Builder.Default
    private String javaClientGeneratorFlag = "true";
    /**
     * 是否使用lombok
     */
    @Builder.Default
    private String lombokFlag = "false";

    private String targetPackage;
    private String targetProject;

    private String dbUsername;
    private String dbPassword;
    @Builder.Default
    private String dbDriverClass = "com.mysql.jdbc.Driver";
    private String dbConnectionURL;
    @Builder.Default
    private List<TableInfo> tableList = new ArrayList<>();

    public void onlyModel() {
        this.javaVoGeneratorFlag = "false";
        this.sqlMapGeneratorFlag = "false";
        this.javaClientGeneratorFlag = "false";
        this.javaModelGeneratorFlag = "true";
    }

    public void genCheck() {
        boolean yes = false;
        if ("true".equals(this.javaVoGeneratorFlag) ||
                "true".equals(this.sqlMapGeneratorFlag) ||
                "true".equals(this.javaClientGeneratorFlag) ||
                "true".equals(this.javaModelGeneratorFlag)
                ) {
            yes = true;
        }
        if (!yes){
            throw new IllegalArgumentException();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TableInfo {
        private String tableName;
        private String domainObjectName;
        private String generatedKey;
        @Builder.Default
        private List<String> ignoreColumns = new ArrayList<>();
    }


    public List<String> tables() {
        if (tableList == null || tableList.size() == 0) {
            return null;
        }
        List<String> result = new ArrayList<>();

        tableList.forEach((v) -> {
            String tableName = v.getTableName();
            if (tableName != null && tableName.trim().length() > 0) {
                result.add(tableName.trim());
            }
        });
        if (result.size() == 0) {
            return null;
        }
        return result;
    }
}
