package com.benx.disruptor.heigh;

import java.util.UUID;

import com.lmax.disruptor.EventHandler;

public class Handler2 implements EventHandler<Trade>{

	@Override
	public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handl 2: SET ID");
		event.setId(UUID.randomUUID().toString());
		Thread.sleep(2000);
	}

}
