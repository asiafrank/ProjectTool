package com.asiafrank.tools;

import com.mysql.cj.core.MysqlType;
import com.raddle.jdbc.JdbcTemplate;
import com.raddle.jdbc.datasource.DriverManagerDataSource;
import com.raddle.jdbc.meta.table.ColumnInfo;
import com.raddle.jdbc.meta.table.TableInfo;
import com.raddle.jdbc.meta.table.TableMetaHelper;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.JDBCType;
import java.util.*;

/**
 * @author asiafrank created at 1/5/2017.
 */
public class CoreGenerator extends Generator {
    private final char sp;

    private final ProjectInfo project;

    private final DBParam param;

    private final JdbcTemplate jdbcTemplate;

    private final Configuration ftlConfig;

    public CoreGenerator(ProjectInfo projectInfo, DBParam param, Configuration config) {
        this.project = projectInfo;
        this.param = param;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(param.getDriver());
        dataSource.setUrl(param.getUrl());
        dataSource.setUsername(param.getUsername());
        dataSource.setPassword(param.getPassword());

        jdbcTemplate = new JdbcTemplate(dataSource);

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
    public void exec() {
        mkCoreDir();
        mkMybatisConfig();

        final String[] tableNames = param.getTableNames();
        final String schema = param.getSchema();
        List<TableInfo> tableInfoList = (List<TableInfo>) getJdbcTemplate().execute((conn)->{
            TableMetaHelper metaHelper = new TableMetaHelper(conn);
            return metaHelper.getTableInfo(tableNames, schema, new String[] { "TABLE" });
        });

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
    }

    private void mkPostgreSQLModel(List<TableInfo> tableInfoList) {
        final String tablePrefix = param.getTablePrefix();
        for (TableInfo info : tableInfoList) {
            String tableName = info.getTableName().toLowerCase();
            Map<Object, Object> context = getPostgreSQLContext(info);

            String mapper = project.getCoreMybatisMapperDir() + sp + tableName + ".xml";
            String model = project.getCoreModelDir() + sp + getModelClassSimpleName(tableName, tablePrefix) + ".java";
            String vo = project.getCoreVODir() + sp + getModelClassSimpleName(tableName, tablePrefix) + "VO.java";
            String dao = project.getCoreDAODir() + sp + getModelClassSimpleName(tableName, tablePrefix) + "DAO.java";
            String daoImpl = project.getCoreDAOImplDir() + sp + "MyBatis" + getModelClassSimpleName(tableName, tablePrefix) + "DAO.java";
            String bo = project.getCoreBODir() + sp + getModelClassSimpleName(tableName, tablePrefix) + "BO.java";
            String boImpl = project.getCoreBOImplDir() + sp + "Default" + getModelClassSimpleName(tableName, tablePrefix) + "BO.java";

            gen( mapper, FTLs.mybatis_mapper_postgresql, ftlConfig, context);
        }
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

            // use com.mysql.cj.core.MySqlType JDBCType Types
            MysqlType mysqlType = MysqlType.getByName(columnTypeName);
            int jdbcType = mysqlType.getJdbcType();
            JDBCType t = JDBCType.valueOf(jdbcType);
            columnTypeList.add(t.getName());
            propertyTypeList.add(mysqlType.getClassName());

            if ("id".equalsIgnoreCase(columnName)) {
                pkType = "java.lang.Integer";
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
        // TODO: use TypeInfoCache JDBCType Types
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

            // use com.mysql.cj.core.MySqlType JDBCType Types
            MysqlType mysqlType = MysqlType.getByName(columnTypeName);
            int jdbcType = mysqlType.getJdbcType();
            JDBCType t = JDBCType.valueOf(jdbcType);
            columnTypeList.add(t.getName());
            propertyTypeList.add(mysqlType.getClassName());

            if ("id".equalsIgnoreCase(columnName)) {
                pkType = "java.lang.Integer";
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

    public ProjectInfo getProject() {
        return project;
    }

    public DBParam getParam() {
        return param;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Configuration getFtlConfig() {
        return ftlConfig;
    }
}
