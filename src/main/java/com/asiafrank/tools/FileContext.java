package com.asiafrank.tools;

import java.text.MessageFormat;

/**
 * 生成文件时所需要的上下文
 *
 * @author zhangxiaofan 2019/05/14-16:26
 */
public class FileContext {

    /**
     * 如: /Users/{username}/project
     */
    private final String projectPath;

    /**
     * 如: /src/main/java
     */
    private final String javaSrcRelativePath;

    /**
     * 如: /src/main/resources
     */
    private final String javaResourceRelativePath;

    /**
     * 模块文件夹，如: core
     */
    private final String moduleDir;

    /**
     * 生成 Java 文件时，包名。如：com.asiafrank.core
     */
    private final String packageName;

    /**
     * 包名对应的文件夹，如：com/asiafrank/core
     */
    private final String packageDir;

    /**
     * 生成文件时的文件夹
     */
    private final String fileDir;

    public FileContext(String projectPath,
                       String javaSrcRelativePath,
                       String javaResourceRelativePath,
                       String moduleDir,
                       String packageName)
    {
        this.projectPath = projectPath;
        this.javaSrcRelativePath = javaSrcRelativePath;
        this.javaResourceRelativePath = javaResourceRelativePath;
        this.moduleDir = moduleDir;
        this.packageName = packageName;
        this.packageDir = packageName.replace('.', '/');

        // {projectPath}/{baseModuleDir}{javaSrcRelativePath}/{basePackageDir}/
        // 如: /Users/username/project/core/com/demo/core/base/
        this.fileDir = MessageFormat.format("{0}/{1}{2}/{3}/",
                                            projectPath, moduleDir, javaSrcRelativePath, packageDir);

    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getJavaSrcRelativePath() {
        return javaSrcRelativePath;
    }

    public String getJavaResourceRelativePath() {
        return javaResourceRelativePath;
    }

    public String getModuleDir() {
        return moduleDir;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageDir() {
        return packageDir;
    }

    public String getFileDir() {
        return fileDir;
    }
}
