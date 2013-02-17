package com.weibo.test;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.weibo.service.ActionServlet;

public class TestTime {

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
	@SuppressWarnings({ "static-access", "unused"})
	private static void TestShowFeedList(BigInteger feed_id,BigInteger like_uid){
		ActionServlet as = new ActionServlet();
		List<BigInteger> list = as.showFeedList(feed_id,like_uid);
		Iterator<BigInteger> it = list.iterator();
		System.out.println("喜欢"+feed_id+"包括：");
		while(it.hasNext()){
			System.out.println("4324324"+":"+it.next());
		}
	}
	
	public static void TestClickVersion(BigInteger feed_id,BigInteger like_uid){
		TestTime.TestshowStatus(feed_id, like_uid);
		TestTime.TestshowCount(feed_id);
		TestTime.TestClick(feed_id,like_uid);
		TestTime.TestshowCount(feed_id);
		TestTime.TestshowStatus(feed_id, like_uid);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BigInteger feed_id = BigInteger.valueOf(4324324);
		BigInteger like_uid = BigInteger.valueOf(6);
		
		int i =0;
		StopWatch sw = new LoggingStopWatch();
		// 测试这个feed的计数
		//TestTime.TestshowCount(feed_id);
		// 测试这个feed的该like用户的状态
		while(i<100){
			TestTime.TestshowStatus(feed_id, like_uid);
			System.out.println("i:"+i);
			i++;
		}
		sw.stop("example1","custom");		
	}

}
