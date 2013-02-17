package com.weibo.test;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.weibo.service.ActionServlet;

public class TestLike {
	
 	
	// click功能测试
	private static void TestClick(BigInteger feed_id,BigInteger like_uid){
		ActionServlet as = new ActionServlet();
		as.actionClick(feed_id,like_uid);
	}
	
	// 测试计数功能
	@SuppressWarnings("static-access")
	private static void TestshowCount(BigInteger feed_id){
		ActionServlet as = new ActionServlet();
		System.out.println(feed_id + "的计数:"+as.showCount(feed_id));
	}
	
	// 显示 用户的喜欢状态
	private static void TestshowStatus(BigInteger feed_id,BigInteger like_uid){
		ActionServlet as = new ActionServlet();
		Boolean ju = as.showStatus(feed_id,like_uid);		
		if(ju == true){
			System.out.println("用户"+like_uid+"表示喜欢"+feed_id);
		}else{
			System.out.println("用户"+like_uid+"表示不喜欢"+feed_id);
		}
	}
	
	// 显示订阅的用户列表
	@SuppressWarnings({ "static-access"})
	private static void TestShowFeedList(BigInteger feed_id,BigInteger like_uid){
		ActionServlet as = new  ActionServlet();
		List<BigInteger> list = as.showFeedList(feed_id,like_uid);
		Iterator<BigInteger> it = list.iterator();
		System.out.println("喜欢"+feed_id+"包括：");
		while(it.hasNext()){
			System.out.println(feed_id+":"+it.next());
		}
	}
	
	public static void TestClickVersion(BigInteger feed_id,BigInteger like_uid){
		TestLike.TestshowStatus(feed_id, like_uid);
		TestLike.TestshowCount(feed_id);
		TestLike.TestClick(feed_id,like_uid);
		TestLike.TestshowCount(feed_id);
		TestLike.TestshowStatus(feed_id, like_uid);
	}
	
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Random rand = new Random();		
		BigInteger feed_id = BigInteger.valueOf(rand.nextInt(100));
		BigInteger like_uid = BigInteger.valueOf(rand.nextInt(100));
		
		
		// 测试click的功能
		// 首先是100次的点击
		/*
		StopWatch sw = new LoggingStopWatch();
		for(int i=0;i<10000;i++){
			feed_id = BigInteger.valueOf(rand.nextInt(100));
			like_uid = BigInteger.valueOf(rand.nextInt(100));
			TestLike.TestClickVersion(feed_id,like_uid);
		}
		sw.stop("example1","custom");	
		*/
		// 测试这个feed的计数
		//TestLike.TestshowCount(feed_id);
		// 测试这个feed的该like用户的状态
		//TestLike.TestshowStatus(feed_id, like_uid);
		// 测试这个feed的所有like用户列表
		StopWatch sw = new LoggingStopWatch();
		for(int i=0;i<10;i++){
			feed_id = BigInteger.valueOf(rand.nextInt(100));
			like_uid = BigInteger.valueOf(rand.nextInt(100));
			TestLike.TestShowFeedList(feed_id, like_uid);
			}
			sw.stop("example1","custom");	
		}

}
