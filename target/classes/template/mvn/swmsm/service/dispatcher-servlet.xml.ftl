<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:jee="http://www.springframework.org/schema/jee"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/mvc
           http://www.springframework.org/schema/mvc/spring-mvc.xsd
           http://www.springframework.org/schema/jee
           http://www.springframework.org/schema/jee/spring-jee.xsd
           http://www.springframework.org/schema/aop
           http://www.springframework.org/schema/aop/spring-aop.xsd
           http://www.springframework.org/schema/tx
           http://www.springframework.org/schema/tx/spring-tx.xsd">

  <context:component-scan base-package="${basePackageName}.${projectName}.service.controller" />

  <mvc:annotation-driven ignoreDefaultModelOnRedirect="true" />

  <import resource="classpath*:/spring/applicationContext-*.xml" />

  <bean id="freemarkerSettings" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
    <property name="location" value="classpath:freemarker.properties" />
  </bean>

  <bean id="freemarkerConfigurer" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
    <property name="freemarkerSettings" ref="freemarkerSettings" />
    <property name="templateLoaderPath" value="/WEB-INF/view" />
  </bean>

  <bean id="freemarkerViewResolver" class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
    <property name="contentType" value="text/html; charset=UTF-8" />
  </bean>

  <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
  </bean>
</beans>
