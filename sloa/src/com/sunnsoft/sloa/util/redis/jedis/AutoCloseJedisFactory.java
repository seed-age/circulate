package com.sunnsoft.sloa.util.redis.jedis;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


//@Component
public class AutoCloseJedisFactory {
	
	@Resource
	private JedisService jedisService;
	
	public AutoCloseJedis  create(){
		return new AutoCloseJedis(jedisService);
	}
}
