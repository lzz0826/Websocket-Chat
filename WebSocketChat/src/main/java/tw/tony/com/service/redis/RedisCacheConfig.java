//package tw.tony.com.service.redis;
//
//import java.time.Duration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableCaching // 啟用快取，這個註解很重要；
//public class RedisCacheConfig extends CachingConfigurerSupport {
//
//	// 注入 RedisConnectionFactory
//	@Autowired
//	public RedisConnectionFactory redisConnectionFactory;
//
//	// 實例化 RedisTemplate 對象 設置數據存入 redis 的序列化方式
//	@Bean
//	public RedisTemplate<String, Object> functionDomainRedisTemplate() {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//		// 定義KEY生成定義
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
//		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//		redisTemplate.setConnectionFactory(redisConnectionFactory);
//		return redisTemplate;
//	}
//
//	// 緩存管理器 配置緩存時間
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory factory) {
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(-1));
//		RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config).build();
//		return cacheManager;
//	}
//
//	//实例化 HashOperations 对象,可以使用 Hash 类型操作
//	@Bean
//	public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
//		return redisTemplate.opsForHash();
//	}
//	
//	//实例化 ListOperations 对象,可以使用 List 操作
//	@Bean
//	public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate){
//		return redisTemplate.opsForList();
//	}
//	
//	//实例化 SetOperations 对象,可以使用 Set 操作
//	@Bean
//	public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
//		return redisTemplate.opsForSet();
//	}
//	
//	
//	//实例化 ZSetOperations 对象,可以使用 ZSet 操作
//	@Bean
//	public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate){
//		return redisTemplate.opsForZSet();
//	}
//	
//	
//	
//	
//	
//
////	@Bean
////	public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate) {
////		CacheManager cacheManager = new RedisCacheManager(redisTemplate);
////		return cacheManager;
////	}
//
//	@Bean
//	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
//		redisTemplate.setConnectionFactory(factory);
//		// key序列化方式;（不然會出現亂碼;）,但是如果方法上有Long等非String型別的話，會報型別轉換錯誤；
//		// 所以在沒有自己定義key生成策略的時候，以下這個程式碼建議不要這麼寫，可以不配置或者自己實現ObjectRedisSerializer
//		// 或者JdkSerializationRedisSerializer序列化方式;
//		// RedisSerializer<String> redisSerializer = new
//		// StringRedisSerializer();//Long型別不可以會出現異常資訊;
//		// redisTemplate.setKeySerializer(redisSerializer);
//		// redisTemplate.setHashKeySerializer(redisSerializer);
//		return redisTemplate;
//	}
//
//}
