package com.weibo.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigInteger uid;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BigInteger> friendList = Collections.synchronizedList(new ArrayList());
	
	public User(BigInteger like_uid, List<BigInteger> list){
		this.uid = like_uid;
		this.friendList = list;
	}
	
	/*
	 * Description:
	 * <br/>get the friend-list of the friend-list
	 * return list
	 */
	public List<BigInteger> getFriendList(){		
		return friendList;		
	}

	/*
	 * Description:
	 * <br/>set the friend-list of the friend-list
	 * return void
	 */
	public void setFriendList(List<BigInteger> friendList){
		this.friendList = friendList;		
	}
	
	/*
	 * Description:
	 * <br/>get the uid of the friend-list
	 * return int
	 */
	public BigInteger getUid() {
		return uid;
	}
	
	/*
	 * Description:
	 * <br/>set the uid of the friend-list
	 * return void
	 */
	public void setUid(BigInteger uid) {
		this.uid = uid;
	}
	
}
