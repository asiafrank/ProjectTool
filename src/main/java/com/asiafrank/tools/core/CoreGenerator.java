package com.asiafrank.tools.core;

import com.asiafrank.tools.ProjectInfo;
import com.asiafrank.tools.util.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang.StringUtils;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author asiafrank created at 1/5/2017.
 */
public class CoreGenerator extends Generator {
    private static final Logger log = Logger.getGlobal();

    private final char sp;

    private final ProjectInfo project;

    private final DBParam param;

    private final DataSource dataSource;

    private final Configuration ftlConfig;

    public CoreGenerator(ProjectInfo projectInfo, DBParam param, Configuration config) {
        this.project = projectInfo;
        this.param = param;

        if (DB.MYSQL == param.getDb()) {
            try {
                Class.forName(param.getDriver());
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException("Could not load mysql driver class [" + param.getDriver() + "]", ex);
            }

            MysqlDataSource ds = new MysqlDataSource();
            ds.setUrl(param.getUrl());
            ds.setUser(param.getUsername());
            ds.setPassword(param.getPassword());
            this.dataSource = ds;
        } else if (DB.POSTGRESQL == param.getDb()) {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setUrl(param.getUrl());
            ds.setUser(param.getUsername());
            ds.setPassword(param.getPassword());
            this.dataSource = ds;
        } else {
            throw new IllegalArgumentException("DB not support");
        }

        if (Objects.isNull(config)) {
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateLoader(new ClassTemplateLoader(CoreGenerator.class, "/"));
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            this.ftlConfig = configuration;
        } else {
            this.ftlConfig = config;
        }
        this.sp = projectInfo.getSp();
    }

