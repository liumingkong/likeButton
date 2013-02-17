package com.weibo.test;

import com.weibo.store.MemCached;

public class TestMC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MemCached mc = MemCached.getInstance();
		mc.add("come", "here");
		mc.add("come1", "here1");
		
		System.out.println("come"+"="+mc.get("come"));
		System.out.println("come1"+"="+mc.get("come1"));
		
	}

}
