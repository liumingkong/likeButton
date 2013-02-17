package com.weibo.service;

import java.math.BigInteger;
import java.util.List;


public interface likeOp {
	
	/*
	 * Description:
	 * <br/>add a count 
	 * @param like_uid
	 * @return void
	 */
	void addCount(BigInteger like_uid);
	
	/*
	 * Description:
	 * <br/>delete a count
	 * @param like_uid
	 * @return void
	 */
	void delCount(BigInteger like_uid);
	
	/*
	 * Description:
	 * <br/>get the count
	 * @return int
	 */
	int showCount();
	
	/*
	 * Description:
	 * <br/>get the userlist of the feed_id please show the friend at first
	 * @return list
	 */
	List<?> showFeedList();
 
}
