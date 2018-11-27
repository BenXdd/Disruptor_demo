package com.benx.disruptor.heigh;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Disruptor 中的Event
 * @author BenX
 *
 */
public class Trade {

	private String id;
	private String name;
	private double price;
	private AtomicInteger count = new AtomicInteger(0); //当多线程访问时,实现线程的安全
	
	public Trade() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public AtomicInteger getCount() {
		return count;
	}
	public void setCount(AtomicInteger count) {
		this.count = count;
	}

	
}