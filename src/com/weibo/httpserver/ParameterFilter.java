package com.weibo.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/*
 * Description:
 * <br/>this class is used to parse the parameters of the http request
 * 
 */

public class ParameterFilter extends Filter {

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Parse the requuests URI for parameters";
	}

	@Override
	public void doFilter(HttpExchange e, Chain chain) throws IOException {
		// TODO Auto-generated method stub
		parseGetParameters(e);
		parsePostParameters(e);
		chain.doFilter(e);
	}
	
	private void parseGetParameters(HttpExchange exchange)throws UnsupportedEncodingException{
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		URI requestUri = exchange.getRequestURI();
		String query = requestUri.getRawQuery();
		parseQuery(query,parameters);
		exchange.setAttribute("parameters", parameters);
	}

	private void parsePostParameters(HttpExchange exchange)throws  IOException{
		
		if("post".equalsIgnoreCase(exchange.getRequestMethod())){
			Map<String,Object> parameters = new HashMap<String,Object>();
			InputStreamReader isr = new InputStreamReader(
					exchange.getRequestBody(),"utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
			parseQuery(query,parameters);			
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseQuery(String query,Map<String,Object> parameters)throws UnsupportedEncodingException{
		if(query != null){
			String pairs[] = query.split("[&]");
			
			for(String pair : pairs){
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				
				if(param.length>0){
					key = URLDecoder.decode(param[0],
							System.getProperty("file.encoding"));
				}
				
				if(param.length>1){
					value = URLDecoder.decode(param[1],
							System.getProperty("file.encoding"));
				}
				
				if(parameters.containsKey(key)){
					Object obj = parameters.get(key);
					if(obj instanceof List<?>){
						List<String> values = (List<String>)obj;
						values.add(value);
					}else if(obj instanceof String){
						List<String> values = new ArrayList<String>();
						values.add((String)obj);
						values.add(value);
						parameters.put(key, values);
					}
				}else{
					parameters.put(key, value);
				}
			}
		}
	}
}
