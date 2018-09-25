用spring session 做多tomcat的session共享，(注意，和javamelody有冲突，新版本的javamelody有bug,目前最高能兼容到1.63版本的javamelody)
1.把本文件夹对应的jar的最新版加入到lib(jedis的jar一般项目内有)
2.找到resourceContext-deploy.xml，加入如下配置：
	<bean id="redisHttpSessionConfiguration"  class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration" >
	    <property name="maxInactiveIntervalInSeconds" value="7200" />
	</bean>
	
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
	    <property name="maxTotal" value="300" />
	    <property name="maxIdle" value="10" />
	</bean>

	<bean id="jedisConnectionFactory"
	      class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory" destroy-method="destroy">
	    <property name="hostName" value="${redis.server.ip}"/>
	    <property name="port" value="${redis.server.port}"/>
	    <property name="password" value="${redis.server.password}" />
	    <property name="timeout" value="3000"/>
	     <property name="database" value="15"></property>
	    <property name="usePool" value="true"/>
	    <property name="poolConfig" ref="jedisPoolConfig"/>
	</bean>
3.找到web-deploy.xml，加入如下配置
	<filter>
		<filter-name>springSessionRepositoryFilter</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>springSessionRepositoryFilter</filter-name>
		<url-pattern>/*</url-pattern>
		<dispatcher>REQUEST</dispatcher>
		<dispatcher>ERROR</dispatcher>
	</filter-mapping>

这样多tomcat之间的session共享就配置完成了。注意，此filter应该在所有filter之前。
备注：由于session共享一般在生产环境或测试环境上使用，所以一般只配置 web-deploy.xml和resourceContext-deploy.xml

注意：系统使用JavaMelody 1.64~1.69版本的话，使用spring-session会有问题。