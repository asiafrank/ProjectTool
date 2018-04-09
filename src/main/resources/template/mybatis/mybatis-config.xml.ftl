<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE configuration PUBLIC
    "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
  <mappers>
    <!--
    <mapper resource="mapper/table_name.xml" />
    <mapper resource="mapper/extension/table_name.xml" />
    -->
    <#list tableNames as tableName><mapper resource="mapper/${tableName}.xml" />
    </#list>
  </mappers>
</configuration>
