package com.benx.disruptor.heigh.multi;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.lmax.disruptor.WorkHandler;

/**
 * 多消费者一定要实现的接口 WorkHandler
 * @author BenX
 *
 */
public class Consumer implements WorkHandler<Order>{
	
	private String consumerId;
	
	//计数统计
	private static AtomicInteger count = new AtomicInteger(0);
	
	private Random random = new Random();
	
	public Consumer(String consumerId) {
		this.consumerId = consumerId;
	}
	
	@Override
	public void onEvent(Order event) throws Exception {
		Thread.sleep(1*random.nextInt(5)); //每做一个消息的消费需要1~4毫秒
		System.out.println("当前消费者: "+ this.consumerId + "消费信息ID: "+ event.getId());
		
		count.incrementAndGet();
	}
	
	public int getCount(){
		return count.get();
	}
}
