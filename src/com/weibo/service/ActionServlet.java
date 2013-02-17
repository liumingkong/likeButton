package com.weibo.service;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.perf4j.aop.Profiled;

import com.weibo.store.C3P0Pool;
import com.weibo.store.MemCached;

/*
 * Description:
 * <br/>Copyright (C),2012
 * <br/>This class is used for LikeAction.
 * @author LZ
 * @version 1.0
 */
public class ActionServlet {

	private static MemCached cache = MemCached.getInstance();

	/*
	 * Description:
	 * <br/>when user click the button
	 * <br/>we will judge whether the user like the feed at first.
	 * <br/>then by the result of the judgment, it decides how to deal
	 * <br/>just a simple jump
	 * @param feed_id 
	 * @param like_uid
	 * @return void
	 */
	@Profiled(tag="tagClick")
	public void actionClick(BigInteger feed_id,BigInteger like_uid){
		
		//if the result is true,it means the user liked the feed
		//click the button is order to cancel feed,he will not like it
		if(this.showStatus(feed_id, like_uid)==true){
			ActionServlet.delCount(feed_id, like_uid);
		}else{
			//if the result is false,it means the user didn't like the feed
			//click the button is order to feed,he will like it
			ActionServlet.addCount(feed_id, like_uid);
		}
	}
	
	/*
	 * Description:
	 * <br/>this method is to show whether the user like the feed
	 * <br/>if the return is true,the user like it
	 * <br/>if the return is false,the user don't like it 
	 * @param feed_id 
	 * @param like_uid
	 * @return boolean
	 */
	@Profiled(tag="showStatus")
	public boolean showStatus(BigInteger feed_id,BigInteger like_uid){

		// At first,we need judge whether we can find the feed in the mc by feed_id
		// if we can't find the feed in the mc,we need to build new feed from database.
		Feed feed = findFeed(feed_id);
		boolean judge = feed.showFeedList().contains(like_uid);
		return judge; 
	}
	
	/*
	 * Description:
	 * <br/>this method is to show the count of the feed,how many people like the feed
	 * @param feed_id 
	 * @return int
	 */
	@Profiled(tag="showCount")
	public static int showCount(BigInteger feed_id){
		return findFeed(feed_id).showCount();		
	}
		
	/*
	 * Description:
	 * <br/>this method is to show the list of the feed,how many people like the feed
	 * <br/>we also need to list the friend list of the likeUid at first
	 * @param feed_id 
	 * @param like_uid 
	 * @return list
	 */
	@Profiled(tag="showFeedList")
	public static List<BigInteger> showFeedList(BigInteger feed_id,BigInteger like_uid){
		
		// At first,we need judge whether we can find the feed in the mc by feed_id
		// if we can't find the feed in the mc,we need to build new feed from database.
		Feed feed = findFeed(feed_id);
		// get the list of the feed,who likes the feed.
		List<BigInteger> aList = feed.showFeedList();
		
		// this test code help us to show list
		/*
		Iterator<BigInteger> it1 = aList.iterator();
		while(it1.hasNext()){
			System.out.println("喜欢"+feed_id+"的人："+it1.next());
		}
		*/
		// we need to find the user in the mc at first
		// in the mc, key:like_uid,value:the user object
		// notice:I think the key may be duplicated 
		// so we need to adjust the key
		// key:"u"+like_uid value:user
		// key:"f"+feed_id value:feed
		User user = findFriend(like_uid);
		// get the friend list of the user
		List<BigInteger> frList = user.getFriendList();
		
		/*
		Iterator<BigInteger> it2 = frList.iterator();
		while(it2.hasNext()){
			System.out.println(like_uid+"的好友:"+it2.next());
		}
		*/
		// we need to keep the list is the newest,and update
		// get the result list
		List<BigInteger> list =sortList(aList,frList);
		return list;
	}
	
