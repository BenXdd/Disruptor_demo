package com.benx.disruptor.quickstart;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

public class OrderEventProducer {

	private RingBuffer<OrderEvent> ringBuffer;

	public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {

		this.ringBuffer = ringBuffer;
	}

	// 生产者 需要有投递数据的方法
	public void sendData(ByteBuffer data) {
		// 1.在生产者发送消息的时候, 需要从ringBuffer 获取一个可用的序号
		long sequence = ringBuffer.next();
		try {
			// 2.根据这个序号,找到具体 "OrderEvent"元素 注意:此时获取的OrderEvent对象是一个没有被填充的对象
			OrderEvent event = ringBuffer.get(sequence);
			// 3.进行实际的赋值处理
			event.setValue(data.getLong(0));
		} finally {
			// 4.提交操作(发布)
			ringBuffer.publish(sequence);
		}

	}
}
