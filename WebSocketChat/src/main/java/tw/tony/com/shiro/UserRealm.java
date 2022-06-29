package tw.tony.com.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tw.tony.com.model.Role;
import tw.tony.com.model.User;
import tw.tony.com.service.PermissionService;
import tw.tony.com.service.RoleService;
import tw.tony.com.service.UserService;

public class UserRealm extends AuthorizingRealm {

	private static Logger log = LoggerFactory.getLogger(UserRealm.class);// slf4j

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	// 執行授權邏輯
	// @param principals
	// @return
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 資源授權
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		// 資源授權perms中的字符串

//		Subject subject = SecurityUtils.getSubject();
//		User user = (User) subject.getPrincipal();
		User user = (User) principals.getPrimaryPrincipal();
//		Role role = roleService.getUserRolse(user.getRole());
		// 獲取用戶身分許可
		String permission = permissionService.getUserPermission(user.getUid());

//		log.info("當前用戶訊息為" + user);
//		log.info("當前用戶等級為" + role.getRole_name());
		log.info("當前用戶權限為" + permission);
		// 根据许可名过滤权限（ShiroConfig.java權限過濾器）
		simpleAuthorizationInfo.addStringPermission(permission);
		return simpleAuthorizationInfo;
	}

	// 執行認證邏輯
	// @param token
	// @return
	// @throws AuthenticationException

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 判斷用戶名
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		log.info("當前正在登入用戶為:" + usernamePasswordToken.getUsername());

		User user = userService.getUserBySid(usernamePasswordToken.getUsername());

		if (user == null) {
			return null;
		}
		// 判斷密碼
		return new SimpleAuthenticationInfo(user, user.getPassword(), "");
	}

}
