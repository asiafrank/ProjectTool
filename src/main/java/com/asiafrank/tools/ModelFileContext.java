package com.asiafrank.tools;

/**
 * @author zhangxiaofan 2019/05/14-16:48
 */
public class ModelFileContext extends FileContext {
    public ModelFileContext(String projectPath, String javaSrcRelativePath, String javaResourceRelativePath, String moduleDir, String packageName) {
        super(projectPath, javaSrcRelativePath, javaResourceRelativePath, moduleDir, packageName);
    }

    private String modelClassSimpleName;

    public String getModelClassSimpleName() {
        return modelClassSimpleName;
    }

    public void setModelClassSimpleName(String modelClassSimpleName) {
        this.modelClassSimpleName = modelClassSimpleName;
    }
}
