//package tw.tony.com.service;
//
//import java.util.Optional;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//import tw.tony.com.service.redis.RedisRepository;
//import tw.tony.com.service.redis.UserDemoInfo;
//
//@Service
//public class RedisUserService {
//	
//
//	@Resource
//	private RedisRepository redisRepository;
//	
//	@Resource
//	private RedisTemplate<String,String> redisTemplate;
//	
//	
//	
//	public void test() {
//		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//		valueOperations.set("mykey4", "HIYOU");
//		System.out.println(valueOperations.get("mykey4"));	
//	} 
//	
//	//keyGenerator="myKeyGenerator"
//	@Cacheable(value="UserDemoInfo" )
//	public Optional<UserDemoInfo> findByid(long id) {
//		System.err.println("redisRepository.findById()=========從資料庫中進行獲取的....id=" + id);	
//		return redisRepository.findById(id);
//	}
//	
//	@Cacheable(value="UserDemoInfo" )
//	public void deleteFromCache(long id) {
//		System.out.println("DemoInfoServiceImpl.delete().從快取中刪除.");
//		redisRepository.deleteById(id);
//	}
//	
//	
//
//	
//
//}
