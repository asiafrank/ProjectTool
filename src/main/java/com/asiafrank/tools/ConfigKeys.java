package com.asiafrank.tools;

/**
 * 读取 config.properties 或 命令行传如的配置文件路径
 *
 * @author zhangxiaofan 2019/05/11-09:50
 */
public interface ConfigKeys {

    //----------------------------
    // 配置项的 key
    //----------------------------

    String  mysql_url = "mysql.url";
    String  mysql_username = "mysql.username";
    String  mysql_password = "mysql.password";
    String  mysql_database = "mysql.database";
    String  mysql_tables = "mysql.tables";
    String  mysql_driver = "mysql.driver";

    String  project_path = "project.path";
    String  java_src_relativePath = "java.src.relativePath";
    String  java_resources_relativePath = "java.resources.relativePath";

    String  base_module_dir = "base.module.dir";
    String  base_package = "base.package";

    String  bo_module_dir = "bo.module.dir";
    String  bo_package = "bo.package";
    String  bo_clazz_suffix = "bo.clazz.suffix";
    String  bo_impl_package = "bo.impl.package";
    String  bo_impl_clazz_suffix = "bo.impl.clazz.suffix";

    String  dao_module_dir = "dao.module.dir";
    String  dao_package = "dao.package";
    String  dao_clazz_suffix = "dao.clazz.suffix";
    String  dao_impl_package = "dao.impl.package";
    String  dao_impl_clazz_suffix = "dao.impl.clazz.suffix";

    String  model_module_dir = "model.module.dir";
    String  model_package = "model.package";
    String  model_clazz_suffix = "model.clazz.suffix";

    String  vo_module_dir = "vo.module.dir";
    String  vo_package = "vo.package";
    String  vo_clazz_suffix = "vo.clazz.suffix";

    String  mybatis_mapper_module_dir = "mybatis.mapper.module.dir";
    String  mybatis_mapper_dir = "mybatis.mapper.dir";
}
