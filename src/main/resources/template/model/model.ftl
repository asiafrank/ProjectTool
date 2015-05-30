package ${basePackageName}.${projectName}.model;

import java.io.Serializable;

public class ${modelClassSimpleName} implements Serializable {
    private static final long serialVersionUID = 1L;

    <#list propertyNameList as propertyName>
    private ${propertyTypeList[propertyName_index]} ${propertyName};
    </#list>

    <#list propertyNameList as propertyName>
    <#if (propertyName_index > 0)>

    </#if>
    public ${propertyTypeList[propertyName_index]} get${propertyName?cap_first}() {
        return ${propertyName};
    }

    public void set${propertyName?cap_first}(${propertyTypeList[propertyName_index]} ${propertyName}) {
        this.${propertyName} = ${propertyName};
    }
    </#list>
}
