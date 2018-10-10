package com.sunnsoft;
import java.util.concurrent.atomic.AtomicInteger;

 public class UniqueThreadIdGenerator {

     private static final AtomicInteger uniqueId = new AtomicInteger(0);

     private static final ThreadLocal < Integer > uniqueNum = 
         new ThreadLocal < Integer > () {
             @Override protected Integer initialValue() {
                 return uniqueId.getAndIncrement();
         }
     };
 
     public static int getCurrentThreadId() {
//         return uniqueId.get();//我靠，oracle的官方API文档里面的例子是错误的。下面才是正确的代码。
    	 return uniqueNum.get();
     }
 } // UniqueThreadIdGenerator