package com.asiafrank.tools;

import com.raddle.jdbc.JdbcTemplate;
import com.raddle.jdbc.datasource.DriverManagerDataSource;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.util.Objects;

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
    public void exec() {
        mkCoreDir();

        final String[] tableNames = param.getTableNames();
    }

    private void mkCoreDir() {
        mkdir(project.getCoreModelDir());
        mkdir(project.getCoreVODir());
        mkdir(project.getCoreDAODir());
        mkdir(project.getCoreDAOImplDir());
        mkdir(project.getCoreBODir());
        mkdir(project.getCoreBOImplDir());
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
