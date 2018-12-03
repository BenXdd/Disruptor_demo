package com.benx.disruptor.heigh;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 构建一个线程池用于提交任务
		ExecutorService es1 = Executors.newFixedThreadPool(4);
		ExecutorService es2 = Executors.newFixedThreadPool(5);// 有5个handler 需要5个  单消费者

		// 1.构建disruptor
		Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {
			@Override
			public Trade newInstance() {
				return new Trade();
			}
		}, 1024 * 1024, es2, ProducerType.SINGLE, new BusySpinWaitStrategy());

		// 2.把消费者设置到Disruptor中 handleEventsWith

		// 2.1串行操作
		/**
		 * disruptor .handleEventsWith(new Handler1()) .handleEventsWith(new Handler2())
		 * .handleEventsWith(new Handler3());
		 */

		// 2.2并行操作: 可以有两种方式:
		// 1.handleEventsWith方法 形参添加多个handler实现即可
		// 2.handleEventsWith方法 分别进行调用
		/**
		 * disruptor.handleEventsWith(new Handler1(), new Handler2(), new Handler3());
		 * // disruptor.handleEventsWith(new Handler2()); //
		 * disruptor.handleEventsWith(new Handler3());
		 */

		// 2.3菱形操作(一) CyclicBarrier
		/**
		 * disruptor.handleEventsWith(new Handler1(), new
		 * Handler2()).handleEventsWith(new Handler3());
		 */
		
		// 2.3菱形操作(二) 
		/**
		 * 
		 EventHandlerGroup<Trade> handleEventsWith = disruptor.handleEventsWith(new Handler1(), new Handler2());
		 handleEventsWith.then(new Handler3());
		 */
		 
		// 2.4 六边形操作  
		Handler1 h1 = new Handler1();
		Handler2 h2 = new Handler2();
		Handler3 h3 = new Handler3();
		Handler4 h4 = new Handler4();
		Handler5 h5 = new Handler5();
		disruptor.handleEventsWith(h1,h4);
		disruptor.after(h1).handleEventsWith(h2);
		disruptor.after(h4).handleEventsWith(h5);
		disruptor.after(h2,h5).handleEventsWith(h3);
		

		// 3.启动disruptor
		RingBuffer<Trade> ringBuffer = disruptor.start();

		CountDownLatch latch = new CountDownLatch(1);

		long begin = System.currentTimeMillis();

		es1.submit(new TradePublisher(latch, disruptor)); // submit是异步的

		latch.await(); // 进行向下

		disruptor.shutdown();
		es1.shutdown();
		es2.shutdown();

		System.out.println("总耗时: " + (System.currentTimeMillis() - begin));
	}

}
