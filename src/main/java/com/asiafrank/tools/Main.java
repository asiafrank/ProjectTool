package com.asiafrank.tools;

import com.asiafrank.tools.core.CoreGenerator;
import com.asiafrank.tools.core.DBParam;
import com.asiafrank.tools.util.DB;
import com.asiafrank.tools.util.ProjectInfo;

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
public class Main {
    public static void main(String[] args) {
        ProjectInfo info = new ProjectInfo("/Users/asiafrank/Downloads",
                "demo", "com.example.demo");
        DBParam dbParam = new DBParam(
                "test",
                "asiafrank",
                "123456", DB.MYSQL, "", "",
                new String[]{"sample"});
        CoreGenerator cg = new CoreGenerator(info, dbParam);
        cg.execute();
    }
}
