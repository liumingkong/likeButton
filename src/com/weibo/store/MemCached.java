package com.weibo.store;

import java.util.Date;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
 

public class MemCached {

	// create single instance for MemCacheClient
	protected static MemCachedClient mcc = new MemCachedClient();  
	
	// create single instance MemCached
	protected static MemCached memCached = new MemCached();  
	     
	private static String serverAddress = "192.168.1.102";
	private static String serverPort = "11211";
	private static Integer serverWeight = 3;
	
	    // set and cache the server  
	    static {  
	        
	    	//server list and the weight of server
	        String[] servers = {serverAddress+":"+serverPort};  
	        Integer[] weights = {serverWeight};  
	  
	        // get the instance of the socket pool 
	        SockIOPool pool = SockIOPool.getInstance();  
	  
	        // set the servers's informations
	        pool.setServers(servers);  
	        pool.setWeights(weights);  
	  
	        // set the count of initial,min,max connector and max deal-time
	        pool.setInitConn( 5 );  
	        pool.setMinConn( 5 );  
	        pool.setMaxConn( 250 );  
	        pool.setMaxIdle( 1000 * 60 * 60 * 6 );  
	  
	        // set the sleep-time of the main thread  
	        pool.setMaintSleep(5);  
	  
	        // set the parameters of the tcp   
	        pool.setNagle( false );  
	        pool.setSocketTO( 3000 );  
	        pool.setSocketConnectTO( 0 );  
	  
	        // initialize the connector pool
	        pool.initialize();  	  
	    }  
	     
	    // don't allow to instantiate
	    protected MemCached()  
	    {  	         
	    }  
	     
	    /** 
	     * get the instance  
	     * @return 
	     */  
	    public static MemCached getInstance()  
	    {  
	        return memCached;  
	    }  
	     
	    /** 
	     * add a key-value to memCache 
	     * @param key 
	     * @param value 
	     * @return 
	     */  
	    public boolean add(String key, Object value)  
	    {  
	        return mcc.add(key, value);  
	    }  
	     
	    public boolean add(String key, Object value, Date expiry)  
	    {  
	        return mcc.add(key, value, expiry);  
	    }  
	     
	    public boolean replace(String key, Object value)  
	    {  
	        return mcc.replace(key, value);  
	    }  
	     
	    public boolean replace(String key, Object value, Date expiry)  
	    {  
	        return mcc.replace(key, value, expiry);  
	    }  
	     
	    /** 
	     * get value by key
	     * @param key 
	     * @return 
	     */  
	    public Object get(String key)  
	    {  
	        return mcc.get(key);  
	    } 
}