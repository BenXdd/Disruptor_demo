package com.benx.disruptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public class Review {

	public static void main(String[] args) throws Exception {
		
		//1.ConcurrentHashMap
		/**
		 * private static final int DEFAULT_CAPACITY = 16;  
		 * 	有16份,每一份都是数组形式的
		 * 
		 * private static final ObjectStreamField[] serialPersistentFields = {
		        new ObjectStreamField("segments", Segment[].class),
		        new ObjectStreamField("segmentMask", Integer.TYPE),
		        new ObjectStreamField("segmentShift", Integer.TYPE)
    		};
    		
    		Segment[]
    		
    		
    		 static class Segment<K,V> extends ReentrantLock implements Serializable {
		        private static final long serialVersionUID = 2249069246763182397L;
		        final float loadFactor;
		        Segment(float lf) { this.loadFactor = lf; }
    		}
    		
    		
    		Segment继承了ReentrantLock,它肯定是要加锁的, 
    		高并发的时候,多线程操作的是不同的Segment,是可以支持高并发的读写的
    		,要是两个线程操作到同一个Segment上了, 就只能等待了,因为它是分段锁,减小锁的力度
    		
		 */
		//ConcurrentHashMap<String,String> map = new ConcurrentHashMap<String , String>();
		
		
		
		//2.CopyOnWrite  
		/**
		  *  写时复制 读写分离的思想  在写的时候 会把原容器copy一份,成为原容器的副本,
		  *  一个线程在对容器进行操作时,总是操作它的副本,而原先的那个容器是支持读的

		  *  不太适合写特别多的场景   不太适合数据量多的场景,做一个读写操作的时候需要先copy
		  *  
		  *  适合场景: 读多写少, 容量不是特别大,尽量保证在1000个元素以内
		 * 
		 * 
		 * public boolean add(E e) {
		        final ReentrantLock lock = this.lock;   // 加了锁
		        lock.lock(); 							//先上锁
		        try {
		            Object[] elements = getArray();	    //getArray()  在成员变量里的Object[] 数组
		            int len = elements.length;
		            Object[] newElements = Arrays.copyOf(elements, len + 1);  //把原容器copy一份
		            newElements[len] = e;     			//操作的是新的副本
		            setArray(newElements);				//修改成员变量里的那个Object[] 数组
		            return true;
		        } finally {
		            lock.unlock();						//在finally代码块 开锁
		        }
		    }
		    
		    add操作是把原容器copy一份,  然后操作新的副本
		 */
		
		//CopyOnWriteArrayList<String> cowal = new CopyOnWriteArrayList<String>();
		//CopyOnWriteArraySet<String> cowsl = new CopyOnWriteArraySet<String>();
		//cowal.add("aaa");
		
		
		//3.Atomic
		//AtomicLong count = new AtomicLong(1);
		//boolean flag = count.compareAndSet(0, 2);//(expect, update)  
		/**
		 * 只有except是1的时候才能更新成功
		 * 因为是原子操作  
		 */
		//System.err.println(flag);  //false
		//System.err.println(count.get());
		
		
		
		/**
		 * wait 方法释放锁
		 * notify 不释放锁
		 */
		Object lock = new Object();
		Thread A = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int sum = 0;
				for (int i = 0; i < 10; i++) {
					sum += i;
				}
				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.err.println("sum: "+ sum);
			}
		});
		
		A.start();
		
		Thread.sleep(2000);
		
		synchronized (lock) {
			lock.notify();
		}
	}
}
