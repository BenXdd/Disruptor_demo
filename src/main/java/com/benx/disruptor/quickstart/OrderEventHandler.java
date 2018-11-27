package com.benx.disruptor.quickstart;

import com.lmax.disruptor.EventHandler;

/**
 * 消费者
 * @author BenX
 *
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

	@Override
	public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("消费者: "+ event.getValue());
	}
	
}
