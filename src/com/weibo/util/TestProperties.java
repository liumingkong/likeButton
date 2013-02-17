package com.weibo.util;

import static com.weibo.util.PropertiesUtil.readValue;


public class TestProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		/*
		InputStream in = ClassLoader.getSystemResourceAsStream("../conf/store.properties");
	
		Properties p = new Properties();  
		try {
			p.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println(p.get("name"));
		
		String path = TestProperties.class.getResource("").getFile();
		
		System.out.println(path);
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
	        System.out.println(TestProperties.class.getClassLoader().getResource(""));
	        System.out.println(ClassLoader.getSystemResource(""));
	        System.out.println(TestProperties.class.getResource(""));
	        System.out.println(TestProperties.class.getResource("/"));

	        System.out.println(new File("").getAbsolutePath());
	        System.out.println(System.getProperty("user.dir"));
	        
	       
		
		String path = new File("").getAbsolutePath()+File.separator
				+"conf"+File.separator+"store.properties";
		
		System.out.println(path);
		 */
		
		
		String url = readValue("url");
		System.out.println(url);
		
	}

}
