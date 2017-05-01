package com.asiafrank.tools;

import java.io.File;

/**
 * @author asiafrank created at 1/5/2017.
 */
public class ProjectInfo {

    private final char sp = File.separatorChar;

    private final char dot = '.';

    /**
     * generate path
     */
    private final String path;

    private final String projectName;

    private final String packageName;

    private final String baseDir;

    private final String coreBaseDir;
    private final String corePackageDir;
    private final String coreResourcesDir;

    private final String coreModelDir;
    private final String coreVODir;
    private final String coreDAODir;
    private final String coreDAOImplDir;
    private final String coreBODir;
    private final String coreBOImplDir;

    private final String coreMybatisMapperDir;
    private final String coreMybatisMapperExtensionDir;

    private final String serviceBaseDir;
    private final String servicePackageDir;
    private final String serviceResourcesDir;
    private final String serviceConfigDir;

    public ProjectInfo(String path, String projectName, String packageName) {
        this.path = path;
        this.projectName = projectName;
        this.packageName = packageName;

        final String resources = "/src/main/resources";
        final String java = "/src/main/java";
        final String config = "/src/main/config";

        this.baseDir = path + sp + projectName;
        String packageDir = packageName.replace(dot, sp);

        this.coreBaseDir = baseDir + sp + projectName + "-core";
        this.corePackageDir = coreBaseDir + java + sp + packageDir + sp + "core";
        this.coreResourcesDir = coreBaseDir + resources;

        this.coreModelDir = corePackageDir + sp + "model";
        this.coreVODir = corePackageDir + sp + "vo";
        this.coreDAODir = corePackageDir + sp + "dao";
        this.coreDAOImplDir = corePackageDir + sp + "dao/impl";
        this.coreBODir = corePackageDir + sp + "bo";
        this.coreBOImplDir = corePackageDir + sp + "bo/impl";

        this.coreMybatisMapperDir = coreResourcesDir + sp + "mapper";
        this.coreMybatisMapperExtensionDir = coreMybatisMapperDir + sp + "extension";

        this.serviceBaseDir = baseDir + sp + projectName + "-service";
        this.servicePackageDir = serviceBaseDir + java + sp + packageDir + sp + "service";
        this.serviceResourcesDir = serviceBaseDir + resources;
        this.serviceConfigDir = serviceBaseDir + config;
    }

    public String getPath() {
        return path;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getCoreBaseDir() {
        return coreBaseDir;
    }

    public String getCorePackageDir() {
        return corePackageDir;
    }

    public String getCoreResourcesDir() {
        return coreResourcesDir;
    }

    public String getServiceBaseDir() {
        return serviceBaseDir;
    }

    public String getServicePackageDir() {
        return servicePackageDir;
    }

    public String getServiceResourcesDir() {
        return serviceResourcesDir;
    }

    public String getServiceConfigDir() {
        return serviceConfigDir;
    }

    public String getCoreModelDir() {
        return coreModelDir;
    }

    public String getCoreVODir() {
        return coreVODir;
    }

    public String getCoreDAODir() {
        return coreDAODir;
    }

    public String getCoreDAOImplDir() {
        return coreDAOImplDir;
    }

    public String getCoreBODir() {
        return coreBODir;
    }

    public String getCoreBOImplDir() {
        return coreBOImplDir;
    }

    public String getCoreMybatisMapperDir() {
        return coreMybatisMapperDir;
    }

    public String getCoreMybatisMapperExtensionDir() {
        return coreMybatisMapperExtensionDir;
    }

    public char getSp() {
        return sp;
    }

    public char getDot() {
        return dot;
    }
}
