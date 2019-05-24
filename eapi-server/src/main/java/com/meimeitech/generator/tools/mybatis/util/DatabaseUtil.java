package com.meimeitech.generator.tools.mybatis.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;

/**
 * @author paul
 * @description
 * @date 2019/4/17
 */
public class DatabaseUtil {
    protected static Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    public static boolean testConnection(MybatisGeneratorConfigModel model) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setUserId(model.getDbUsername());
        jdbcConnectionConfiguration.setPassword(model.getDbPassword());
        jdbcConnectionConfiguration.setDriverClass(model.getDbDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(model.getDbConnectionURL());

        ConnectionFactory connectionFactory = new JDBCConnectionFactory(jdbcConnectionConfiguration);
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            return true;
        }catch (Exception e){
            logger.error("测试数据库连接error",e);
            e.printStackTrace();
            return false;
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }


    public static Map<Table, List<Column>> info(MybatisGeneratorConfigModel model) throws Exception {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setUserId(model.getDbUsername());
        jdbcConnectionConfiguration.setPassword(model.getDbPassword());
        jdbcConnectionConfiguration.setDriverClass(model.getDbDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(model.getDbConnectionURL());

        ConnectionFactory connectionFactory = new JDBCConnectionFactory(jdbcConnectionConfiguration);
        Connection connection = null;
        try {
            Map<Table, List<Column>> result = new HashMap<>();
            connection = connectionFactory.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List<Table> tables = _tables(metaData);
            for (Table table : tables) {
                List<String> tablesString = model.tables();
                if (tablesString != null && !tablesString.contains(table.getTableName())) {
                    continue;
                }
                List<Column> columns = _columns(table, metaData);
                if (columns != null && columns.size() > 0) {
                    result.put(table, columns);
                }
            }
            return result;
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static List<Table> _tables(DatabaseMetaData metaData) throws Exception {
        ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
        try {
            List<Table> list = new ArrayList();
            while (resultSet.next()) {
                Table table = new Table(
                        resultSet.getString("TABLE_CAT"),
                        resultSet.getString("TABLE_SCHEM"),
                        resultSet.getString("TABLE_NAME"),
                        resultSet.getString("REMARKS"));
                list.add(table);
            }
            return list;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static List<Column> _columns(Table table, DatabaseMetaData metaData) throws Exception {
        ResultSet resultSet = metaData.getColumns(table.getCatalog(), table.getSchema(), table.getTableName(), "%");
        try {
            boolean supportsIsAutoIncrement = false;
            boolean supportsIsGeneratedColumn = false;
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int colCount = rsmd.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) {
                    supportsIsAutoIncrement = true;
                }
                if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) {
                    supportsIsGeneratedColumn = true;
                }
            }
            List<Column> columns = new ArrayList();
            while (resultSet.next()) {
                Column introspectedColumn = new Column();
                introspectedColumn.setJdbcType(resultSet.getInt("DATA_TYPE"));
                introspectedColumn.setLength(resultSet.getInt("COLUMN_SIZE"));
                introspectedColumn.setActualColumnName(resultSet.getString("COLUMN_NAME"));
                introspectedColumn
                        .setNullable(resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                introspectedColumn.setScale(resultSet.getInt("DECIMAL_DIGITS"));
                introspectedColumn.setRemarks(resultSet.getString("REMARKS"));
                introspectedColumn.setDefaultValue(resultSet.getString("COLUMN_DEF"));

                if (supportsIsAutoIncrement) {
                    introspectedColumn.setAutoIncrement("YES".equals(resultSet.getString("IS_AUTOINCREMENT")));
                }

                if (supportsIsGeneratedColumn) {
                    introspectedColumn.setGeneratedColumn("YES".equals(resultSet.getString("IS_GENERATEDCOLUMN")));
                }
                columns.add(introspectedColumn);
            }
            return columns;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static List<Table> tables(MybatisGeneratorConfigModel model) throws Exception {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setUserId(model.getDbUsername());
        jdbcConnectionConfiguration.setPassword(model.getDbPassword());
        jdbcConnectionConfiguration.setDriverClass(model.getDbDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(model.getDbConnectionURL());
        ConnectionFactory connectionFactory = new JDBCConnectionFactory(jdbcConnectionConfiguration);
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            List<Table> tables = _tables(metaData);
            return tables;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        MybatisGeneratorConfigModel mysqlGeneratorModel = MybatisGeneratorConfigModel.builder()
                .dbDriverClass("com.mysql.jdbc.Driver")
                .dbConnectionURL("jdbc:mysql://127.0.0.1:3306/ApolloConfigDB?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
                .dbUsername("root")
                .dbPassword("root")
                .build();
        Map<Table, List<Column>> columns = info(mysqlGeneratorModel);
        System.out.println(columns);
    }

    @Getter
    @Setter
    public static class Table {
        private String tableName;
        private String catalog;
        private String schema;
        private String fullName;
        private String remarks;

        public Table(String catalog, String schema, String tableName, String remarks) {
            this.catalog = catalog;
            this.schema = schema;
            this.tableName = tableName;
            this.remarks = remarks;
            fullName = composeFullyQualifiedTableName(catalog,
                    schema, tableName, '.');
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof org.mybatis.generator.internal.db.ActualTableName)) {
                return false;
            }
            return obj.toString().equals(this.toString());
        }

        @Override
        public int hashCode() {
            return fullName.hashCode();
        }

        @Override
        public String toString() {
            return fullName;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Column {
        protected String actualColumnName;
        protected int jdbcType;
        protected boolean nullable;
        protected int length;
        protected int scale;
        protected String remarks;
        protected String defaultValue;
        /**
         * true if the JDBC driver reports that this column is auto-increment
         */
        protected boolean isAutoIncrement;
        /**
         * true if the JDBC driver reports that this column is generated
         */
        protected boolean isGeneratedColumn;

        /**
         * Constructs a Column definition. This object holds all the information
         * about a column that is required to generate Java objects and SQL maps;
         */
        public Column() {
        }

    }
}
