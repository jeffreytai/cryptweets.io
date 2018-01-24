# cryptweets.io

### Usage
To properly save coins into local MySQL database, add a "hibernate.properties" file under src/main/resources with the following format:

```
hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
hibernate.connection.url=jdbc:mysql://<hostname>:<port>/<database>?useSSL=false
hibernate.connection.username=<username>
hibernate.connection.password=<password>
hibernate.current_session_context_class=thread
hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

To use the Solume.io API, add a "solumeio.properties" file under src/main/resources with the following format:<br/>
```
id-token=<API Token>
```

To use the Slack webhook API, add a "slack.properties" file under src/main/resources with the following format:<br/>
```
webhook-url=<webhook-url>
```
