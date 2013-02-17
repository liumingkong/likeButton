package com.weibo.test;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.weibo.store.C3P0Pool;
import com.weibo.store.JdbcUtils;

public class TestJdbc {

	public static void test1(){
		Connection conn = C3P0Pool.getConnection();
		//Connection conn = JdbcUtils.getConnection();
		String sql = "select * from feed";
		Statement st = null;
		ResultSet rs = null;
		try {
			 st = conn.createStatement();
			
			 rs = st.executeQuery(sql);
			
			while(rs.next()){
				System.out.println(rs.getInt("feed_id")+":"+rs.getInt("like_uid"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcUtils.free(rs, st, conn);
	}

	public static void test2(){
		Connection conn = JdbcUtils.getConnection();
		String sql = "select like_uid from feed where feed_id = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1,BigInteger.valueOf(4324324));
			rs = ps.executeQuery();	
			while(rs.next()){
				System.out.println("feed_id"+":"+rs.getInt("like_uid"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcUtils.free(rs, ps, conn);
	}
	
	public static void test3(){
		Connection conn = C3P0Pool.getConnection();
		String sql = "select like_uid from feed where feed_id = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1,BigInteger.valueOf(4324324));
			rs = ps.executeQuery();	
			while(rs.next()){
				System.out.println("feed_id"+":"+rs.getInt("like_uid"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C3P0Pool.free(rs, ps, conn);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StopWatch sw = new LoggingStopWatch();
		test1();
		sw.stop("example1","custom");
	}

}
