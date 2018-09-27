package com.sunnsoft.sloa.util.redis.redisson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.redisson.config.Config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RDeque;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RQueue;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RSortedSet;
import org.redisson.api.RTopic;

/**
 * 
 * @author andy
 * 使用redisson操作 redis的示范类，更多例子，请参考https://github.com/mrniko/redisson/wiki
 * redisson和jedis都是官方推荐的redis客户端驱动，redisson api和java面向对象编程相符，而且有容易使用的锁、公平锁、异步方式等使用方式，更容易编程。
 * jedis是老牌客户端，兼容性好，bug较少，但很多api只是直接参照redis命令，没有更高一级抽象。
 * 1.本样例程序未实际启动，根据项目需要启用。
 * 2.本样例只使用了单redis模式，集群、哨兵模式则需要调整代码设置。
 * 3.本样例基于redisson需要redis版本2.8以上才能使用。
 */
//@Service
public class RedisService {
	
//	@Resource
//	private UserService userService;
	
//	@Value("${redis.server.ip}")
	private String redisServerIp;
//	@Value("${redis.server.port}")
	private int redisServerPort;
//	@Value("${redis.server.password}")
	private String redisPassword;
	private RedissonClient redisson;
	
	
	public static void main(String[] args) throws IOException{
		RedisService s = new RedisService();
		s.redisServerIp = "localhost";
		s.redisServerPort = 6379;
		s.init();
		System.out.println(s.exist("test001"));
		System.out.println(s.exist("test002"));
		s.setDouble("test001", 0.223);
		System.out.println(s.getDouble("test001"));
//		s.setBigDecimal("test001", BigDecimal.valueOf(2.22));
//		System.out.println(s.getBigDecimal("test001"));
		s.setDouble("test001", null);
		s.delete("test001");
		s.destroy();
	}
	
//	@PostConstruct
	public void init() throws IOException{
		Config config = new Config();
		config.setCodec(new org.redisson.codec.KryoCodec()).useSingleServer().setAddress(redisServerIp + ":" + redisServerPort).setPassword(redisPassword);
		redisson = Redisson.create(config);
//		
//		try {
//			RMap<String, User> map = redisson.getMap("userMap");
//			User admin = (User) this.userService.findByProperty("accountName", "admin").get(0);
//			User value = new User();
//			value.setAccountName(admin.getAccountName());
//			value.setAdmin(admin.isAdmin());
//			value.setCreateTime(admin.getCreateTime());
//			value.setLastLogin(admin.getLastLogin());
//			map.put("redisson.user.admin", value);
//			
//			RMap<String, User> map2 = redisson.getMap("userMap");
//			System.out.println("map2:" + map2.get("redisson.user.admin").getAccountName());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long start0 = System.currentTimeMillis();
//		for(int i = 0; i < 7000 ; i ++){
//			RBucket<String> bucket =  redisson.getBucket("redisson.string.test" + i);
//			bucket.set("test" + i);
//			bucket.expire(60, TimeUnit.SECONDS);
//		}
//		System.out.println("put 7000 time : " + (System.currentTimeMillis() - start0));
//		long start1 = System.currentTimeMillis();
//				
//		RKeys keys = redisson.getKeys();
//
//		Iterable<String> allKeys = keys.getKeys();
//		for (String key : allKeys) {
////			System.out.println("redisson test iterate key:" + key);
//			
//		}
//		
//		System.out.println("key iterator time : " + (System.currentTimeMillis() - start1));
//		
//		redisson.shutdown();

		
	}
	
	public RedissonClient getRedisson() {
		return redisson;
	}