	/*
	 * Description:
	 * <br/>this method is to add the count and user to the feed
	 * @param feed_id 
	 * @param like_uid
	 * @return void
	 */
	@Profiled(tag="addCount")
	private static void addCount(BigInteger feed_id,BigInteger like_uid){
		
		
		// At first,we need judge whether we can find the feed in the mc by feed_id
		// if we can't find the feed in the mc,we need to build new feed from database.
		// Maybe we think this step is duplicated
		// in fact,we should ensure the feed still is saved in the mc.
		Feed feed = findFeed(feed_id);

		// add the count of the feed and add the user to list of the feed
		feed.addCount(like_uid);
			
		// add the new feed to the mc
		// notice:this time,we need use the replace method
		// if we use the add method,maybe the same key is saved to the mc
		cache.replace("f"+String.valueOf(feed_id),feed);
		
		// get a connection from the pool
		Connection conn = C3P0Pool.getConnection();
		PreparedStatement ps = null;
		// save the id of the user to feed table. 
		String sql = "replace into feed(feed_id,like_uid) values(?,?) ";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1,feed_id);
			ps.setObject(2,like_uid);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// return the connection to the pool
		C3P0Pool.free(ps, conn);		
	}
	
	/*
	 * Description:
	 * <br/>this method is to delete the count and user to the feed
	 * @param feed_id 
	 * @param like_uid
	 * @return void
	 */
	@Profiled(tag="delCount")
	private static void delCount(BigInteger feed_id,BigInteger like_uid){

		// At first,we need judge whether we can find the feed in the mc by feed_id
		// if we can't find the feed in the mc,we need to build new feed from database.
		Feed feed = findFeed(feed_id);

		// delete the count of the feed and delete the user to list of the feed	
		feed.delCount(like_uid);
		
		// add the new feed to the mc
		cache.replace("f"+String.valueOf(feed_id),feed);
		
		Connection conn = C3P0Pool.getConnection();
		PreparedStatement ps = null;
		
		// delete the id of the user to feed table. 
		String sql = "delete from feed where feed_id = ? and like_uid = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1,feed_id);
			ps.setObject(2,like_uid);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		C3P0Pool.free(ps, conn);
	}
	
	/*
	 * Description:
	 * <br/>this method is to find the feed in the mc by feed_id
	 * <br/>if the result is true,it means the user liked the feed
	 * <br/>if the result is false,it means the user didn't like the feed
	 * <br/>I think this method need synchronized to avoid the create same feed
	 * @param feed_id 
	 * @return Feed
	 */
	@Profiled(tag="findFeed")
	private static Feed findFeed(BigInteger feed_id){
		
		//System.out.println("findFeed start"+feed_id);
		// find the feed from the mc
		Feed feed = (Feed) cache.get("f"+String.valueOf(feed_id));
		
		// if we can't find the feed in mc
		// we need to create new feed
		if(feed==null){
			//System.out.println("createNewFeed start"+feed_id);
			feed = createNewFeed(feed_id,feed);
		}
		return feed;
	}
	
	/*
	 * Description:
	 * <br/>this method is to create new feed when we can't find the feed in mc
	 * <br/>I think this method need synchronized to avoid the create same feed
	 * <br/>narrow the the range of the synchronized code block
	 * @param feed_id 
	 * @return Feed
	 */
	@Profiled(tag="createNewFeed")
	private synchronized static Feed createNewFeed(BigInteger feed_id,Feed feed){
		
		Connection conn = C3P0Pool.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		// the count and the list 
		int count = 0;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<BigInteger> list = Collections.synchronizedList(new ArrayList());
		
		// get the data from database
		String sql = "select like_uid from feed where feed_id = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, feed_id);
			rs = ps.executeQuery();
			// get the result from database 
			while(rs.next()){
				list.add((BigInteger)rs.getObject("like_uid"));
				count++;
			}
			// sort the list 
			Collections.sort(list);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		// create new feed
		feed = new Feed(feed_id,count,list);	
		// save to the mc
		// notice:key:"f"+feed_id value:feed
		cache.add("f"+String.valueOf(feed_id), feed);
		
		// return the connection
		C3P0Pool.free(rs, ps, conn);
		return feed;
	}
	
	/*
	 * Description:
	 * <br/>this method is to find the user in the mc by like_uid
	 * <br/>I think this method need synchronized to avoid the create same user
	 * @param like_uid
	 * @return User
	 */
	@Profiled(tag="findFriend")
	private static User findFriend(BigInteger like_uid){
		User user = (User) cache.get("u"+String.valueOf(like_uid));		
		if(user==null){
			user = createNewUser(like_uid,user);
		}		 
		return user;
	}
	
	/*
	 * Description:
	 * <br/>this method is to create new user in the mc by like_uid
	 * <br/>I think this method need synchronized to avoid the create same user
	 * @param like_uid
	 * @param user 
	 * @return User
	 */
	@Profiled(tag="createNewUser")	
	private synchronized static User createNewUser(BigInteger like_uid,User user){
		
		Connection conn = C3P0Pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<BigInteger> list = Collections.synchronizedList(new ArrayList());
		
		// get the data from database
		String sql = "select to_uid from friends where from_uid = ?";

		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1,like_uid);
			rs = ps.executeQuery();					
			while(rs.next()){
				list.add((BigInteger)rs.getObject("to_uid"));
			}
			Collections.sort(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user = new User(like_uid,list);			
		// store to the mc 
		// key:"u"+like_uid value:user
		cache.add("u"+String.valueOf(like_uid),user);
		
		C3P0Pool.free(rs, ps, conn);
		return user;
	}
	
	/*
	 * Description:
	 * <br/>this method is to get the result list
	 * <br/>we also need to list the friend list of the likeUid at first
	 * @param aList
	 * @param frList
	 * @return List<BigInteger>
	 */
	@Profiled(tag="sortList")		
	private static List<BigInteger> sortList(List<BigInteger> aList,List<BigInteger> frList){
		// at first,we need consider to sort and compare
		// we had sorted the list before this deal 
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<BigInteger> lastList = Collections.synchronizedList(new ArrayList());
		// the like-list of the feed 	
		ListIterator<BigInteger> itAList = aList.listIterator();		
		// the friend-list of the user 	
		ListIterator<BigInteger> itFrList = frList.listIterator();
		
		// to get the result list
		while(itFrList.hasNext()){
			// get the frUid from the friend-list
			BigInteger frUid = (BigInteger)itFrList.next();
			while(itAList.hasNext()){
				// get the likeUid from the like-list
				BigInteger likeUid = (BigInteger)itAList.next();
				// if frUid likeUid is the same
				// add the likeUid to the lastList and remove the likeUid from the like-list
				// then break the cycle 
				if(frUid.equals(likeUid)){
					// because the list is one to many,so the element of the list will not be same. 
					lastList.add(likeUid);
					itAList.remove();
					break;
				}
				
				// if frUid < likeUid ,it means this frUid is not in the like-list
				// we need move the like-list to the previous elements
				// and break the cycle
				if(frUid.compareTo(likeUid)==-1){
					itAList.previous();
					break;
				}
			}
		}
		
		// make the like-list set behind the the lastList 
		ListIterator<BigInteger> itAList2 = aList.listIterator();
		while(itAList2.hasNext()){
			BigInteger likeUid = (BigInteger)itAList2.next();
			lastList.add(likeUid);
		}		
		return lastList;		
	}
	
}
