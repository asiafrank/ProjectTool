<?xml version="1.0" encoding="UTF-8"?>

<configuration>
  <property name="default_pattern" value="%date [%thread] %-5level %logger{80} - %msg%n" />
  <property name="charset" value="UTF-8" />

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>${r"$"}{default_pattern}</pattern>
      <charset>${r"$"}{charset}</charset>
    </encoder>
  </appender>

  <logger name="${basePackageName}.${projectName}">
    <level value="DEBUG" />
  </logger>

  <logger name="org.springframework">
    <level value="WARN" />
  </logger>

  <logger name="org.apache.ibatis">
    <level value="DEBUG" />
  </logger>

  <logger name="java.sql">
    <level value="DEBUG" />
  </logger>

  <root>
    <level value="INFO" />
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
