package tw.tony.com.shiro;

import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

	private Logger log = LoggerFactory.getLogger(ShiroConfig.class);// slf4j

	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(
			@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 設置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		/*
		 * Shiro過濾器 anon:無須認證訪問 authc：必須用認證訪問 user:使用rememberMe功能直接訪問 perms:資源必須得到資源權限訪問
		 * role:資源必須得到角色授權訪問
		 */
		// filterMap過濾器Map，顺序存放攔截url
		Map<String, String> filterMap = new LinkedHashMap<String, String>();

		// 放行頁面
		filterMap.put("/login", "anon");

		// 授權過濾器
		filterMap.put("/user", "perms[user:user]");
		filterMap.put("/admin/*", "perms[user:admin]");

		// 權限過濾器
		filterMap.put("/user/*", "authc");
		// filterMap.put("/superAdmin/*", "authc");

		// 設置未授權頁面
		shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

		// 失敗後跳轉登入頁面
		shiroFilterFactoryBean.setLoginUrl("/login");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

		return shiroFilterFactoryBean;

	}

	@Bean
	public DefaultWebSessionManager getDefaultWebSessionManager() {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setGlobalSessionTimeout(1000 * 3600); // session過期時間
		defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
		defaultWebSessionManager.setSessionIdCookieEnabled(true);
		return defaultWebSessionManager;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(userRealm);
		return securityManager;
	}

	@Bean(name = "userRealm")
	public UserRealm getUserRealm() {
		return new UserRealm();

	}

	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		// cokie設置為7天
		simpleCookie.setMaxAge(604800);
		cookieRememberMeManager.setCookie(simpleCookie);
		return cookieRememberMeManager;
	}

}
