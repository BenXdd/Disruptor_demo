package com.benx.disruptor.heigh;

import com.couchbase.client.deps.com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.EventHandler;

public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade>{
	
	//EventHandler
	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		this.onEvent(event);
	}
	
	//WorkHandler
	@Override
	public void onEvent(Trade event) throws Exception {
		System.err.println("handl 1: SET NAME");
		event.setName("H1");
		Thread.sleep(1000);
	}

}
