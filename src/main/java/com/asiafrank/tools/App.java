package com.asiafrank.tools;

import com.asiafrank.tools.util.ColumnInfo;
import com.asiafrank.tools.util.JavaType;
import com.asiafrank.tools.util.MySQLTypeUtil;
import com.asiafrank.tools.util.TableInfo;
import com.mysql.cj.jdbc.MysqlDataSource;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * TODO: 如果 modifyTime 和 createTime 是 CURRENT_TIMESTAMP 修饰这无需 写入 更新语句
 * TODO: 抽离成 MapperGenerator，VOGenerator，ModelGenerator 等
 *
 * 操作步骤：
 * 1.去 start.spring.io 生成基础项目
 * 2.使用工具生成 BO，DAO，VO 及 mapper 文件
 * @author asiafrank created at 1/5/2017.
 */
public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            print("USAGE: jar project-gen.jar config.properties");
            return;
        }

        String propPath = args[0];
        App app = new App();
        app.exec(propPath);
    }

    private static void print(String s) {
        System.out.println(s);
    }

    private DataSource dataSource;

    private Configuration ftlConfig;

    private final Properties prop = new Properties();

    public void exec(String propPath) {
        prepare(propPath);
        generateFiles();
    }

    /**
     * 1.读取 classpath 中的 ftl.properties
     * 2.读取 classpath 中的 config-default.properties
     * 3.读取 propPath 的配置文件，将 1 与 2 步骤的配置覆盖为用户自定义的配置
     *
     * @param propPath 用户自定义的 properties
     */
    private void prepare(String propPath) {
        Objects.requireNonNull(propPath);
        if (propPath.isEmpty())
            throw new IllegalArgumentException("properties path is empty");

        ClassLoader cl = getClass().getClassLoader();

        try (
            InputStream is0 = cl.getResourceAsStream("ftl.properties");
            InputStream is1 = cl.getResourceAsStream("config-default.properties");
            InputStream is2 = new FileInputStream(propPath)
        ) {
            assert is0 != null;
            assert is1 != null;

            prop.load(is0);
            prop.load(is1);
            prop.load(is2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String driver = get(ConfigKeys.mysql_driver);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Could not load mysql driver class [" + driver + "]", ex);
        }

        MysqlDataSource ds = new MysqlDataSource();
        ds.setUrl(get(ConfigKeys.mysql_url));
        ds.setUser(get(ConfigKeys.mysql_username));
        ds.setPassword(get(ConfigKeys.mysql_password));
        this.dataSource = ds;

        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.ftlConfig = configuration;
    }

    private String projectPath;
    private String javaSrcRelativePath;
    private String javaResourceRelativePath;

    private String tablePrefix;

    /**
     * 生成文件
     * 1.生成 base 文件
     * 2.生成 vo，model 文件
     * 3.生成 mybatis mapper 文件
     * 4.生成 dao 文件
     * 5.生成 bo 文件
     */
    private void generateFiles() {
        projectPath = get(ConfigKeys.project_path);
        javaSrcRelativePath = get(ConfigKeys.java_src_relativePath);
        javaResourceRelativePath = get(ConfigKeys.java_resources_relativePath);

        // 1.
        if (getBoolean(ConfigKeys.baseGenerate)) {
            createBaseFiles(projectPath, javaSrcRelativePath);
        }

        // 2.,3.
        {
            ModelFileContext modelFileContext = new ModelFileContext(projectPath,
                                                      javaSrcRelativePath,
                                                      javaResourceRelativePath,
                                                      get(ConfigKeys.model_module_dir),
                                                      get(ConfigKeys.model_package));

            String tables = get(ConfigKeys.mysql_tables);
            String database = get(ConfigKeys.mysql_database);
            tablePrefix = get(ConfigKeys.mysql_tablePrefix);
            tablePrefix = Objects.isNull(tablePrefix) ? "" : tablePrefix;

            String[] tableNames = tables.split(",");
            List<TableInfo> tableInfoList = getTableInfo(tableNames, database, "", new String[] { "TABLE" });
            if (tableInfoList.size() > 0) {
                String dir = modelFileContext.getFileDir();
                checkAndCreateDir(dir);
                for (TableInfo t : tableInfoList) {

                    Map<Object, Object> context = getMySQLContext(modelFileContext, t);

                    if (getBoolean(ConfigKeys.modelGenerate))
                        createModel(modelFileContext, context);

                    if (getBoolean(ConfigKeys.voGenerate))
                        createVO(modelFileContext, context);

                    if (getBoolean(ConfigKeys.daoGenerate)) {
                        createDAO(modelFileContext, context);
                        createDAOImpl(modelFileContext, context);
                    }

                    if (getBoolean(ConfigKeys.mybatisMapperGenerate))
                        createMapper(modelFileContext, context);

                    if (getBoolean(ConfigKeys.boGenerate)) {
                        createBO(modelFileContext, context);
                        createBOImpl(modelFileContext, context);
                    }
                }
            } else {
                print("no table to create file");
            }
        }
    }

    /**
     * 生成 base 文件
     * @param projectPath
     * @param javaSrcRelativePath
     */
    private void createBaseFiles(String projectPath, String javaSrcRelativePath) {
        FileContext fileContext = new FileContext(projectPath,
                                                  javaSrcRelativePath,
                                                  javaResourceRelativePath,
                                                  get(ConfigKeys.base_module_dir),
                                                  get(ConfigKeys.base_package));

        String dir = fileContext.getFileDir();
        checkAndCreateDir(dir);

        Map<Object, Object> map = new HashMap<>();
        map.put("basePackage", fileContext.getPackageName());
        createFile(dir + "AbstractBO.java", get(FTLKeys.base_AbstractBO), map);
        createFile(dir + "AbstractDAO.java", get(FTLKeys.base_AbstractDAO), map);
        createFile(dir + "BO.java", get(FTLKeys.base_BO), map);
        createFile(dir + "DAO.java", get(FTLKeys.base_DAO), map);
        createFile(dir + "DAOUtils.java", get(FTLKeys.base_DAOUtils), map);
        createFile(dir + "Expression.java", get(FTLKeys.base_Expression), map);
        createFile(dir + "ExpressionChain.java", get(FTLKeys.base_ExpressionChain), map);
        createFile(dir + "Expressions.java", get(FTLKeys.base_Expressions), map);
        createFile(dir + "OrderBy.java", get(FTLKeys.base_OrderBy), map);
        createFile(dir + "Page.java", get(FTLKeys.base_Page), map);
        createFile(dir + "Pageable.java", get(FTLKeys.base_Pageable), map);
    }

    private void checkAndCreateDir(String dir) {
        Path p = Paths.get(dir);
        if (!Files.exists(p)) {
            File f = p.toFile();
            if (!f.mkdirs())
                throw new RuntimeException("mkdirs " + dir + " failed");
        }
    }

    /**
     * 生成 model 文件
     *
     * @param fileContext
     * @param context
     */
    private void createModel(ModelFileContext fileContext, Map<Object, Object> context) {
        String dir = fileContext.getFileDir();
        String javaName = fileContext.getModelClassSimpleName() + ".java";
        createFile(dir + javaName, get(FTLKeys.model), context);
    }

    /**
     * 生成 mapper xml 文件
     *
     * @param fileContext
     * @param context
     */
    private void createMapper(ModelFileContext fileContext, Map<Object, Object> context) {
        String moduleDir = get(ConfigKeys.mybatis_mapper_module_dir);
        String mapperDir = get(ConfigKeys.mybatis_mapper_dir);

        // {projectPath}/{moduleDir}{javaResourceRelativePath}/{mapperDir}/
        // 如: /Users/username/project/core/src/main/resources/mapper/
        String mapperFileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                                    projectPath,
                                                    moduleDir,
                                                    javaResourceRelativePath,
                                                    mapperDir);
        checkAndCreateDir(mapperFileDir);

        String mapperXmlName = fileContext.getMapperSimpleName() + ".xml";

        createFile(mapperFileDir + mapperXmlName, get(FTLKeys.mybatis_mapper_mysql), context);
    }

    /**
     * 生成 VO 文件
     *
     * @param modelFileContext
     * @param context
     */
    private void createVO(ModelFileContext modelFileContext, Map<Object, Object> context) {
        String moduleDir = get(ConfigKeys.vo_module_dir);
        String voPackage = get(ConfigKeys.vo_package);
        context.put("voPackage", voPackage); // 暂时覆盖, 以后再改
        context.put("voClassSimpleName", modelFileContext.getVoSimpleName());
        context.put("basePackage", get(ConfigKeys.base_package));
        context.put("modelPackage", get(ConfigKeys.model_package));

        String voPackageDir = voPackage.replace('.', '/');
        // {projectPath}/{moduleDir}{javaSrcRelativePath}/{packageDir}/
        // 如: /Users/username/project/core/src/main/java/com/demo/core/vo/
        String voFileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                                projectPath,
                                                moduleDir,
                                                javaSrcRelativePath,
                                                voPackageDir);

        checkAndCreateDir(voFileDir);

        String voName = modelFileContext.getVoSimpleName() + ".java";
        createFile(voFileDir + voName, get(FTLKeys.vo), context);
    }

    /**
     * 生成 DAO 文件
     *
     * @param modelFileContext
     * @param context
     */
    private void createDAO(ModelFileContext modelFileContext, Map<Object, Object> context) {
        String moduleDir = get(ConfigKeys.dao_module_dir);
        String daoPackage = get(ConfigKeys.dao_package);
        context.put("daoPackage", daoPackage); // 暂时覆盖, 以后再改
        context.put("daoClassSimpleName", modelFileContext.getDaoSimpleName());

        String daoPackageDir = daoPackage.replace('.', '/');
        // {projectPath}/{moduleDir}{javaSrcRelativePath}/{packageDir}/
        // 如: /Users/username/project/core/src/main/java/com/demo/core/dao/
        String daoFileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                                 projectPath,
                                                 moduleDir,
                                                 javaSrcRelativePath,
                                                 daoPackageDir);
        checkAndCreateDir(daoFileDir);
        String daoName = modelFileContext.getDaoSimpleName() + ".java";
        createFile(daoFileDir + daoName, get(FTLKeys.dao), context);
    }

    /**
     * 生成 DAOImpl
     *
     * @param modelFileContext
     * @param context
     */
    private void createDAOImpl(ModelFileContext modelFileContext, Map<Object, Object> context) {
        String moduleDir = get(ConfigKeys.dao_impl_module_dir);
        String daoImplPackage = get(ConfigKeys.dao_impl_package);

        context.put("daoImplPackage", daoImplPackage);
        context.put("daoImplClassSimpleName", modelFileContext.getDaoImplSimpleName());

        String daoImplPackageDir = daoImplPackage.replace('.', '/');
        // {projectPath}/{moduleDir}{javaSrcRelativePath}/{packageDir}/
        // 如: /Users/username/project/core/src/main/java/com/demo/core/dao/
        String daoImplFileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                                 projectPath,
                                                 moduleDir,
                                                 javaSrcRelativePath,
                                                 daoImplPackageDir);
        checkAndCreateDir(daoImplFileDir);
        String daoImplName = modelFileContext.getDaoImplSimpleName() + ".java";
        createFile(daoImplFileDir + daoImplName, get(FTLKeys.dao_impl), context);
    }

    private void createBO(ModelFileContext modelFileContext, Map<Object, Object> context) {
        String moduleDir = get(ConfigKeys.bo_module_dir);
        String boPackage = get(ConfigKeys.bo_package);

        context.put("boPackage", boPackage);
        context.put("boClassSimpleName", modelFileContext.getBoSimpleName());

        String boPackageDir = boPackage.replace(".", "/");
        // {projectPath}/{moduleDir}{javaSrcRelativePath}/{packageDir}/
        // 如: /Users/username/project/core/src/main/java/com/demo/core/bo/
        String boFileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                                     projectPath,
                                                     moduleDir,
                                                     javaSrcRelativePath,
                                                     boPackageDir);

        checkAndCreateDir(boFileDir);
        String boName = modelFileContext.getBoSimpleName() + ".java";
        createFile(boFileDir + boName, get(FTLKeys.bo), context);
    }

    private void createBOImpl(ModelFileContext modelFileContext, Map<Object, Object> context) {
        String moduleDir = get(ConfigKeys.bo_impl_module_dir);
        String boImplPackage = get(ConfigKeys.bo_impl_package);

        context.put("boImplPackage", boImplPackage);
        context.put("boImplClassSimpleName", modelFileContext.getBoImplSimpleName());

        String boImplPackageDir = boImplPackage.replace(".", "/");
        // {projectPath}/{moduleDir}{javaSrcRelativePath}/{packageDir}/
        // 如: /Users/username/project/core/src/main/java/com/demo/core/bo/
        String boImplFileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                                projectPath,
                                                moduleDir,
                                                javaSrcRelativePath,
                                                boImplPackageDir);
        checkAndCreateDir(boImplFileDir);
        String boImplName = modelFileContext.getBoImplSimpleName() + ".java";
        createFile(boImplFileDir + boImplName, get(FTLKeys.bo_impl), context);
    }

    private String get(String key) {
        return prop.getProperty(key);
    }

    private boolean getBoolean(String key) {
        String v = prop.getProperty(key);
        if (Objects.isNull(v))
            return false;
        return v.equalsIgnoreCase("true");
    }

    private void createFile(String filePath,
                    String ftlPath,
                    Map<Object, Object> context)
    {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            Template template = ftlConfig.getTemplate(ftlPath);
            template.process(context, out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------

    private Map<Object, Object> getMySQLContext(ModelFileContext fileContext, TableInfo tableInfo) {
        String packageName = fileContext.getPackageName();
        String tableName = tableInfo.getTableName().toLowerCase();

        String suffix = get(ConfigKeys.model_clazz_suffix);
        String modelClassSimpleName = getModelClassSimpleName(tableName, suffix);
        String mapperSimpleName = getModelClassSimpleName(tableName, "Mapper");

        String voSuffix = get(ConfigKeys.vo_clazz_suffix);
        String voSimpleName = getModelClassSimpleName(tableName, voSuffix);

        String daoSuffix = get(ConfigKeys.dao_clazz_suffix);
        String daoSimpleName = getModelClassSimpleName(tableName, daoSuffix);

        String daoImplSuffix = get(ConfigKeys.dao_impl_clazz_suffix);
        String daoImplSimpleName = getModelClassSimpleName(tableName, daoImplSuffix);

        String boSuffix = get(ConfigKeys.bo_clazz_suffix);
        String boSimpleName = getModelClassSimpleName(tableName, boSuffix);

        String boImplSuffix = get(ConfigKeys.bo_impl_clazz_suffix);
        String boImplSimpleName = getModelClassSimpleName(tableName, boImplSuffix);

        fileContext.setModelClassSimpleName(modelClassSimpleName);
        fileContext.setMapperSimpleName(mapperSimpleName);
        fileContext.setVoSimpleName(voSimpleName);
        fileContext.setDaoSimpleName(daoSimpleName);
        fileContext.setDaoImplSimpleName(daoImplSimpleName);
        fileContext.setBoSimpleName(boSimpleName);
        fileContext.setBoImplSimpleName(boImplSimpleName);

        Map<Object, Object> context = new HashMap<>();
        context.put("modelPackage", packageName);
        context.put("tableName", tableName);
        context.put("modelClassSimpleName", modelClassSimpleName);

        Set<String> importSet = new HashSet<>();
        List<String> propertyNameList = new ArrayList<>();
        List<String> propertyTypeList = new ArrayList<>();
        List<String> columnTypeList = new ArrayList<>();
        String pkType = "";
        Collection<ColumnInfo> columnInfoList = tableInfo.getColumnInfos();
        for (ColumnInfo columnInfo : columnInfoList) {
            String columnName = columnInfo.getColumnName();
            String columnTypeName = columnInfo.getColumnTypeName();

            print(columnInfo.getColumnName() + " " +
                          columnInfo.getColumnType() + " " +
                          columnInfo.getColumnTypeName() + " " +
                          columnInfo.getComment() + " " +
                          columnInfo.getLength() + " " +
                          columnInfo.getScale() + " " +
                          columnInfo.getPrecision());

            propertyNameList.add(getPropertyName(columnInfo.getColumnName()));

            JavaType t = MySQLTypeUtil.getJavaType(columnTypeName);
            columnTypeList.add(t.getJdbcTypeName());
            propertyTypeList.add(t.getJavaSimpleName());
            importSet.add(t.getJavaTypeName());

            if ("id".equalsIgnoreCase(columnName)) {
                pkType = t.getJavaSimpleName();
                importSet.add(t.getJavaTypeName());
            }
        }
        context.put("importList", importSet.stream().sorted().toArray());
        context.put("propertyNameList", propertyNameList);
        context.put("propertyTypeList", propertyTypeList);
        context.put("columnTypeList", columnTypeList);
        context.put("columnInfoList", columnInfoList);
        context.put("pkType", pkType);
        return context;
    }

    private String getModelClassSimpleName(String tableName, String suffix) {
        String name = tableName.toLowerCase();
        if (!tablePrefix.isEmpty() && name.startsWith(tablePrefix)) {
            name = name.substring(tablePrefix.length());
        }
        if (name.contains("_")) {
            String[] ss = name.split("_");
            StringBuilder sb = new StringBuilder();
            for (String s : ss) {
                sb.append(capitalize(s));
            }
            sb.append(suffix);
            return sb.toString();
        } else {
            return capitalize(name) + suffix;
        }
    }

    public List<TableInfo> getTableInfo(String[] tableNames, String catalog, String schema, String[] types) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            if (Objects.isNull(types) || types.length == 0) {
                types = new String[] { "TABLE" };
            }
            DatabaseMetaData dbMeta = connection.getMetaData();
            if (Objects.isNull(tableNames)) {
                try (ResultSet rs = connection.getMetaData().getTables(catalog, schema, "%", types)) {
                    fillTableInfo(tableInfoList, catalog, schema, dbMeta, rs);
                }
            } else {
                for (String tableName : tableNames) {
                    try (ResultSet rs = connection.getMetaData().getTables(catalog, schema, tableName, types)) {
                        fillTableInfo(tableInfoList, catalog, schema, dbMeta, rs);
                    }
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
            print("Getting information for table [" + tableInfo.getTableName() + "] .");
            try (ResultSet colRet = dbMeta.getColumns(catalog, schema, tableInfo.getTableName(), "%");
                 ResultSet pkRet = dbMeta.getPrimaryKeys(catalog, schema, tableInfo.getTableName())) {
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
                    // columnInfo.setComment((Objects.isNull(colRet.getString("REMARKS"))) ? .getColumnComment(tableInfo.getTableName(), columnInfo.getColumnName()) : colRet.getString("REMARKS"));
                    columnInfo.setLength(colRet.getInt("COLUMN_SIZE"));
                    columnInfo.setPrecision(columnInfo.getLength());
                    columnInfo.setScale(colRet.getInt("DECIMAL_DIGITS"));
                    columnInfo.setNullable("YES".equals(colRet.getString("IS_NULLABLE")));
                    columnInfo.setPrimaryKey(primaryKeyMap.containsKey(columnInfo.getColumnName()));
                    tableInfo.addColumnInfo(columnInfo.getColumnName(), columnInfo);
                }
            }
            tableInfoList.add(tableInfo);
        }
    }

    private static String getPropertyName(String columnName) {
        String name = columnName;

        if (name.contains("_")) {
            name = name.toLowerCase();
            String[] ss = name.split("_");
            StringBuilder sb = new StringBuilder();
            for (String s : ss) {
                sb.append(capitalize(s));
            }
            return uncapitalize(sb.toString());
        } else {
            return name;
        }
    }

    public static String capitalize(String str) {
        char[] arr = str.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return String.copyValueOf(arr);
    }

    public static String uncapitalize(String str) {
        char[] arr = str.toCharArray();
        arr[0] = Character.toLowerCase(arr[0]);
        return String.copyValueOf(arr);
    }
}
