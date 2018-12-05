package com.benx.disruptor.quickstart;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class Main {
	
	public static void main(String[] args) {
		
		//参数准备工作
		OrderEventFactory orderEventFactory = new OrderEventFactory();
		int ringBufferSize = 1024*1024;
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		/**
		 * 1.orderEventFactory:消息(event)工厂对象
		 * 2.ringBufferSize: 容器的长度
		 * 3.executor:线程池(建议使用自定义线程池)  RejectedExecutionHandler(拒绝策略)
		 * 4.ProducerType:单生产者 还是 多生产者
		 * 5.WaitStrategy:等待策略
		 */
		//1.实例化disruptor对象
		Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(orderEventFactory, 
				ringBufferSize, 
				executor, 
				ProducerType.SINGLE,  //单  多
				new BlockingWaitStrategy());    // 阻塞的策略
		
		//2.添加消费者的监听    --- (消费者和disruptor进行关联)
		disruptor.handleEventsWith(new OrderEventHandler());
		
		//3.启动disruptor
		disruptor.start();
		
		//4.获取实际存储数据的容器: RingBuffer    做数据存储的是RingBuffer而不是disruptor
		RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
		
		//创建生产者
		OrderEventProducer producer = new OrderEventProducer(ringBuffer);
		
		ByteBuffer bb = ByteBuffer.allocate(8); //nio   用的不是new
		//投递
		for(long i = 0 ;i<= 100 ; i++ ) {
			bb.putLong(0, i);
			producer.sendData(bb);
		}
		
		
		disruptor.shutdown();
		executor.shutdown();
	}
	
}