	@PreDestroy
	public void destroy(){
		redisson.shutdown();
	}
	/**
	 * 处理加锁任务的方法，需要实现Lock对象的success和fail方法分别处理加锁成功和失败的逻辑。
	 * Lock对象默认60秒就会自动释放锁，success方法返回后，也会自动释放锁。可以覆盖getAutoReleaseTime方法来修改默认的自动释放时间。
	 * 
	 * 本方法主要作用是用来短暂锁定多个JVM都需要访问和占用的资源，例如抽奖。如果只是单单使用关系数据库字段标识来锁定，无法解决多线程和多JVM的导致的锁被覆盖失效的问题（因为数据库字段获取和变更不是原子操作）。
	 * 因此，引入redis来短暂在jvm之间共享锁，在短暂锁定的情况下，设置长效的数据库字段锁，这样使得数据库字段锁“看起来”像是原子锁。
	 * @param lock
	 * @param lockName
	 * @throws InterruptedException
	 */
	public void doLock(Lock lock,String lockName) throws InterruptedException{
		RLock rLock = this.getFairLock(lockName);
		boolean locked =rLock.tryLock(lock.getWaitTimeOut(), lock.getAutoReleaseTime(), lock.getTimeUnit());
		if(locked){
			try {
				lock.success();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			rLock.unlockAsync();
		}else{
			lock.fail();
		}
	}
	/**
	 * 判断一个锁是否已经锁住，对于一些需要提前判断锁是否锁住的，但不需要锁的场景有用。
	 * @param lockName
	 * @return
	 */
	public boolean isLock(String lockName){
		RLock rLock = this.getFairLock(lockName);
		return rLock.isLocked();
	}
	
	/**
	 * 是否存在
	 * @param objectName
	 * @return
	 */
	public boolean exist(String objectName){
		RBucket<Object> bucket = this.getRBucket(objectName);
		return bucket.isExists();
	}
	/**
	 * 清除过期设置。
	 * @param objectName
	 * @return
	 */
	public boolean clearExpired(String objectName){
		RBucket<Object> bucket = this.getRBucket(objectName);
		return bucket.clearExpire();
	}
	/**
	 * 删除,如果对应的objectName这个key存在，则删除后会返回true，否则返回false
	 * @param objectName
	 * @return 
	 */
	public boolean delete(String objectName){
		RBucket<Object> bucket = this.getRBucket(objectName);
		return bucket.delete();
	}
	/**
	 * 定时失效
	 * @param objectName
	 * @param date
	 */
	public void expiredAt(String objectName , Date date){
		RBucket<Object> bucket = this.getRBucket(objectName);
		bucket.expireAt(date);
	}
	
	/**
	 * 设置多久后失效。
	 * @param objectName
	 * @param seconds
	 */
	public void expiredSeconds(String objectName , long seconds){
		RBucket<Object> bucket = this.getRBucket(objectName);
		bucket.expire(seconds, TimeUnit.SECONDS);
	}
	
	/**
	 * 设置多久后失效。
	 * @param objectName
	 * @param time
	 * @param timeUnit
	 */
	
	public void expireAfter(String objectName , long time , TimeUnit timeUnit){
		RBucket<Object> bucket = this.getRBucket(objectName);
		bucket.expire(time, TimeUnit.SECONDS);
	}
	/**
	 * 获取值
	 * @param objectName
	 * @return
	 */
	public String getString(String objectName){
		RBucket<String> bucket = this.getRBucket(objectName);
		return bucket.get();
	}
	/**
	 * 存入值
	 * @param objectName
	 * @param value
	 */
	public void setString(String objectName,String value){
		RBucket<String> bucket = this.getRBucket(objectName);
		bucket.set(value);
	}
	/**
	 * 存入值,并设置多少秒之后失效。
	 * @param objectName
	 * @param value
	 * @param seconds
	 */
	public void setString(String objectName,String value,long seconds){
		RBucket<String> bucket = this.getRBucket(objectName);
		bucket.set(value);
		bucket.expire(seconds, TimeUnit.SECONDS);
	}
	/**
	 * 获取值
	 * @param objectName
	 * @return
	 */
	public Long getLong(String objectName){
		RBucket<Long> bucket = this.getRBucket(objectName);
		return bucket.get();
	}
	/**
	 * 存入值
	 * @param objectName
	 * @param value
	 */
	public void setLong(String objectName,Long value){
		RBucket<Long> bucket = this.getRBucket(objectName);
		bucket.set(value);
	}
	/**
	 * 存入值,并设置多少秒之后失效。
	 * @param objectName
	 * @param value
	 * @param seconds
	 */
	public void setLong(String objectName,Long value,long seconds){
		RBucket<Long> bucket = this.getRBucket(objectName);
		bucket.set(value);
		bucket.expire(seconds, TimeUnit.SECONDS);
	}
	/**
	 * 获取值
	 * @param objectName
	 * @return
	 */
	public Double getDouble(String objectName){
		RBucket<Double> bucket = this.getRBucket(objectName);
		return bucket.get();
	}
	/**
	 * 存入值
	 * @param objectName
	 * @param value
	 */
	public void setDouble(String objectName,Double value){
		RBucket<Double> bucket = this.getRBucket(objectName);
		bucket.set(value);
	}
	/**
	 * 存入值,并设置多少秒之后失效。
	 * @param objectName
	 * @param value
	 * @param seconds
	 */
	public void setDouble(String objectName,Double value,long seconds){
		RBucket<Double> bucket = this.getRBucket(objectName);
		bucket.set(value);
		bucket.expire(seconds, TimeUnit.SECONDS);
	}
	/**
	 * 获取值
	 * @param objectName
	 * @param value
	 */
	public BigDecimal getBigDecimal(String objectName){
		RBucket<BigDecimal> bucket = this.getRBucket(objectName);
		return bucket.get();
	}
	
	/**
	 * 存入值
	 * @param objectName
	 * @param value
	 */
	public void setBigDecimal(String objectName,BigDecimal value){
		RBucket<BigDecimal> bucket = this.getRBucket(objectName);
		bucket.set(value);
	}
	/**
	 * 存入值,并设置多少秒之后失效。
	 * @param objectName
	 * @param value
	 * @param seconds
	 */
	public void setBigDecimal(String objectName,BigDecimal value,long seconds){
		RBucket<BigDecimal> bucket = this.getRBucket(objectName);
		bucket.set(value);
		bucket.expire(seconds, TimeUnit.SECONDS);
	}
	/**
	 * 获取值
	 * @param objectName
	 * @return
	 */
	public Object getObject(String objectName){
		RBucket<Object> bucket = this.getRBucket(objectName);
		return bucket.get();
	}
	/**
	 * 存入值
	 * @param objectName
	 * @param value
	 */
	public void setObject(String objectName, Object value){
		RBucket<Object> bucket = this.getRBucket(objectName);
		bucket.set(value);
	}
	/**
	 * 存入值,并设置多少秒之后失效。
	 * @param objectName
	 * @param value
	 * @param seconds
	 */
	public void setObject(String objectName, Object value,long seconds){
		RBucket<Object> bucket = this.getRBucket(objectName);
		bucket.set(value);
		bucket.expire(seconds, TimeUnit.SECONDS);
	}
	 /** 
     * 获取存放值对象的“箱子”，可存放字符串、对象、数字等值，相比传统的只能放字符串和数字的jedis，redisson支持更广泛更符合JAVA习惯的对象。 
     * @param objectName 
     * @return 
     */  
    public <T> RBucket<T> getRBucket(String objectName){  
        RBucket<T> bucket=redisson.getBucket(objectName); 
        return bucket;  
    }  
      
    /** 
     * 获取Map对象，可以像HashMap一样使用。但不要存放过大的Map集合，否则会影响整个redis的速度。
     * @param objectName 
     * @return 
     */  
    public <K,V> RMap<K, V> getRMap(String objectName){  
        RMap<K, V> map=redisson.getMap(objectName);
        return map;  
    }  
      
    /** 
     * 获取有序集合，可以用于排序
     * @param objectName 
     * @return 
     */  
    public <V> RSortedSet<V> getRSortedSet(String objectName){  
        RSortedSet<V> sortedSet=redisson.getSortedSet(objectName);  
        return sortedSet;  
    }
    
    /** 
     * 获取有序集合，根据权重分数排序，可以用于处理过百万的需要对比的数据，例如在100W个手机号码根据月消费来判断优先顺序（插入数据库开销太大了）。
     * @param objectName 
     * @return 
     */  
    public <V> RScoredSortedSet<V> getRScoredSortedSet(String objectName){  
        RScoredSortedSet<V> sortedSet=redisson.getScoredSortedSet(objectName);
        return sortedSet;  
    } 
      
    /** 
     * 获取集合 ，用来去重复，求交集、并集等操作。
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public <V> RSet<V> getRSet(String objectName){  
        RSet<V> rSet=redisson.getSet(objectName);  
        return rSet;  
    }  
      
    /** 
     * 获取列表 
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public <V> RList<V> getRList(String objectName){  
        RList<V> rList=redisson.getList(objectName);  
        return rList;  
    }  
      
    /** 
     * 获取队列 
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public <V> RQueue<V> getRQueue(String objectName){  
        RQueue<V> rQueue=redisson.getQueue(objectName);  
        return rQueue;  
    }  
      
    /** 
     * 获取双端队列 
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public <V> RDeque<V> getRDeque(String objectName){  
        RDeque<V> rDeque=redisson.getDeque(objectName);  
        return rDeque;  
    }  
      
    /** 
     * 获取阻塞队列
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    
    public <V> RBlockingQueue<V> getRBlockingQueue(String objectName){ 
        RBlockingQueue<V> rb=redisson.getBlockingQueue(objectName); 
        return rb; 
    }
      
    /** 
     * 获取锁，同时获取锁的线程有竞争关系，有可能是后启动的线程先获取到锁。竞争时间很短。
     * 推荐最安全的用法（避免永久死锁和永远等待）：
     *  RLock lock=....getLock(objectName);  
     *  boolean result = lock.tryLock(2, //第一个参数2表示2秒内获取锁，获取得到返回true,否则返回false，避免了线程永远等待锁（某些原因没解锁），可以设置为0或用只有2个参数的tryLock方法，无论锁定成功还是失败都是立即返回。
     *                          10,//第二参数表示假设10秒后还没调用unlock方法释放锁，自动释放。可以避免jvm进程被杀死导致的永久死锁（因为没有来得及调用unlock），自动解锁需要根据实际需要判断长度。
     *                          TimeUnit.SECONDS);
     *  if(result){//成功锁定，处理锁定后的逻辑
     *   ....
     *  }else{//锁定失败（表示已经被其他线程锁住），或跳出方法，或返回消息。
     *   ....
     *  }
     *   lock.unlock();//自己手工解锁，无需等待超时,但由于死机和进程杀死的问题，可能没被运行，所以保险起见，应该用自动解锁作为后补手段。
     * @param objectName 
     * @return 
     */  
    public RLock getRLock(String objectName){  
        RLock rLock=redisson.getLock(objectName);  
        return rLock;  
    }
    
    
    
    /**
     * 获取公平锁,按获取的先后顺序为优先级。
     * 推荐最安全的用法（避免永久死锁和永远等待）：
     *  RLock lock=....getFairRLock(objectName);  
     *  boolean result = lock.tryLock(1, //第一个参数1表示1秒内获取锁，获取得到返回true,否则返回false，避免了线程永远等待锁（某些原因没解锁），可以设置为0或用只有2个参数的tryLock方法，无论锁定成功还是失败都是立即返回。
     *                          10,//第二参数表示假设10秒后还没调用unlock方法释放锁，自动释放。可以避免jvm进程被杀死导致的永久死锁（因为没有来得及调用unlock），自动解锁需要根据实际需要判断长度。
     *                          TimeUnit.SECONDS);
     *  if(result){//成功锁定，处理锁定后的逻辑
     *   ....
     *  }else{//锁定失败（表示已经被其他线程锁住），或跳出方法，或返回消息。
     *   ....
     *  }
     *  lock.unlock();//自己手工解锁，无需等待超时,但由于死机和进程杀死的问题，可能没被运行，所以保险起见，应该用自动解锁作为后补手段。
     * @param objectName
     * @return
     */
    public RLock getFairLock(String objectName){  
        RLock rLock=redisson.getFairLock(objectName);  
        return rLock;  
    } 
    
    /** 
     * 获取原子数 ，可以直接使用getAndAdd和addAndGet方法来进行原子操作和返回结果。
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public RAtomicLong getRAtomicLong(String objectName){
        RAtomicLong rAtomicLong=redisson.getAtomicLong(objectName);  
        return rAtomicLong;  
    }  
    /**
     * 获取原子数  double类型。可以直接使用getAndAdd和addAndGet方法来进行原子操作和返回结果。
     * @param objectName
     * @return
     */
    public <T> RAtomicDouble getRAtomicDouble(String objectName){
    	RAtomicDouble rAtomicDouble = redisson.getAtomicDouble(objectName);
		return rAtomicDouble;
	}
    
    /** 
     * 获取记数锁 
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public RCountDownLatch getRCountDownLatch(String objectName){  
        RCountDownLatch rCountDownLatch=redisson.getCountDownLatch(objectName);  
        return rCountDownLatch;  
    }  
      
    /** 
     * 获取消息的Topic 
     * @param redisson 
     * @param objectName 
     * @return 
     */  
    public <M> RTopic<M> getRTopic(String objectName){  
         RTopic<M> rTopic=redisson.getTopic(objectName);  
         return rTopic;  
    }  
}
