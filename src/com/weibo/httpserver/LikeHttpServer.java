package com.weibo.httpserver;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.weibo.service.ActionServlet;

public class LikeHttpServer implements HttpHandler {

	//private static int i =0 ;
	@Override
	public void handle(HttpExchange t) throws IOException {
		// TODO Auto-generated method stub
		// because we need to test the function by ab
		// but this test tool can not make different parameters easily。

		Random rand = new Random();	
		BigInteger feed_id = BigInteger.valueOf(rand.nextInt(100));
		//BigInteger feed_id = BigInteger.valueOf(0);
		BigInteger like_uid = BigInteger.valueOf(rand.nextInt(100));
		String response = null;
		
		@SuppressWarnings("unchecked")
		Map<String,Object> params = (Map<String,Object>)t.getAttribute("parameters");
		String choose = (String) params.get("tag");
		if("tc".equalsIgnoreCase(choose)){
			new ActionServlet().actionClick(feed_id,like_uid);
			response = "<h3>user("+like_uid+")click the feed("+feed_id+")</h3>";
			this.dealResponse(t,response);
			
		}else if("ss".equalsIgnoreCase(choose)){
			// for the function of showStatus
			Boolean b = new ActionServlet().showStatus(feed_id, like_uid);
			//i++;
			//System.out.println("ss"+i);
			if(b){
				response = "<h3>user("+like_uid+")like feed("+feed_id+")</h3>";
			}else{
				response = "<h3>user("+like_uid+")don't like feed("+feed_id+")</h3>";
			}
			this.dealResponse(t,response);
			
		}else if("sc".equalsIgnoreCase(choose)){
			// for the function of showStatus
			int count = ActionServlet.showCount(feed_id);
			response = "<h3>the count of feed("+feed_id+") is "+count+"</h3>";
			this.dealResponse(t,response);
			
		}else if("sl".equalsIgnoreCase(choose)){
			List<BigInteger> list = ActionServlet.showFeedList(feed_id, like_uid);
			Iterator<BigInteger> it = list.iterator();
			response = "<h3>the userList of the feed("+feed_id
					+") by user("+like_uid+")</h3><ol>";
			while(it.hasNext()){
				response = response+"<li>"+it.next()+"</li>";
			}
			response = response+"</ol>";
			this.dealResponse(t, response);
			
		}else{
			// if just click but not has the tag
			response = new String("<h3>please use a like</h3>");
			this.dealResponse(t, response);
		}
	}
	
	private void dealResponse(HttpExchange t,String response) throws IOException{
		t.sendResponseHeaders(200,response.length());
		PrintStream ps = new PrintStream(t.getResponseBody());
		ps.println(response);
		ps.close();
	}
 
}
