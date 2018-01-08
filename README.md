# cryptweets.io

To properly save coins into local MySQL database, add a "hibernate.properties" file under src/main/resources with the following format:

javax.persistence.jdbc.driver=com.mysql.cj.jdbc.Driver<br/>
javax.persistence.jdbc.url=jdbc:mysql://&lt;hostname&gt;:&lt;port&gt;/&lt;database&gt;?useSSL=false<br/>
javax.persistence.jdbc.user=&lt;username&gt;<br>
javax.persistence.jdbc.password=&lt;password&gt;<br>
hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

To use the Solume.io API, add a "solumeio.properties" file under src/main/resources with the following format:<br/>
id-token=&lt;API Token&gt;
