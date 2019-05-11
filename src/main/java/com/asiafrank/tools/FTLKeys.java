package com.asiafrank.tools;

/**
 * FTL 配置。
 * 读取 ftl.properties 获得 ftl 文件路径
 *
 * @author zhangxiaofan 2019/05/11-09:35
 */
public interface FTLKeys {

    //----------------------------
    // 配置项的 key
    //----------------------------

    String base_AbstractBO  = "template.base.AbstractBO";
    String base_AbstractDAO = "template.base.AbstractDAO";

    String base_BO = "template.base.BO";
    String base_DAO = "template.base.DAO";
    String base_DAOUtils = "template.base.DAOUtils";
    String base_Expression = "template.base.Expression";
    String base_ExpressionChain = "template.base.ExpressionChain";
    String base_Expressions = "template.base.Expressions";
    String base_OrderBy = "template.base.OrderBy";
    String base_Page = "template.base.Page";
    String base_Pageable = "template.base.Pageable";

    String bo = "template.bo";
    String bo_impl = "template.bo.impl";
    String dao = "template.dao";
    String dao_impl = "template.dao.impl";

    String model = "template.model";
    String vo = "template.vo";

    String mybatis_mapper_mysql = "template.mybatis.mapper_mysql";
    String mybatis_mapper_postgresql = "template.mybatis.mapper_postgresql";
    String mybatis_mybatis_config = "template.mybatis.mybatis_config";

    String web_page_default = "template.web_page_default";
    String web_page_default_factory_provider = "template.web_page_default_factory_provider";
    String web_page_default_resolver = "template.web_page_default_resolver";

    String web_controller = "template.web.controller";
    String web_web_config = "template.web.web_config";

    String core_config = "template.core_config";
}
