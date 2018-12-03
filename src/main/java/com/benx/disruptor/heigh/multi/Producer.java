package com.benx.disruptor.heigh.multi;

import com.lmax.disruptor.RingBuffer;

public class Producer {

	private RingBuffer<Order> ringBuffer;

	public Producer(RingBuffer<Order> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void sendData(String uuid) {

		long sequence = ringBuffer.next();
		try {
			//刚new出来 没有任何属性的order
			Order order = ringBuffer.get(sequence);
			order.setId(uuid);
		} finally  {
			//发布一个序号 让对应消费端收到
			ringBuffer.publish(sequence);
		}
	}

	
	
}
