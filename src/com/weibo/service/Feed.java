package com.weibo.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Feed implements likeOp,Serializable{
	
	private static final long serialVersionUID = 1L;

	// the feed id
	@SuppressWarnings("unused")
	private BigInteger feed_id;
		
	// the like count
	private int feedCount = 0;
	
	// the feed like-list
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BigInteger> userList = Collections.synchronizedList(new ArrayList());


	/*
	 * Description:
	 * <br/>construct the Feed object 
	 * @param feed_id 
	 * @param feedCount
	 * @param userList
	 */
	public Feed(BigInteger feed_id,int feedCount,List<BigInteger> userList){
		this.feed_id = feed_id;
		this.setFeedCount(feedCount);
		this.userList = (List<BigInteger>) userList;
	}
	
	/*
	 * Description:
	 * <br/>get the count of the feed 
	 * return int
	 */
	private int getFeedCount() {
		return feedCount;
	}

	/*
	 * Description:
	 * <br/>set the count of the feed 
	 * @param feedCount
	 * return void
	 */
	private synchronized void setFeedCount(int feedCount) {
		this.feedCount = feedCount;
	}
	
	/*
	 * Description:
	 * <br/>get the list of the feed 
	 * return list
	 */
	private List<BigInteger> getUserList() {
		return userList;
	}


	/*
	 * Description:
	 * <br/>add the count and list of the feed-like
	 * @param like_uid
	 * return void
	 */
	@Override
	public void addCount(BigInteger like_uid) {
		// TODO Auto-generated method stub
		// at first,we need to add the count of the feed 
		this.setFeedCount(this.getFeedCount() + 1);
		// then add the user to the feed-like-list
		this.userList.add(like_uid);
		// sort the list
		Collections.sort(this.getUserList());
	}

	/*
	 * Description:
	 * <br/>delete the count and list of the feed-like
	 * @param like_uid
	 * return void
	 */
	@Override
	public void delCount(BigInteger like_uid) {
		// TODO Auto-generated method stub
		// delete the count of the feed-like
		this.setFeedCount(this.getFeedCount()-1);
		// then delete the user to the feed-like-list 
		this.userList.remove(like_uid);
	}

	/*
	 * Description:
	 * <br/>show the count of the feed-like
	 * return int
	 */
	@Override
	public int showCount() {
		// TODO Auto-generated method stub
		return this.getFeedCount();
	}

	/*
	 * Description:
	 * <br/>show the list of the feed-like
	 * return list
	 */
	@Override
	public List<BigInteger> showFeedList(){
		// TODO Auto-generated method stub
		return this.getUserList();
	}	
}
