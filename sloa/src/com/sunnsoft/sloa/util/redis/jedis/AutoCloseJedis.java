package com.sunnsoft.sloa.util.redis.jedis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.JedisCluster.Reset;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AutoCloseJedis extends Jedis {
	
	private static final Log logger = LogFactory.getLog(AutoCloseJedis.class);
	
	private JedisService jedisService;
	
	public AutoCloseJedis(JedisService jedisService) {
		super("no use", 0);
		this.jedisService = jedisService;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.set(key, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, long time) {
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.set(key, value, nxxx, expx, time);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.get(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.exists(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long del(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.del(keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long del(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.del(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String type(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.type(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> keys(String pattern) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.keys(pattern);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String randomKey() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.randomKey();
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String rename(String oldkey, String newkey) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.rename(oldkey, newkey);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long renamenx(String oldkey, String newkey) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.renamenx(oldkey, newkey);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long expire(String key, int seconds) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.expire(key, seconds);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.expireAt(key, unixTime);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long ttl(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.ttl(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long move(String key, int dbIndex) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.move(key, dbIndex);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String getSet(String key, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.getSet(key, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> mget(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.mget(keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long setnx(String key, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.setnx(key, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String setex(String key, int seconds, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.setex(key, seconds, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String mset(String... keysvalues) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.mset(keysvalues);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long msetnx(String... keysvalues) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.msetnx(keysvalues);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long decrBy(String key, long integer) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.decrBy(key, integer);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long decr(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.decr(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long incrBy(String key, long integer) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.incrBy(key, integer);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long incr(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.incr(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long append(String key, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.append(key, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String substr(String key, int start, int end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.substr(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long hset(String key, String field, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hset(key, field, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String hget(String key, String field) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hget(key, field);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hsetnx(key, field, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hmset(key, hash);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hmget(key, fields);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hincrBy(key, field, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean hexists(String key, String field) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hexists(key, field);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long hdel(String key, String... fields) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hdel(key, fields);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long hlen(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hlen(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> hkeys(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hkeys(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> hvals(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hvals(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hgetAll(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long rpush(String key, String... strings) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.rpush(key, strings);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long lpush(String key, String... strings) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lpush(key, strings);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long llen(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.llen(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lrange(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String ltrim(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.ltrim(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String lindex(String key, long index) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lindex(key, index);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String lset(String key, long index, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lset(key, index, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long lrem(String key, long count, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lrem(key, count, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String lpop(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lpop(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String rpop(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.rpop(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.rpoplpush(srckey, dstkey);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sadd(String key, String... members) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sadd(key, members);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> smembers(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.smembers(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long srem(String key, String... members) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.srem(key, members);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String spop(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.spop(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long smove(String srckey, String dstkey, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.smove(srckey, dstkey, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long scard(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scard(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean sismember(String key, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sismember(key, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> sinter(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sinter(keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sinterstore(String dstkey, String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sinterstore(dstkey, keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> sunion(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sunion(keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sunionstore(String dstkey, String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sunionstore(dstkey, keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> sdiff(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sdiff(keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sdiffstore(String dstkey, String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sdiffstore(dstkey, keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String srandmember(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.srandmember(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> srandmember(String key, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.srandmember(key, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zadd(String key, double score, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zadd(key, score, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	

	@Override
	public String asking() {
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.asking();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Long> bitfield(String key, String... arguments) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.bitfield(key, arguments);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long bitpos(String key, boolean value, BitPosParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.bitpos(key, value, params);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long bitpos(String key, boolean value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.bitpos(key, value);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> blpop(int timeout, String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.blpop(timeout, key);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> brpop(int timeout, String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.brpop(timeout, key);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public void close() {
		
		super.close();
	}

	@Override
	public String clusterAddSlots(int... slots) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterAddSlots(slots);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long clusterCountKeysInSlot(int slot) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterCountKeysInSlot(slot);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterDelSlots(int... slots) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterDelSlots(slots);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterFailover() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterFailover();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterFlushSlots() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterFlushSlots();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterForget(String nodeId) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterForget(nodeId);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> clusterGetKeysInSlot(int slot, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterGetKeysInSlot(slot, count);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterInfo() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterInfo();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long clusterKeySlot(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterKeySlot(key);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterMeet(String ip, int port) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterMeet(ip, port);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterNodes() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterNodes();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterReplicate(String nodeId) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterReplicate(nodeId);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterReset(Reset resetType) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterReset(resetType);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterSaveConfig() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSaveConfig();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterSetSlotImporting(int slot, String nodeId) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSetSlotImporting(slot, nodeId);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterSetSlotMigrating(int slot, String nodeId) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSetSlotMigrating(slot, nodeId);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterSetSlotNode(int slot, String nodeId) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSetSlotNode(slot, nodeId);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clusterSetSlotStable(int slot) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSetSlotStable(slot);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> clusterSlaves(String nodeId) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSlaves(nodeId);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Object> clusterSlots() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clusterSlots();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long exists(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.exists(keys);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.geoadd(key, longitude, latitude, member);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.geoadd(key, memberCoordinateMap);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double geodist(String key, String member1, String member2, GeoUnit unit) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.geodist(key, member1, member2, unit);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double geodist(String key, String member1, String member2) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.geodist(key, member1, member2);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> geohash(String key, String... members) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.geohash(key, members);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<GeoCoordinate> geopos(String key, String... members) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.geopos(key, members);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.georadius(key, longitude, latitude, radius, unit, param);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
			GeoUnit unit) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.georadius(key, longitude, latitude, radius, unit);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.georadiusByMember(key, member, radius, unit, param);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.georadiusByMember(key, member, radius, unit);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hscan(key, cursor, params);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hscan(key, cursor);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long pexpire(String key, long milliseconds) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pexpire(key, milliseconds);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long pfadd(String key, String... elements) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pfadd(key, elements);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public long pfcount(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pfcount(keys);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return 0;
	}

	@Override
	public long pfcount(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pfcount(key);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return 0;
	}

	@Override
	public String pfmerge(String destkey, String... sourcekeys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pfmerge(destkey, sourcekeys);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String psetex(String key, long milliseconds, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.psetex(key, milliseconds, value);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> pubsubChannels(String pattern) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pubsubChannels(pattern);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long pubsubNumPat() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pubsubNumPat();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Map<String, String> pubsubNumSub(String... channels) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pubsubNumSub(channels);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String readonly() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.readonly();
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> scan(String arg0, ScanParams arg1) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scan(arg0, arg1);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> scan(String cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scan(cursor);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String sentinelFailover(String masterName) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelFailover(masterName);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelMonitor(masterName, ip, port, quorum);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String sentinelRemove(String masterName) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelRemove(masterName);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String sentinelSet(String arg0, Map<String, String> arg1) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelSet(arg0, arg1);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public void setDataSource(Pool<Jedis> jedisPool) {
		throw new RuntimeException("本类中，此方法无效");
	}

	@Override
	public Set<String> spop(String key, long count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.spop(key, count);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> sscan(String arg0, String arg1, ScanParams arg2) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sscan(arg0, arg1, arg2);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sscan(key, cursor);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zadd(String key, double score, String member, ZAddParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zadd(key, score, member, params);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zadd(key, scoreMembers, params);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zadd(key, scoreMembers);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double zincrby(String key, double score, String member, ZIncrByParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zincrby(key, score, member, params);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zlexcount(String key, String min, String max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zlexcount(key, min, max);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByLex(key, min, max, offset, count);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrangeByLex(String key, String min, String max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByLex(key, min, max);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zremrangeByLex(String key, String min, String max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zremrangeByLex(key, min, max);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByLex(key, max, min, offset, count);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrangeByLex(String key, String max, String min) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByLex(key, max, min);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zscan(key, cursor, params);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zscan(key, cursor);
		} catch (Throwable e) {
			this.jedisService.returnResource(jedis);
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrange(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zrem(String key, String... members) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrem(key, members);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zincrby(key, score, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zrank(String key, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrank(key, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zrevrank(String key, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrank(key, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrange(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeWithScores(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeWithScores(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zcard(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zcard(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double zscore(String key, String member) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zscore(key, member);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String watch(String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.watch(keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> sort(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sort(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sort(key, sortingParameters);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> blpop(int timeout, String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.blpop(timeout, keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> blpop(String... args) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.blpop(args);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> brpop(String... args) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.brpop(args);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> blpop(String arg) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.blpop(arg);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> brpop(String arg) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.brpop(arg);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sort(key, sortingParameters, dstkey);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sort(String key, String dstkey) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sort(key, dstkey);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> brpop(int timeout, String... keys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.brpop(timeout, keys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zcount(String key, double min, double max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zcount(key, min, max);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zcount(String key, String min, String max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zcount(key, min, max);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScore(key, min, max);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScore(key, min, max);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScoreWithScores(key, min, max);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zremrangeByRank(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zremrangeByScore(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zremrangeByScore(String key, String start, String end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zremrangeByScore(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zunionstore(String dstkey, String... sets) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zunionstore(dstkey, sets);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zunionstore(dstkey, params, sets);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zinterstore(String dstkey, String... sets) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zinterstore(dstkey, sets);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zinterstore(dstkey, params, sets);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long strlen(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.strlen(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long lpushx(String key, String... string) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.lpushx(key, string);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long persist(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.persist(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long rpushx(String key, String... string) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.rpushx(key, string);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String echo(String string) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.echo(string);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.linsert(key, where, pivot, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String brpoplpush(String source, String destination, int timeout) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.brpoplpush(source, destination, timeout);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.setbit(key, offset, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean setbit(String key, long offset, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.setbit(key, offset, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean getbit(String key, long offset) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.getbit(key, offset);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long setrange(String key, long offset, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.setrange(key, offset, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.getrange(key, startOffset, endOffset);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> configGet(String pattern) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.configGet(pattern);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String configSet(String parameter, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.configSet(parameter, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Object eval(String script, int keyCount, String... params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.eval(script, keyCount, params);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			jedis.subscribe(jedisPubSub, channels);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
	}

	@Override
	public Long publish(String channel, String message) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.publish(channel, message);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			jedis.psubscribe(jedisPubSub, patterns);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
	}

	@Override
	public Object eval(String script, List<String> keys, List<String> args) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.eval(script, keys, args);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Object eval(String script) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.eval(script);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Object evalsha(String script) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.evalsha(script);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.evalsha(sha1, keys, args);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Object evalsha(String sha1, int keyCount, String... params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.evalsha(sha1, keyCount, params);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Boolean scriptExists(String sha1) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scriptExists(sha1);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Boolean> scriptExists(String... sha1) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scriptExists(sha1);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String scriptLoad(String script) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scriptLoad(script);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Slowlog> slowlogGet() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.slowlogGet();
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Slowlog> slowlogGet(long entries) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.slowlogGet(entries);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long objectRefcount(String string) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.objectRefcount(string);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String objectEncoding(String string) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.objectEncoding(string);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long objectIdletime(String string) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.objectIdletime(string);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long bitcount(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.bitcount(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long bitcount(String key, long start, long end) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.bitcount(key, start, end);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long bitop(BitOP op, String destKey, String... srcKeys) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.bitop(op, destKey, srcKeys);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Map<String, String>> sentinelMasters() {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelMasters();
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<String> sentinelGetMasterAddrByName(String masterName) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelGetMasterAddrByName(masterName);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long sentinelReset(String pattern) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelReset(pattern);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public List<Map<String, String>> sentinelSlaves(String masterName) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sentinelSlaves(masterName);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public byte[] dump(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.dump(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String restore(String key, int ttl, byte[] serializedValue) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.restore(key, ttl, serializedValue);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long pexpire(String key, int milliseconds) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pexpire(key, milliseconds);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pexpireAt(key, millisecondsTimestamp);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Long pttl(String key) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.pttl(key);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double incrByFloat(String key, double increment) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.incrByFloat(key, increment);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String psetex(String key, int milliseconds, String value) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.psetex(key, milliseconds, value);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String set(String key, String value, String nxxx) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.set(key, value, nxxx);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, int time) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.set(key, value, nxxx, expx, time);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clientKill(String client) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clientKill(client);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String clientSetname(String name) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.clientSetname(name);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public String migrate(String host, int port, String key, int destinationDb, int timeout) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.migrate(host, port, key, destinationDb, timeout);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public Double hincrByFloat(String key, String field, double increment) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hincrByFloat(key, field, increment);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> scan(int cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scan(cursor);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> scan(int cursor, ScanParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.scan(cursor, params);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hscan(key, cursor);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, int cursor, ScanParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.hscan(key, cursor, params);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> sscan(String key, int cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sscan(key, cursor);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<String> sscan(String key, int cursor, ScanParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.sscan(key, cursor, params);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Tuple> zscan(String key, int cursor) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zscan(key, cursor);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}

	@Override
	public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) {
		
		Jedis jedis = null;
		try {
			jedis = this.jedisService.borrow();
			return jedis.zscan(key, cursor, params);
		} catch (Throwable e) {
			
			logger.error("jedis exception",e);
		} finally{
			this.jedisService.returnResource(jedis);
		}
		return null;
	}
	
	

}
