package ${packageName}.core.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${packageName}.core.base.AbstractBO;
import ${packageName}.core.base.DAO;
import ${packageName}.core.bo.${modelClassSimpleName}BO;
import ${packageName}.core.dao.${modelClassSimpleName}DAO;
import ${packageName}.core.model.${modelClassSimpleName};
import ${packageName}.core.vo.${modelClassSimpleName}VO;

@Service("${modelClassSimpleName?uncap_first}BO")
public class Default${modelClassSimpleName}BO extends AbstractBO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> implements ${modelClassSimpleName}BO {
    @Autowired
    private ${modelClassSimpleName}DAO ${modelClassSimpleName?uncap_first}DAO;

    @Override
    protected DAO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> getDAO() {
        return ${modelClassSimpleName?uncap_first}DAO;
    }
}
