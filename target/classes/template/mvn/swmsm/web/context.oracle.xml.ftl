<Context>
  <Resource name="jdbc/${projectName}"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="oracle.jdbc.driver.OracleDriver"
            url="jdbc:oracle:thin:@192.168.141.100:1521:fosttw"
            username="${projectName}"
            password="abc123456"
            maxActive="100"
            maxIdle="30"
            maxWait="10000" />
</Context>
