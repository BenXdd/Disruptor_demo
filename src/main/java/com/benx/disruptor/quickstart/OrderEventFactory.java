package com.benx.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

public class OrderEventFactory implements EventFactory<OrderEvent>{

	@Override
	public OrderEvent newInstance() {
		// 这个方法为了返回空的数据对象
		return new OrderEvent();  
	}

	
}
