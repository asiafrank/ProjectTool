# http://docs.spring.io/autorepo/docs/spring-boot/current/reference/htmlsingle/#common-application-properties
server.port=8080
server.session.cookie.http-only=true
endpoints.cors.allowed-methods=GET,POST,PUT,DELETE

spring.jersey.application-path=/
spring.jackson.serialization.write-dates-as-timestamps=false

logging.level.root=INFO
logging.level.sample.mybatis.mapper=TRACE

spring.datasource.url=${url}
spring.datasource.username=${username}
spring.datasource.password=${password}
spring.datasource.driver-class-name=${driver}
spring.datasource.tomcat.max-wait=10000
spring.datasource.tomcat.max-active=50
spring.datasource.tomcat.test-on-borrow=true