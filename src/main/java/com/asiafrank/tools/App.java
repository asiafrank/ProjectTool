package com.asiafrank.tools;

import com.asiafrank.tools.core.CoreGenerator;
import com.mysql.cj.jdbc.MysqlDataSource;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * TODO: 1. properties 写配置映射
 *       2. BO,DAO,VO 等后缀可以自定义
 *       3. JDBC type 映射写配置里；
 *          JDBC 连接配置写 properties;
 *          ftl 文件映射也写配置文件里
 *       4. 仅生成 BO，DAO，VO 的文件。
 *       spring-boot 基础项目 统一使用 start.spring.io 来生成
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

    /**
     * 生成文件
     * 1.生成 base 文件
     * 2.生成 bo 文件
     * 3.生成 dao 文件
     * 4.生成 vo，model 文件
     * 5.生成 mybatis mapper 文件
     */
    private void generateFiles() {
        String projectPath = get(ConfigKeys.project_path);
        String javaSrcRelativePath = get(ConfigKeys.java_src_relativePath);
        String javaResourceRelativePath = get(ConfigKeys.java_resources_relativePath);

        String baseModuleDir = get(ConfigKeys.base_module_dir);
        String basePackage = get(ConfigKeys.base_package);
        String basePackageDir = basePackage.replace('.', '/');

        // {projectPath}/{baseModuleDir}{javaSrcRelativePath}/{basePackageDir}
        // 如: /Users/username/project/core/com/demo/core/base
        StringBuilder baseDirSB = new StringBuilder();
        baseDirSB.append(projectPath)
                 .append("/")
                 .append(baseModuleDir)
                 .append(javaSrcRelativePath)
                 .append("/")
                 .append(basePackageDir);

        String baseDir = baseDirSB.toString();
    }

    private String get(String key) {
        return prop.getProperty(key);
    }
}
