# cryptweets.io

To properly save coins into local MySQL database, add a "hibernate.properties" file under src/main/resources with the following format:

hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver<br>
hibernate.connection.url=jdbc:mysql://&lt;hostname&gt;:&lt;port&gt;/&lt;database&gt;?useSSL=false<br>
hibernate.connection.username=&lt;username&gt;<br>
hibernate.connection.password=&lt;password&gt;<br>
hibernate.connection.pool_size=1<br>
hibernate.current_session_context_class=thread<br>
hibernate.show_sql=true<br>
hibernate.dialect=org.hibernate.dialect.MySQLDialect


To use the Solume.io API, add a "solumeio.properties" file under src/main/resources with the following format:
id-token=&lt;API Token&gt;
