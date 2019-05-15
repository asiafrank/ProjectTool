package ${daoImplPackage};

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${basePackage}.AbstractDAO;
import ${daoPackage}.${daoClassSimpleName};
import ${modelPackage}.${modelClassSimpleName};
import ${voPackage}.${voClassSimpleName};

@Repository("${daoClassSimpleName?uncap_first}")
public class ${daoImplClassSimpleName} extends AbstractDAO<${modelClassSimpleName}, ${voClassSimpleName}, ${pkType}> implements ${daoClassSimpleName} {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    protected SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Override
    protected String getNamespace() {
        return "${daoClassSimpleName?uncap_first}";
    }
}
