package tw.tony.com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import tw.tony.com.service.redis.RedisService;

public class WebSocketTools {
	
	private RedisTemplate redisTemplate = new RedisTemplate();
	

	public void deleteRedisToken(String uid) {
		
		remove(uid);
	}
	
	/**
	 * 删除對應的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有對應的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}
	

}
