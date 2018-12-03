package com.benx.disruptor.heigh.multi;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		
		//1.创建RingBuffer
		RingBuffer<Order>  ringBuffer = 
				RingBuffer.create(ProducerType.MULTI,
						new EventFactory<Order>() {
							@Override
							public Order newInstance() {
								return new Order();
							}
						}, 
						1024*1024, 
						new YieldingWaitStrategy());
		
		//2.通过RingBuffer 创建一个序号屏障
		SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
		
		//3.创建多个消费者数组
		Consumer[] consumers = new Consumer[10];
		for (int i = 0; i < consumers.length; i++) {
			consumers[i] = new Consumer("C" + i);
		}
		
		//4.构建多消费者工作池
		WorkerPool<Order> workerPool = new WorkerPool<>(
				ringBuffer, 
				sequenceBarrier, 
				new EventExceptionHandler(), 
				consumers);
		
		//5.设置多个消费者的sequence序号 用于单独统计消费进度,并且设置到ringbuffer中
		//		workerPool.getWorkerSequences()获得消费者中每个sequence
		ringBuffer.addGatingSequences(workerPool.getWorkerSequences());  
		
		//6.启动workpool
		workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		for (int i = 0; i < 100; i++) {
			Producer producer = new Producer(ringBuffer);		
			//异步
			new Thread(new Runnable() {		
				@Override
				public void run() {
					try {
						latch.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
					for (int j = 0; j < 100; j++) {
						producer.sendData(UUID.randomUUID().toString());
					}
				}
			}).start();
			
		}
			
			Thread.sleep(2000);
			System.err.println("------------线程创建完毕,开始生产数据-------------");
			latch.countDown();
			
			Thread.sleep(5000);
			System.err.println("任务总数: "+ consumers[2].getCount());
			
		
	}
	
	static class EventExceptionHandler implements ExceptionHandler<Order>{
		
		/**
		 * 进行消费的时候发生异常
		 */
		@Override
		public void handleEventException(Throwable ex, long sequence, Order event) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * 刚启动的时候
		 */
		@Override
		public void handleOnStartException(Throwable ex) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * 刚结束的时候
		 */
		@Override
		public void handleOnShutdownException(Throwable ex) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
