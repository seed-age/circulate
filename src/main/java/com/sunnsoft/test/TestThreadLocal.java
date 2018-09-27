package com.sunnsoft.test;

public class TestThreadLocal {

	private static final ThreadLocal < Integer > unique = new ThreadLocal<Integer>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					unique.set(1);
					System.out.println("t1:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					unique.set(2);
					System.out.println("t1:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					unique.set(3);
					System.out.println("t1:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					unique.set(4);
					System.out.println("t1:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					unique.set(5);
					System.out.println("t1:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					unique.set(6);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					System.out.println(unique.get());
					System.out.println("t2:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					System.out.println(unique.get());
					System.out.println("t2:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					System.out.println(unique.get());
					System.out.println("t2:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					System.out.println(unique.get());
					System.out.println("t2:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					System.out.println(unique.get());
					System.out.println("t2:"+UniqueThreadIdGenerator.getCurrentThreadId());
					Thread.sleep(2000);
					System.out.println(unique.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();

	}

}
