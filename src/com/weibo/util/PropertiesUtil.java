package com.weibo.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {

	// the file of the properties file 
	private static final String FILE_NAME = new File("").getAbsolutePath()+
			File.separator+"conf"+File.separator+"store.properties";

	public static String readValue(String key){
		// please don't set it static
		// because it will cause the variable always be alive in memory
		Properties props = new Properties();
		try {
			InputStream in= new BufferedInputStream(new FileInputStream(FILE_NAME));			
			props.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		String value = props.getProperty(key);		
		return value;
	}
	
	public static void writeValue(String key,String value){
		// please don't set it static
		// because it will cause the variable always be alive in memory
		Properties props = new Properties();
		try {
			File file = new File(FILE_NAME);
			if(!file.exists()){
				file.createNewFile();

			}
			InputStream in = new FileInputStream(file);			
			props.load(in);
			// notice;we must close the input stream before writing
			in.close();
			
			OutputStream fos = new FileOutputStream(file);
			props.setProperty(key, value);
			props.store(fos,"Update`"+key+"`value");
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
