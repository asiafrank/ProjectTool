package ${basePackageName}.core.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${basePackageName}.core.base.AbstractDAO;
import ${basePackageName}.core.dao.${modelClassSimpleName}DAO;
import ${basePackageName}.core.model.${modelClassSimpleName};
import ${basePackageName}.core.vo.${modelClassSimpleName}VO;

@Repository("${modelClassSimpleName?uncap_first}DAO")
public class MyBatis${modelClassSimpleName}DAO extends AbstractDAO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> implements ${modelClassSimpleName}DAO {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    protected SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Override
    protected String getNamespace() {
        return "${modelClassSimpleName?uncap_first}DAO";
    }
}
