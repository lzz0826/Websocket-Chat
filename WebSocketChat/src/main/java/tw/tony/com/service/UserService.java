package tw.tony.com.service;

import java.lang.ProcessHandle.Info;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tw.tony.com.mapper.PermissionMapper;
import tw.tony.com.mapper.UserMapper;
import tw.tony.com.model.ChatPage;
import tw.tony.com.model.OlineUser;
import tw.tony.com.model.ResultSet;
import tw.tony.com.model.User;
import tw.tony.com.service.redis.RedisService;
import tw.tony.com.util.JwtToken;
import tw.tony.com.util.Tools;

@Service
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);// slf4j

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PermissionMapper permissionMapper;


	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private OlineUser olineUser;
	
	//取得ID
	public User getUserBySid(String sid) {
		return userMapper.getUserBySid(sid);
	}

	// 用戶登入
	public ResultSet userLogin(String account, String password, HttpServletRequest request) {
		ResultSet resultSet = new ResultSet();
		JwtToken jwtToken = new JwtToken();
		// 1.獲取Shiro的Subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封裝數據
		UsernamePasswordToken token = new UsernamePasswordToken(account, password);
		// 3.執行登入方法
		try {
			subject.login(token);
			// Shiro紀錄session
			Session session = subject.getSession();
			session.setAttribute("sid", account);
			User user = getUserBySid(account);
			// 紀錄uid,學生姓名
			session.setAttribute("uid", user.getUid());
			session.setAttribute("username", user.getUsername());
			// 紀錄用戶權限
			String permission = permissionService.getUserPermission(user.getUid());
			session.setAttribute("permission", permission);
			// 更新用戶登入紀錄
			userMapper.userLoginUpdate(user.getUid(), Tools.getUserIp(request), Tools.getBrowserVersion(request));
			log.info(String.format("登入用戶為:%s  用戶uid為:%s  用戶學號為:%s 權限為:%s", user.getUsername(), user.getUid(),
					user.getSid(), permission));
			
			//Redis存Token發話全權關聯
			resultSet.setUid(user.getUid());
			System.out.println("UID : "+user.getUid());
			olineUser.setUser(user.getUid(), user.getUsername());
			String uid = userMapper.getUserByUid(account).getUid();
			userMapper.getUserByUid(account);
			String token11 = jwtToken.generateToken(account);
			resultSet.setToken(token11);	
			System.out.println("令牌 : "+token11);
			redisService.set(uid, token11,1800);
			
			
			log.info("登入成功");
			resultSet.success("登入成功");
		} catch (UnknownAccountException e) {
			// 用戶不存在
			log.info("登入失敗,用戶不存在");
			resultSet.fail("登入失敗,用戶不存在");
		} catch (IncorrectCredentialsException e) {
			// 密碼錯誤
			log.info("登入失敗,密碼錯誤");
			resultSet.fail("登入失敗,密碼錯誤");
		}
		return resultSet;
	}

	// 用戶新增
	public ResultSet userAdd(User user) {
		ResultSet resultSet = new ResultSet();
		try {
			userMapper.userAdd(user);
			permissionMapper.userPermissionAdd(user.getUid(), "user:user");
			resultSet.success("添加用戶成功");
			log.info("添加用戶成功");
		} catch (Exception e) {
			resultSet.fail("添加用戶失敗");
			log.info("添加用戶失敗");
		}
		return resultSet;
	}

	// 用戶更新
	public ResultSet userUpdate(User user) {
		ResultSet resultSet = new ResultSet();
		try {
			userMapper.userUpdate(user);
			resultSet.success("用戶更新成功");
			log.info("用戶更新成功");
		} catch (Exception e) {
			resultSet.fail("用戶更新失敗");
			log.info("用戶更新失敗");
		}
		return resultSet;
	}

	// 用戶刪除
	public ResultSet userDelete(String uid) {
		ResultSet resultSet = new ResultSet();
		try {
			userMapper.userDelete(uid);
			resultSet.success("成功刪除用戶");
			log.info("成功刪除用戶");

		} catch (Exception e) {
			resultSet.fail("用戶刪除失敗");
			log.info("用戶刪除失敗");
		}
		return resultSet;
	}

	
	
	// 刪除上線中用戶名
	public void deleteOlineUser(String currentUser ) {
//		System.out.println(currentUser);
		olineUser.deleteKey(currentUser);	
	}
	
	// 刪除用戶Token 和 上線中用戶名
	public void deleteRedisToken(String account) {
		redisService.remove(account);
		olineUser.deleteKey(account);
		
	}
	
	
	

	// 頁碼
	public ResultSet getUserAllByAdmin(int pageNum) {
		ResultSet resultSet = new ResultSet();
		ChatPage chatPage = new ChatPage();
		Page<User> userPages = PageHelper.startPage(pageNum, 10);
		HashMap<String, Object> hasMap = new HashMap<>();
		List<User> userList = userMapper.getUserListByAdmin();
		chatPage.setPage(pageNum);
		chatPage.setTotal((int) ((userPages.getTotal() - 1) / 10 + 1));
		hasMap.put("data", userList);
		hasMap.put("pageNum", chatPage);
		resultSet.success("成員列表返回成功");
		resultSet.setData(hasMap);
		return resultSet;
	}

	// 用戶查詢
	public ResultSet getUserByAdmin(String sid) {
		ResultSet resultSet = new ResultSet();
		try {
			User user = userMapper.getUserBySid(sid);
			resultSet.success("成員查找成功");
			resultSet.setData(user);
		} catch (Exception e) {
			resultSet.fail("成員查找失敗");
		}
		return resultSet;

	}

}
