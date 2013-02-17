package com.weibo.test;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import com.weibo.store.C3P0Pool;

public class DataManagement {

	
	public static void createDataFriend(BigInteger fromUid,BigInteger toUid){
		
		Connection conn = C3P0Pool.getConnection();
		
		String sql = "insert into friends(from_uid,to_uid)"
				+"value(?,?)";
		
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1,fromUid);
			ps.setObject(2,toUid);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// skip the exception
			// avoid to insert the duplicated data so that 
			// the program is stopped ,because of exception
		}
		C3P0Pool.free(ps, conn);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Random rand = new Random();
		for(int i=0;i<1000;i++){
			createDataFriend(BigInteger.valueOf(rand.nextInt(100)),
					BigInteger.valueOf(rand.nextInt(100)));
		}
		
	}

}
