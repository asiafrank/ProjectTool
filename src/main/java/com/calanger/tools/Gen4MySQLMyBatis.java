package com.calanger.tools;

import com.calanger.tools.util.DirUtils;
import com.calanger.tools.util.GenerateUtils;
import com.raddle.jdbc.JdbcTemplate;
import com.raddle.jdbc.callback.ConnectionCallback;
import com.raddle.jdbc.datasource.DriverManagerDataSource;
import com.raddle.jdbc.meta.table.ColumnInfo;
import com.raddle.jdbc.meta.table.TableInfo;
import com.raddle.jdbc.meta.table.TableMetaHelper;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Gen4MySQLMyBatis {
    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/asiafrank?useUnicode=true&characterEncoding=UTF-8");
            dataSource.setUsername("root");
            dataSource.setPassword("root");

            jdbcTemplate = new JdbcTemplate(dataSource);
        }

        return jdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        final String baseDirName = "E:/workspace/frank";
        final String basePackageName = "com.calanger";
        final String projectName = "blog";
        final String tablePrefix = "";
        final String[] tableNames = {
//                "ot_stock_index"
                "article",
                "user"
        };
        final String schema = "fcj";

        String coreBaseDirName = baseDirName + "/" + projectName + "-core";

        String sqlMapDirName = coreBaseDirName + "/src/main/resources/mybatis/base";

        String javaBaseDirName = getJavaBaseDirName(coreBaseDirName, basePackageName, projectName);

        String modelDirName = javaBaseDirName + "/model";
        DirUtils.mkdir(modelDirName);

        String voDirName = javaBaseDirName + "/vo";
        DirUtils.mkdir(voDirName);

        String daoDirName = javaBaseDirName + "/dao";
        DirUtils.mkdir(daoDirName);

        String daoImplDirName = javaBaseDirName + "/dao/impl";
        DirUtils.mkdir(daoImplDirName);

        String boDirName = javaBaseDirName + "/bo";
        DirUtils.mkdir(boDirName);

        String boImplDirName = javaBaseDirName + "/bo/impl";
        DirUtils.mkdir(boImplDirName);

        List<TableInfo> tableInfoList = (List<TableInfo>) getJdbcTemplate().execute(new ConnectionCallback() {
            @Override
            public Object doInConnection(Connection connection) throws SQLException {
                TableMetaHelper metaHelper = new TableMetaHelper(connection);
                return metaHelper.getTableInfo(tableNames, schema, new String[] { "TABLE" });
            }
        });

        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateLoader(new ClassTemplateLoader(Gen4MySQLMyBatis.class, "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        for (TableInfo tableInfo : tableInfoList) {
            String tableName = tableInfo.getTableName().toLowerCase();

            String sqlMapPathname = sqlMapDirName + "/mapper-" + tableName + ".xml";
            String modelPathname = modelDirName + "/" + getModelClassSimpleName(tableName, tablePrefix) + ".java";
            String voPathname = voDirName + "/" + getModelClassSimpleName(tableName, tablePrefix) + "VO.java";
            String daoPathname = daoDirName + "/" + getModelClassSimpleName(tableName, tablePrefix) + "DAO.java";
            String daoImplPathname = daoImplDirName + "/MyBatis" + getModelClassSimpleName(tableName, tablePrefix) + "DAO.java";
            String boPathname = boDirName + "/" + getModelClassSimpleName(tableName, tablePrefix) + "BO.java";
            String boImplPathname = boImplDirName + "/Default" + getModelClassSimpleName(tableName, tablePrefix) + "BO.java";

            Map<Object, Object> context = getContext(basePackageName, projectName, tablePrefix, tableInfo);

            GenerateUtils.generateFile(sqlMapPathname, "template/mybatis/mapper-mysql.ftl", configuration, context);
            GenerateUtils.generateFile(modelPathname, "template/model/model.ftl", configuration, context);
            GenerateUtils.generateFile(voPathname, "template/vo/vo.ftl", configuration, context);
            GenerateUtils.generateFile(daoPathname, "template/dao/dao.ftl", configuration, context);
            GenerateUtils.generateFile(daoImplPathname, "template/mybatis/dao/dao-impl.ftl", configuration, context);
            GenerateUtils.generateFile(boPathname, "template/bo/bo.ftl", configuration, context);
            GenerateUtils.generateFile(boImplPathname, "template/bo/bo-impl.ftl", configuration, context);
        }
    }

    private static String getJavaBaseDirName(String baseDirName, String basePackageName, String projectName) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseDirName);
        sb.append("/src/main/java/");
        sb.append(basePackageName.replace('.', '/'));
        sb.append("/");
        sb.append(projectName);
        return sb.toString();
    }

    private static Map<Object, Object> getContext(String basePackageName, String projectName, String tablePrefix, TableInfo tableInfo) {
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

            if ("char".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("CHAR");
            } else if ("varchar".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            } else if ("tinytext".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            }  else if ("text".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            }  else if ("mediumtext".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            }  else if ("longtext".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            }  else if ("decimal".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("DECIMAL");
            } else if ("date".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("DATE");
            } else if ("datetime".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("TIMESTAMP");
            } else if ("bit".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("tinyint".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("smallint".equalsIgnoreCase(columnTypeName)
                    || "tinyint unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("mediumint".equalsIgnoreCase(columnTypeName)
                    || "smallint unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("int".equalsIgnoreCase(columnTypeName)
                    || "mediumint unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("bigint".equalsIgnoreCase(columnTypeName)
                    || "bigint unsigned".equalsIgnoreCase(columnTypeName)
                    || "int unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("BIGINT");
            }  else {
                columnTypeList.add("UnknownType" + "[" + columnTypeName + "]");
            }

            if ("bit".equalsIgnoreCase(columnTypeName)
                    || "tinyint".equalsIgnoreCase(columnTypeName)
                    || "tinyint unsigned".equalsIgnoreCase(columnTypeName)
                    || "smallint".equalsIgnoreCase(columnTypeName)
                    || "smallint unsigned".equalsIgnoreCase(columnTypeName)
                    || "mediumint".equalsIgnoreCase(columnTypeName)
                    || "mediumint unsigned".equalsIgnoreCase(columnTypeName)
                    || "int".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.lang.Integer");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.Integer";
                }
            } else if ("bigint".equalsIgnoreCase(columnTypeName)
                    || "int unsigned".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.lang.Long");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.Long";
                }
            } else if ("char".equalsIgnoreCase(columnTypeName)
                    || "varchar".equalsIgnoreCase(columnTypeName)
                    || "tinytext".equalsIgnoreCase(columnTypeName)
                    || "text".equalsIgnoreCase(columnTypeName)
                    || "mediumtext".equalsIgnoreCase(columnTypeName)
                    || "longtext".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.lang.String");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.String";
                }
            } else if ("date".equalsIgnoreCase(columnTypeName)
                    || "datetime".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.util.Date");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.util.Date";
                }
            } else if ("decimal".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.math.BigDecimal");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.math.BigDecimal";
                }
            } else {
                propertyTypeList.add("UnknownType[" + columnTypeName + "]");
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
            throw new RuntimeException("表名前缀不正确");
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
}
