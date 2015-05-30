package ${basePackageName}.${projectName}.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${basePackageName}.common.bo.AbstractBO;
import ${basePackageName}.common.dao.DAO;
import ${basePackageName}.${projectName}.bo.${modelClassSimpleName}BO;
import ${basePackageName}.${projectName}.dao.${modelClassSimpleName}DAO;
import ${basePackageName}.${projectName}.model.${modelClassSimpleName};
import ${basePackageName}.${projectName}.vo.${modelClassSimpleName}VO;

@Service("${modelClassSimpleName?uncap_first}BO")
public class Default${modelClassSimpleName}BO extends AbstractBO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> implements ${modelClassSimpleName}BO {
    @Autowired
    private ${modelClassSimpleName}DAO ${modelClassSimpleName?uncap_first}DAO;

    @Override
    protected DAO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> getDAO() {
        return ${modelClassSimpleName?uncap_first}DAO;
    }
}
