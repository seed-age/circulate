package com.sunnsoft.sloa.util.redis.jedis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;


public class RedisLock implements Lock, Serializable {
 
    private static final long serialVersionUID = -2951680710381145553L;
 
    private static final Log logger = LogFactory.getLog(RedisLock.class);
    private static final String PREFIX = "lock_";
 
    private String key;
    private int tryLock;//加锁失败重试次数
    private int tryUnlock;//解锁失败重试次数
    private int lockSleep;//加锁重试时,线程等待时间
    private int unlockSleep;//解锁重试时,线程等待时间
    private int lockExpire;//加锁过期时间(防止unlock失败)  单位:秒
    private JedisService jedisService;
 
    public RedisLock(String key,JedisService jedisService) {
        this(key,60,jedisService);
    }
    
    public RedisLock(String key,int lockExpire,JedisService jedisService) {
        this(key, 20, 20, 200, 200, lockExpire,jedisService);
    }
 
    /**
     * @param key 锁id
     * @param tryLock 获取锁重试次数
     * @param tryUnlock 释放锁重试次数
     * @param lockSleep 获取锁重试等待时间(毫秒)在0~lockSleep之间随机
     * @param unlockSleep 释放锁重试等待时间(毫秒)在0~unlockSleep之间随机
     * @param lockExpire 获取锁时,key过期时间
     * @param unlockExpire 释放锁时,key过期时间
     */
    public RedisLock(String key, int tryLock, int tryUnlock, int lockSleep, int unlockSleep, int lockExpire,JedisService jedisService) {
        this.key = key;
        this.tryLock = tryLock;
        this.tryUnlock = tryUnlock;
        this.lockSleep = lockSleep;
        this.unlockSleep = unlockSleep;
        this.lockExpire = lockExpire;
        this.jedisService = jedisService;
    }
    
    public static final String RESP_OK = "OK";
    public static final Long RESP_1 = new Long(1);
    
    public boolean expect(List<Object> results, Object... expects) {
        try {
            if (results != null && expects != null && results.size() == expects.length) {
                for (int i = 0; i < expects.length; i++) {
                    Object result = results.get(i);
                    if(result == null || !result.equals(expects[i])){
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            if(logger.isErrorEnabled()){
            	logger.error("对比失败:" + results + " @ " + Arrays.toString(expects), e);
            }
        }
        return false;
    }
 
    @Override
    public void lock() {
        Jedis jedis = null;
        try {
            String lockKey = getLockKey();
            jedis = jedisService.borrow();
            Random r = new Random();
            for (int i = 0; i < tryLock; i++) {
                Boolean exists = jedis.exists(lockKey);
                if (exists) {//存在, sleep, 重试
                    try {
                        Thread.sleep(r.nextInt(lockSleep));
                    } catch (InterruptedException e) {
                        if(logger.isWarnEnabled()){
                        	logger.warn(Thread.currentThread().getName() + " 收到中断信号 : " + this);
                        }
                    }
                    continue;
                } else {//不存在, 加锁
                    jedis.watch(lockKey);
                    Transaction t = jedis.multi();
                    t.set(lockKey, key);
                    t.expire(lockKey, lockExpire);
                    List<Object> exec = t.exec();
                    if (expect(exec, RESP_OK, RESP_1)) {
                        if(logger.isDebugEnabled()){
                        	logger.debug(Thread.currentThread().getName() + " 获取到锁 : " + this);
                        }
                        return;
                    } else {//加锁失败, sleep, 重试
                        try {
                            Thread.sleep(lockSleep);
                        } catch (InterruptedException e) {
                            if(logger.isWarnEnabled()){
                            	logger.warn(Thread.currentThread().getName() + " 获取锁 收到中断信号 : " + this);
                            }
                        }
                        continue;
                    }
                }
            }
            throw new RuntimeException(Thread.currentThread().getName() + " 获取锁失败, 超过最大重试次数 !");
        } catch (ClassCastException e) {
            this.jedisService.returnResource(jedis);
            throw new RuntimeException(Thread.currentThread().getName() + " 获取锁失败, redis链接损坏 !");
        } finally {
            this.jedisService.returnResource(jedis);
        }
    }
 
    @Override
    public void unlock() {
        Jedis jedis = null;
        try {
            jedis = jedisService.borrow();
            String lockKey = getLockKey();
            Random r = new Random();
            for (int i = 0; i < tryUnlock; i++) {
                Boolean exists = jedis.exists(lockKey);
                if (exists) {
                    jedis.watch(lockKey);
                    Transaction t = jedis.multi();
                    t.del(lockKey);
                    List<Object> exec = t.exec();
                    if (expect(exec, RESP_1)) {
                        if(logger.isDebugEnabled()){
                        	logger.debug(Thread.currentThread().getName() + " 释放锁 : " + this);
                        }
                        return;
                    } else {//解锁失败, sleep, 重试
                    	if(logger.isDebugEnabled()){
                    		logger.debug(Thread.currentThread().getName() + " 解锁失败, sleep, 重试 ,exec: " + exec);
                    	}
                        
                        try {
                        	Thread.sleep(r.nextInt(unlockSleep));
                        } catch (InterruptedException e) {
                            logger.warn(Thread.currentThread().getName() + " 释放锁 收到中断信号 : " + this);
                        }
                        continue;
                    }
                }else{//如果锁自动失效，则解锁动作自动退出。
                	return;
                }
            }
            throw new RuntimeException(Thread.currentThread().getName() + " 释放锁失败, 超过最大重试次数 !");
        } catch (ClassCastException e) {
        	jedisService.returnResource(jedis);
            throw new RuntimeException(Thread.currentThread().getName() + " 释放锁失败, redis链接损坏 !");
        } finally {
            jedisService.returnResource(jedis);
        }
    }
 
    private String getLockKey() {
        return PREFIX + key;
    }
 
    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("不支持的方法!");
    }
 
    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException("不支持的方法!");
    }
 
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("不支持的方法!");
    }
 
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("不支持的方法!");
    }
 
    @Override
    public String toString() {
        return "RedisLock [key=" + key + ", tryLock=" + tryLock + ", tryUnlock=" + tryUnlock + ", lockSleep="
                + lockSleep + ", unlockSleep=" + unlockSleep + ", lockExpire=" + lockExpire + "]";
    }
 
    public String getKey() {
        return key;
    }
 
}
