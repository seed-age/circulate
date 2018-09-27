package com.sunnsoft.sloa.util.redis.redisson;

import java.util.concurrent.TimeUnit;

/**
 * 处理加锁逻辑的类，本方法主要作用是用来短暂锁定多个JVM都需要访问和占用的资源，例如抽奖。如果只是单单使用关系数据库字段标识来锁定，
 * 无法解决多线程和多JVM的导致的锁被覆盖失效的问题（因为数据库字段获取和变更不是原子操作）。 * 因此，引入redis来短暂在jvm之间共享锁，
 * 在短暂锁定的情况下，设置长效的数据库字段锁，这样使得数据库字段锁“看起来”像是原子锁。
 * @author llade
 *
 */
public abstract class Lock {
	
	public final long DEFAULT_WAIT_TIME_OUT = 0;
	
	public final long DEFAULT_AUTO_RELEASE_TIME = 60;
	
	/**
	 * 超时时间，超过这个时间则认定无法被锁定。默认值0，即无法锁定则马上调用fail()方法。
	 * @return
	 */
	public long getWaitTimeOut(){
		return DEFAULT_WAIT_TIME_OUT;
	}
	
	/**
	 * 自动释放锁的时间，默认值时间是60秒，如果自动释放时间之内success方法还没跑完，会redis会自动解锁，主要作用是避免JVM进程被杀死导致的永久死锁。 
	 * @return
	 */
	public long getAutoReleaseTime(){
		return DEFAULT_AUTO_RELEASE_TIME;
	}
	/**
	 * 等待超时和自动释放两种情况所使用的时间单位。
	 * @return
	 */
	public TimeUnit getTimeUnit(){
		return TimeUnit.SECONDS;
	}
	/**
	 * 锁定成功后要实现的方法。success()方法完成之后，会自动调用Redisson的unlock进行解锁。如果设置了自动解锁的时间，可能success方法还没完成，已经提前解锁了
	 * ，所以需要注意，如有必要，可以改写 getAutoReleaseTime()方法来延长自动解锁时间，默认60秒自动解锁。
	 * 
	 */
	public abstract void success();
	/**
	 * 无法锁定的，要实现的方法。可不做任何动作。
	 */
	public abstract void fail();
}
