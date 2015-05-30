<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper PUBLIC
    "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${modelClassSimpleName?uncap_first}DAO">
  <resultMap id="${modelClassSimpleName?uncap_first}" type="${basePackageName}.${projectName}.model.${modelClassSimpleName}">
  <#list propertyNameList as propertyName>
    <result property="${propertyName}" column="${columnInfoList[propertyName_index].columnName}" />
  </#list>
  </resultMap>

  <sql id="sql_insert_column_list">
    <trim prefix="" prefixOverrides=",">
    <#list propertyNameList as propertyName>
    <#if propertyTypeList[propertyName_index] == "java.lang.String">
      <if test="entity.${propertyName} != null and entity.${propertyName} != ''">,${columnInfoList[propertyName_index].columnName}</if>
    <#else>
      <if test="entity.${propertyName} != null">,${columnInfoList[propertyName_index].columnName}</if>
      <#if propertyName == "createdAt" || propertyName == "updatedAt">
      <if test="entity.${propertyName} == null">,${columnInfoList[propertyName_index].columnName}</if>
      </#if>
    </#if>
    </#list>
    </trim>
  </sql>

  <sql id="sql_insert_property_list">
    <trim prefix="" prefixOverrides=",">
    <#list propertyNameList as propertyName>
    <#if propertyTypeList[propertyName_index] == "java.lang.String">
      <if test="entity.${propertyName} != null and entity.${propertyName} != ''">,${r"#"}{entity.${propertyName}}</if>
    <#else>
      <if test="entity.${propertyName} != null">,${r"#"}{entity.${propertyName}}</if>
      <#if propertyName == "createdAt" || propertyName == "updatedAt">
      <if test="entity.${propertyName} == null">,NOW()</if>
      </#if>
    </#if>
    </#list>
    </trim>
  </sql>

  <sql id="sql_column_list">
  <#list columnInfoList as columnInfo>
    <#if (columnInfo_index > 0)>,</#if>${columnInfo.columnName}
  </#list>
  </sql>

  <sql id="sql_update_set">
    <trim prefix="SET" prefixOverrides=",">
      <if test="entity != null">
      <#list propertyNameList as propertyName>
      <#if propertyTypeList[propertyName_index] == "java.lang.String">
        <if test="entity.${propertyName} != null and entity.${propertyName} != ''">,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity.${propertyName}}</if>
      <#else>
        <if test="entity.${propertyName} != null">,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity.${propertyName}}</if>
        <#if propertyName == "updatedAt">
        <if test="entity.${propertyName} == null">,${columnInfoList[propertyName_index].columnName} = NOW()</if>
        </#if>
      </#if>
      </#list>
      </if>
    </trim>
  </sql>

  <sql id="sql_force_update_set">
    <trim prefix="SET" prefixOverrides=",">
    <#list propertyNameList as propertyName>
      <if test="_parameter.containsKey('entity_${propertyName}')">,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity_${propertyName}, jdbcType=${columnTypeList[propertyName_index]}}</if>
      <#if propertyName == "updatedAt">
      <if test="!_parameter.containsKey('entity_${propertyName}')">,${columnInfoList[propertyName_index].columnName} = NOW()</if>
      </#if>
    </#list>
    </trim>
  </sql>

  <sql id="sql_condition">
    <if test="condition != null">
    <#list propertyNameList as propertyName>
    <#if propertyTypeList[propertyName_index] == "java.lang.String">
      <if test="condition.${propertyName} != null and condition.${propertyName} != ''">AND ${columnInfoList[propertyName_index].columnName} = ${r"#"}{condition.${propertyName}}</if>
    <#else>
      <if test="condition.${propertyName} != null">AND ${columnInfoList[propertyName_index].columnName} = ${r"#"}{condition.${propertyName}}</if>
    </#if>
    </#list>
      <if test="!condition.expressionChainList.empty">
        AND
        <foreach collection="condition.expressionChainList" item="expressionChain" separator="OR">
          <if test="!expressionChain.expressionList.empty">
            <foreach collection="expressionChain.expressionList" item="expression" separator="AND">
              <choose>
                <when test="expression.type == 0">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator}
                </when>
                <when test="expression.type == 1">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator} ${r"#"}{expression.value}
                </when>
                <when test="expression.type == 2">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator} ${r"#"}{expression.value} AND ${r"#"}{expression.value1}
                </when>
                <when test="expression.type == 3">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator}
                  <foreach collection="expression.values" item="value" open="(" separator="," close=")">
                    ${r"#"}{value}
                  </foreach>
                </when>
              </choose>
            </foreach>
          </if>
        </foreach>
      </if>
    </if>
  </sql>

  <sql id="sql_pagination_start">
  <![CDATA[
  ]]>
  </sql>

  <sql id="sql_pagination_end">
  <![CDATA[
    LIMIT ${r"#"}{offset}, ${r"#"}{limit}
  ]]>
  </sql>

  <insert id="insert" parameterType="java.util.Map">
    <selectKey keyProperty="entity.id" resultType="${pkType}" order="AFTER">
      SELECT LAST_INSERT_ID()
    </selectKey>
    INSERT INTO ${tableName} (
      <include refid="sql_insert_column_list" />
    ) VALUES (
      <include refid="sql_insert_property_list" />
    )
  </insert>

  <select id="get" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    SELECT
      <include refid="sql_column_list" />
    FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </select>

  <select id="getById" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    SELECT
      <include refid="sql_column_list" />
    FROM ${tableName}
    WHERE id = ${r"#"}{id}
  </select>

  <select id="find" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    <if test="offset != null">
      <include refid="sql_pagination_start" />
    </if>
    SELECT
      <include refid="sql_column_list" />
    FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
    <if test="orderBy != null">
      ORDER BY ${r"$"}{orderBy}
    </if>
    <if test="offset != null">
      <include refid="sql_pagination_end" />
    </if>
  </select>

  <select id="findByIdList" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    SELECT
      <include refid="sql_column_list" />
    FROM ${tableName}
    WHERE id IN
    <foreach collection="idList" item="id" open="(" separator="," close=")">
      ${r"#"}{id}
    </foreach>
    <include refid="sql_condition" />
    <if test="orderBy != null">
      ORDER BY ${r"$"}{orderBy}
    </if>
  </select>

  <select id="count" parameterType="java.util.Map" resultType="java.lang.Integer">
    SELECT count(${r"$"}{count_column}) AS count_ FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </select>

  <select id="aggregate" parameterType="java.util.Map" resultType="java.util.HashMap">
    SELECT ${r"$"}{aggregate_sql} FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </select>

  <update id="update" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_update_set" />
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </update>

  <update id="updateById" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_update_set" />
    WHERE id = ${r"#"}{id}
  </update>

  <update id="updateByIdList" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_update_set" />
    WHERE id IN
    <foreach collection="idList" item="id" open="(" separator="," close=")">
      ${r"#"}{id}
    </foreach>
    <include refid="sql_condition" />
  </update>

  <update id="forceUpdate" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_force_update_set" />
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </update>

  <update id="forceUpdateById" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_force_update_set" />
    WHERE id = ${r"#"}{id}
  </update>

  <update id="forceUpdateByIdList" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_force_update_set" />
    WHERE id IN
    <foreach collection="idList" item="id" open="(" separator="," close=")">
      ${r"#"}{id}
    </foreach>
    <include refid="sql_condition" />
  </update>

  <delete id="remove" parameterType="java.util.Map">
    DELETE FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </delete>

  <delete id="removeById" parameterType="java.util.Map">
    DELETE FROM ${tableName}
    WHERE id = ${r"#"}{id}
  </delete>

  <delete id="removeByIdList" parameterType="java.util.Map">
    DELETE FROM ${tableName}
    WHERE id IN
    <foreach collection="idList" item="id" open="(" separator="," close=")">
      ${r"#"}{id}
    </foreach>
    <include refid="sql_condition" />
  </delete>
</mapper>
