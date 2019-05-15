package ${boImplPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${basePackage}.AbstractBO;
import ${basePackage}.DAO;
import ${boPackage}.${boClassSimpleName};
import ${daoPackage}.${daoClassSimpleName};
import ${modelPackage}.${modelClassSimpleName};
import ${voPackage}.${voClassSimpleName};

@Service("${boClassSimpleName?uncap_first}")
public class ${boImplClassSimpleName} extends AbstractBO<${modelClassSimpleName}, ${voClassSimpleName}, ${pkType}> implements ${boClassSimpleName} {
    @Autowired
    private ${daoClassSimpleName} ${daoClassSimpleName?uncap_first};

    @Override
    protected DAO<${modelClassSimpleName}, ${voClassSimpleName}, ${pkType}> getDAO() {
        return ${daoClassSimpleName?uncap_first};
    }
}
