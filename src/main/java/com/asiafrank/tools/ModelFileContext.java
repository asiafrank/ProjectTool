package com.asiafrank.tools;

/**
 * 生成 model 文件的上下文。
 * 包括生成 mapper
 *
 * @author zhangxiaofan 2019/05/14-16:48
 */
public class ModelFileContext extends FileContext {
    public ModelFileContext(String projectPath, String javaSrcRelativePath, String javaResourceRelativePath, String moduleDir, String packageName) {
        super(projectPath, javaSrcRelativePath, javaResourceRelativePath, moduleDir, packageName);
    }

    private String modelClassSimpleName;
    private String mapperSimpleName;
    private String voSimpleName;
    private String daoSimpleName;
    private String daoImplSimpleName;
    private String boSimpleName;
    private String boImplSimpleName;

    public String getModelClassSimpleName() {
        return modelClassSimpleName;
    }

    public void setModelClassSimpleName(String modelClassSimpleName) {
        this.modelClassSimpleName = modelClassSimpleName;
    }

    public String getMapperSimpleName() {
        return mapperSimpleName;
    }

    public void setMapperSimpleName(String mapperSimpleName) {
        this.mapperSimpleName = mapperSimpleName;
    }

    public String getVoSimpleName() {
        return voSimpleName;
    }

    public void setVoSimpleName(String voSimpleName) {
        this.voSimpleName = voSimpleName;
    }

    public String getDaoSimpleName() {
        return daoSimpleName;
    }

    public void setDaoSimpleName(String daoSimpleName) {
        this.daoSimpleName = daoSimpleName;
    }

    public String getDaoImplSimpleName() {
        return daoImplSimpleName;
    }

    public void setDaoImplSimpleName(String daoImplSimpleName) {
        this.daoImplSimpleName = daoImplSimpleName;
    }

    public String getBoSimpleName() {
        return boSimpleName;
    }

    public void setBoSimpleName(String boSimpleName) {
        this.boSimpleName = boSimpleName;
    }

    public String getBoImplSimpleName() {
        return boImplSimpleName;
    }

    public void setBoImplSimpleName(String boImplSimpleName) {
        this.boImplSimpleName = boImplSimpleName;
    }
}
