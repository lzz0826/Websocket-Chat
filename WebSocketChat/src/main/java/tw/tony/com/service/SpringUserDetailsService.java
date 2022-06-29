//package tw.tony.com.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import tw.tony.com.dao.UserDao;
//import tw.tony.com.po.CurrentUser;
//import tw.tony.com.po.UserTable;
//import tw.tony.com.service.redis.RedisService;
//
//@Service
//public class SpringUserDetailsService implements UserDetailsService {
//
//	@Autowired
//	private CurrentUser currentUser;
//
//	@Autowired
//	private UserDao userDao;
//
//	@Autowired
//	private RedisService redisService;
//	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		System.out.println("account : " + username);
//		UserTable userDaoBean = userDao.getAccount(username);
//		System.out.println("userDaoBean : " + userDaoBean);
//		if (userDaoBean == null) {
//			throw new UsernameNotFoundException("User not found");
//		}
//		UserDetails userDetails = User.builder().username(userDaoBean.getUsername())
//				.password("{noop}" + userDaoBean.getPassword()) // 不加密
//				.roles(userDaoBean.getPermission()).build();
//		
////		String sessionid =  RequestContextHolder.currentRequestAttributes().getSessionId();
////		
////		System.out.println("sessionid : " + sessionid);
//		
//
////		System.out.println(redisService.hmGet( "creatrionTime" , "test:sessions:14d6ea87-67e7-4243-b7e1-c5ffcfb3e60a"));
//		
//		
//		
//		return userDetails;
//	}
//
//}
