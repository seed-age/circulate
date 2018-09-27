package com.sunnsoft.sloa.util.redis.jedis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

//@Component
public class JedisService {
	
	@Value("${redis.server.ip}")
	private String serverIp;
	@Value("${redis.server.port}")
	private int serverPort;
	@Value("${redis.server.password}")
	private String serverPwd;
	@Value("${redis.server.database.index}")
	private int databaseIndex;
	
	private JedisPool pool;
	
	@PostConstruct
	public void init(){
		GenericObjectPoolConfig config= new GenericObjectPoolConfig();
 
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(120);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(20);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(25000);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);       
        pool = new JedisPool(config, serverIp, serverPort, 5000, serverPwd, databaseIndex);
        
        System.out.println("启动 redis 连接池 成功" );
        
        
	}
	/**
	 * 还回到连接池
	 * @param redis
	 */
	public void returnResource(Jedis redis) {
	    if (redis != null) {
	        redis.close();
	    }
	}
	
	@PreDestroy
	public void destroy(){
		pool.destroy();
		 System.out.println("redis 连接池 关闭 成功" );
	}
	 
	public Jedis borrow(){
		Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis;
        } catch (Exception e) {
            //释放redis对象
            if(jedis != null)jedis.close();
            e.printStackTrace();
        } 
        return null;
	}
	
}