    public CoreGenerator(ProjectInfo project, DBParam param) {
        this(project, param, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() {
        mkCoreDir();
        mkMybatisConfig();

        final String[] tableNames = param.getTableNames();
        final String schema = param.getSchema();
        final String catalog = param.getDatabase();
        List<TableInfo> tableInfoList = getTableInfo(tableNames, catalog, schema, new String[] { "TABLE" });

        if (DB.MYSQL == param.getDb())
            mkMySQLModel(tableInfoList);
        else
            mkPostgreSQLModel(tableInfoList);
    }

    private void mkMySQLModel(List<TableInfo> tableInfoList) {
        final String tablePrefix = param.getTablePrefix();
        for (TableInfo info : tableInfoList) {
            String tableName = info.getTableName().toLowerCase();
            Map<Object, Object> context = getMySQLContext(info);

            genFiles(tablePrefix, tableName, context);
        }
    }

    private void mkPostgreSQLModel(List<TableInfo> tableInfoList) {
        final String tablePrefix = param.getTablePrefix();
        for (TableInfo info : tableInfoList) {
            String tableName = info.getTableName().toLowerCase();
            Map<Object, Object> context = getPostgreSQLContext(info);

            genFiles(tablePrefix, tableName, context);
        }
    }

    private void genFiles(String tablePrefix, String tableName, Map<Object, Object> context) {
        String mapper = project.getCoreMybatisMapperDir() + sp + tableName + ".xml";
        String model = project.getCoreModelDir() + sp + getModelClassSimpleName(tableName, tablePrefix) + ".java";
        String vo = project.getCoreVODir() + sp + getModelClassSimpleName(tableName, tablePrefix) + "VO.java";
        String dao = project.getCoreDAODir() + sp + getModelClassSimpleName(tableName, tablePrefix) + "DAO.java";
        String daoImpl = project.getCoreDAOImplDir() + sp + "MyBatis" + getModelClassSimpleName(tableName, tablePrefix) + "DAO.java";
        String bo = project.getCoreBODir() + sp + getModelClassSimpleName(tableName, tablePrefix) + "BO.java";
        String boImpl = project.getCoreBOImplDir() + sp + "Default" + getModelClassSimpleName(tableName, tablePrefix) + "BO.java";

        gen(mapper, FTLs.mybatis_mapper_mysql, ftlConfig, context);
        gen(model, FTLs.model_model, ftlConfig, context);
        gen(vo, FTLs.vo_vo, ftlConfig, context);
        gen(dao, FTLs.dao_dao, ftlConfig, context);
        gen(daoImpl, FTLs.mybatis_dao_dao_impl, ftlConfig, context);
        gen(bo, FTLs.bo_bo, ftlConfig, context);
        gen(boImpl, FTLs.bo_bo_impl, ftlConfig, context);
    }

    private void mkMybatisConfig() {
        gen(project.getCoreResourcesDir() + sp + "mybatis-config.xml", FTLs.mybatis_mybatis_config,
                ftlConfig, new HashMap<>());
    }

    private void mkCoreDir() {
        mkdir(project.getCoreModelDir());
        mkdir(project.getCoreVODir());
        mkdir(project.getCoreDAODir());
        mkdir(project.getCoreDAOImplDir());
        mkdir(project.getCoreBODir());
        mkdir(project.getCoreBOImplDir());
        mkdir(project.getCoreMybatisMapperDir());
        mkdir(project.getCoreMybatisMapperExtensionDir());
    }

    //    https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
    private Map<Object, Object> getMySQLContext(TableInfo tableInfo) {
        final String basePackageName = project.getPackageName();
        final String projectName = project.getProjectName();
        final String tablePrefix = param.getTablePrefix();
        String tableName = tableInfo.getTableName().toLowerCase();

        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put("basePackageName", basePackageName);
        context.put("projectName", projectName);
        context.put("tableName", tableName);
        context.put("modelClassSimpleName", getModelClassSimpleName(tableName, tablePrefix));

        List<String> propertyNameList = new ArrayList<String>();
        List<String> propertyTypeList = new ArrayList<String>();
        List<String> columnTypeList = new ArrayList<String>();
        String pkType = "";
        Collection<ColumnInfo> columnInfoList = tableInfo.getColumnInfos();
        for (ColumnInfo columnInfo : columnInfoList) {
            String columnName = columnInfo.getColumnName();
            String columnTypeName = columnInfo.getColumnTypeName();

            System.out.println(columnInfo.getColumnName());
            System.out.println(columnInfo.getColumnType());
            System.out.println(columnInfo.getColumnTypeName());
            System.out.println(columnInfo.getComment());
            System.out.println(columnInfo.getLength());
            System.out.println(columnInfo.getScale());
            System.out.println(columnInfo.getPrecision());

            propertyNameList.add(getPropertyName(columnInfo.getColumnName()));

            JavaType t = MySQLTypeUtil.getJavaType(columnTypeName);
            columnTypeList.add(t.getJdbcTypeName());
            propertyTypeList.add(t.getJavaTypeName());

            if ("id".equalsIgnoreCase(columnName)) {
                pkType = t.getJavaTypeName();
            }
        }
        context.put("propertyNameList", propertyNameList);
        context.put("propertyTypeList", propertyTypeList);
        context.put("columnTypeList", columnTypeList);
        context.put("columnInfoList", columnInfoList);
        context.put("pkType", pkType);

        return context;
    }

    private Map<Object, Object> getPostgreSQLContext(TableInfo tableInfo) {
        final String basePackageName = project.getPackageName();
        final String projectName = project.getProjectName();
        final String tablePrefix = param.getTablePrefix();
        String tableName = tableInfo.getTableName().toLowerCase();

        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put("basePackageName", basePackageName);
        context.put("projectName", projectName);
        context.put("tableName", tableName);
        context.put("modelClassSimpleName", getModelClassSimpleName(tableName, tablePrefix));

        List<String> propertyNameList = new ArrayList<String>();
        List<String> propertyTypeList = new ArrayList<String>();
        List<String> columnTypeList = new ArrayList<String>();
        String pkType = "";
        Collection<ColumnInfo> columnInfoList = tableInfo.getColumnInfos();
        for (ColumnInfo columnInfo : columnInfoList) {
            String columnName = columnInfo.getColumnName();
            String columnTypeName = columnInfo.getColumnTypeName();

            System.out.println(columnInfo.getColumnName());
            System.out.println(columnInfo.getColumnType());
            System.out.println(columnInfo.getColumnTypeName());
            System.out.println(columnInfo.getComment());
            System.out.println(columnInfo.getLength());
            System.out.println(columnInfo.getScale());
            System.out.println(columnInfo.getPrecision());

            propertyNameList.add(getPropertyName(columnInfo.getColumnName()));

            JavaType t = PostgreSQLTypeUtil.getJavaType(columnTypeName);
            columnTypeList.add(t.getJdbcTypeName());
            propertyTypeList.add(t.getJavaTypeName());

            if ("id".equalsIgnoreCase(columnName)) {
                pkType = t.getJavaTypeName();
            }
        }
        context.put("propertyNameList", propertyNameList);
        context.put("propertyTypeList", propertyTypeList);
        context.put("columnTypeList", columnTypeList);
        context.put("columnInfoList", columnInfoList);
        context.put("pkType", pkType);

        return context;
    }

    private static String getModelClassSimpleName(String tableName, String tablePrefix) {
        return getClassSimpleName(tableName, tablePrefix);
    }

    private static String getClassSimpleName(String tableName, String tablePrefix) {
        if (tablePrefix == null) {
            tablePrefix = "";
        }

        String name = tableName.toLowerCase();
        String prefix = tablePrefix.toLowerCase();

        if (!name.startsWith(prefix)) {
            throw new RuntimeException("prefix of table incorrect");
        }

        name = name.substring(prefix.length());
        String[] ss = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(StringUtils.capitalize(s));
        }
        return sb.toString();
    }

    private static String getPropertyName(String columnName) {
        String name = columnName.toLowerCase();

        String[] ss = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(StringUtils.capitalize(s));
        }
        return StringUtils.uncapitalize(sb.toString());
    }

