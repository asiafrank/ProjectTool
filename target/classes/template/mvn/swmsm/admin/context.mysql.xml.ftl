<Context>
  <Resource name="jdbc/${projectName}"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://localhost:3306/${projectName}?useUnicode=true&amp;characterEncoding=UTF-8"
            username="${projectName}"
            password="123456"
            maxActive="100"
            maxIdle="30"
            maxWait="10000" />
</Context>
