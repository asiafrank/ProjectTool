package ${basePackageName}.core.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${basePackageName}.core.base.AbstractBO;
import ${basePackageName}.core.base.DAO;
import ${basePackageName}.core.bo.${modelClassSimpleName}BO;
import ${basePackageName}.core.dao.${modelClassSimpleName}DAO;
import ${basePackageName}.core.model.${modelClassSimpleName};
import ${basePackageName}.core.vo.${modelClassSimpleName}VO;

@Service("${modelClassSimpleName?uncap_first}BO")
public class Default${modelClassSimpleName}BO extends AbstractBO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> implements ${modelClassSimpleName}BO {
    @Autowired
    private ${modelClassSimpleName}DAO ${modelClassSimpleName?uncap_first}DAO;

    @Override
    protected DAO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> getDAO() {
        return ${modelClassSimpleName?uncap_first}DAO;
    }
}