    public List<TableInfo> getTableInfo(String[] tableNames, String catalog, String schema, String[] types) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
        try {
            if (Objects.isNull(types) || types.length == 0) {
                types = new String[] { "TABLE" };
            }
            DatabaseMetaData dbMeta = connection.getMetaData();
            if (Objects.isNull(tableNames)) {
                ResultSet rs = connection.getMetaData().getTables(catalog, schema, "%", types);
                fillTableInfo(tableInfoList, catalog, schema, dbMeta, rs);
                CloseUtil.close(rs);
            } else {
                for (String tableName : tableNames) {
                    ResultSet rs = connection.getMetaData().getTables(catalog, schema, tableName, types);
                    fillTableInfo(tableInfoList, catalog, schema, dbMeta, rs);
                    CloseUtil.close(rs);
                }
            }
            tableInfoList.sort((o1, o2) -> {
                if (o1 == null || o1.getTableName() == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                return o1.getTableName().compareTo(o2.getTableName());
            });
            return tableInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillTableInfo(List<TableInfo> tableInfoList, String catalog, String schema, DatabaseMetaData dbMeta, ResultSet rs) throws SQLException {
        while (rs.next()) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTableName(rs.getString("TABLE_NAME"));
            log.info("Getting information for table [" + tableInfo.getTableName() + "] .");
            ResultSet colRet = dbMeta.getColumns(catalog, schema, tableInfo.getTableName(), "%");
            ResultSet pkRet = dbMeta.getPrimaryKeys(catalog, schema, tableInfo.getTableName());
            Map<String, Object> primaryKeyMap = new HashMap<String, Object>();
            while (pkRet.next()) {
                primaryKeyMap.put(pkRet.getString("COLUMN_NAME"), null);
            }

            while (colRet.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setTableName(tableInfo.getTableName());
                columnInfo.setColumnName(colRet.getString("COLUMN_NAME"));
                columnInfo.setColumnType(colRet.getInt("DATA_TYPE"));
                columnInfo.setColumnTypeName(colRet.getString("TYPE_NAME"));
//                columnInfo.setComment((Objects.isNull(colRet.getString("REMARKS"))) ? .getColumnComment(tableInfo.getTableName(), columnInfo.getColumnName()) : colRet.getString("REMARKS"));
                columnInfo.setLength(colRet.getInt("COLUMN_SIZE"));
                columnInfo.setPrecision(columnInfo.getLength());
                columnInfo.setScale(colRet.getInt("DECIMAL_DIGITS"));
                columnInfo.setNullable("YES".equals(colRet.getString("IS_NULLABLE")));
                columnInfo.setPrimaryKey(primaryKeyMap.containsKey(columnInfo.getColumnName()));
                tableInfo.addColumnInfo(columnInfo.getColumnName(), columnInfo);
            }
            CloseUtil.close(colRet);
            CloseUtil.close(pkRet);
            tableInfoList.add(tableInfo);
        }
    }

    public ProjectInfo getProject() {
        return project;
    }

    public DBParam getParam() {
        return param;
    }

    public Configuration getFtlConfig() {
        return ftlConfig;
    }
}
