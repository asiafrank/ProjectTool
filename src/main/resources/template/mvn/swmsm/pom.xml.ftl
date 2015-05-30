<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>${basePackageName}</groupId>
    <artifactId>lib-parent</artifactId>
    <version>1</version>
  </parent>
  <groupId>${basePackageName}.${projectName}</groupId>
  <artifactId>${projectName}-parent</artifactId>
  <packaging>pom</packaging>
  <version>1.0.0-SNAPSHOT</version>
  <name>${r"$"}{project.artifactId} v${r"$"}{project.version}</name>

  <modules>
    <module>${projectName}-api</module>
    <module>${projectName}-client</module>
    <module>${projectName}-core</module>
    <module>${projectName}-web</module>
    <module>${projectName}-admin</module>
    <module>${projectName}-service</module>
  </modules>

  <properties>
    <common.util.version>1.0.0-SNAPSHOT</common.util.version>
    <common.bean.version>1.0.0-SNAPSHOT</common.bean.version>
    <common.dao.version>1.0.0-SNAPSHOT</common.dao.version>
    <common.bo.version>1.0.0-SNAPSHOT</common.bo.version>
    <common.pagination.version>1.0.0-SNAPSHOT</common.pagination.version>
    <common.web.version>1.0.0-SNAPSHOT</common.web.version>
    <common.test.version>1.0.0-SNAPSHOT</common.test.version>
  </properties>

  <build>
    <resources>
      <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
      </resource>
    </resources>
    <filters>
      <filter>${r"$"}{user.home}/.filter/${projectName}.properties</filter>
    </filters>
  </build>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>${r"$"}{project.groupId}</groupId>
        <artifactId>${projectName}-api</artifactId>
        <version>${r"$"}{project.version}</version>
      </dependency>
      <dependency>
        <groupId>${r"$"}{project.groupId}</groupId>
        <artifactId>${projectName}-core</artifactId>
        <version>${r"$"}{project.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-util</artifactId>
        <version>${r"$"}{common.util.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-bean</artifactId>
        <version>${r"$"}{common.bean.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-dao</artifactId>
        <version>${r"$"}{common.dao.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-bo</artifactId>
        <version>${r"$"}{common.bo.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-pagination</artifactId>
        <version>${r"$"}{common.pagination.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-web</artifactId>
        <version>${r"$"}{common.web.version}</version>
      </dependency>
      <dependency>
        <groupId>${basePackageName}.common</groupId>
        <artifactId>common-test</artifactId>
        <version>${r"$"}{common.test.version}</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
</project>
