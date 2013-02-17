package com.weibo.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class HTTPServerLike {
	
	private static HttpServer hs = null;
	
	/**
	 * Description:
	 * start the HTTPserver 
	 * we need a console to manage the server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {			
			//set the HttpServer's port 8888 and the backlog
			hs = HttpServer.create(new InetSocketAddress(8888),0);
			
			//use handler to deal the request
			HttpContext context = hs.createContext("/test",new LikeHttpServer());
			
			// set the filter to deal the parameters
			context.getFilters().add(new ParameterFilter());
			
			// in the document,it tells us the method is used for 
			// setting this server's Executor object.
			// an executor must be established before start is called
			// all HTTP requests are handled in tasks given to the executor.
			// if the method is not called or a null Executor.
			// then a default implements is used,
			// which uses the thread which was created by the start method
			ExecutorService pool = Executors.newFixedThreadPool(20);
			
			// creates a implement executor
			hs.setExecutor(pool); 
			// start the server
			hs.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
