package com.benx.disruptor.heigh;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 使用disruptor直接提交
 * 实现了runnable接口的publish类
 * 引用了disruptor,用一种新的方式去提交数据,就是我们new出来一个translator类 
 * @author BenX
 *
 */
public class TradePublisher implements Runnable {

	private CountDownLatch latch;

	private Disruptor<Trade> disruptor;
	
	private static int PUBLISH_COUNT = 1;

	public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {

		this.latch = latch;
		this.disruptor = disruptor;
	}

	@Override
	public void run() {
		// 在run方法里想提交一个数据

		TradeEventTranslator eventTranslator = new TradeEventTranslator();

		for (int i = 0; i < PUBLISH_COUNT; i++) {
			// 新的提交的任务的方式
			disruptor.publishEvent(eventTranslator);
		}
		//数据提交完成后执行  主函数的 latch.await()才能向下执行
		latch.countDown();
	}

}

class TradeEventTranslator implements EventTranslator<Trade> {

	private Random random = new Random();

	/**
	 * 在quickstart中, ringbuffer需要new一个event对象 并且是未填充的,
	 * 我们这里形参里自动给了一个未填充的event对象,同时告诉你sequence,(相当于ringbuffer的简版) ringbuffer中
	 * 需要next()到一个sequence 然后在get到这个空的event
	 */

	@Override
	public void translateTo(Trade event, long sequence) {
		this.generateTrade(event);
	}

	private void generateTrade(Trade event) {
		event.setPrice(random.nextDouble() * 9999);
	}

}