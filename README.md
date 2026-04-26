application.properties

```
spring.application.name=easycrm
spring.datasource.url=jdbc:mysql://localhost:3306/{DB_DATABASE}
spring.datasource.username={DB_USER}
spring.datasource.password={DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.thymeleaf.cache=false

server.servlet.session.timeout=1d
```
