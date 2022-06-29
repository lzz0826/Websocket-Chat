//package tw.tony.com.service;
//
//
//import org.aspectj.weaver.ast.And;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//
//
//
//
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//	@Bean
//	public UserDetailsService springUserService(){
//		return new SpringUserDetailsService();
//	}
//
////	@Autowired
////	private SpringLogoutSuccessHandler springLogoutSuccessHandler;
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//		auth.userDetailsService(springUserService());
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		// 设置拦截忽略文件夹，可以对静态资源放行
//		web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**", "/images/**", "/lib/**");
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception{
//		http
//				.csrf().disable();
//		http.
//		exceptionHandling()
//		        .accessDeniedPage("/refuse") //存取被拒時導往的URL
//		.and()
//		.authorizeRequests()
//		        .antMatchers("/**").hasAnyRole("A","B")         //最高權限
////		        .antMatchers("/").hasAnyRole("A")       //
////				.antMatchers("/index").hasAnyRole("B")    
//
//				.anyRequest().permitAll()
//				.and()
//				.formLogin()
//				.loginPage("/login")                    //登入頁面
//				.loginProcessingUrl("/account")      //登入頁面的form表單資料 user .password
//				.defaultSuccessUrl("/index")            //成功後轉首頁
//				.failureUrl("/login?error=true")        //失敗頁面
//				.and()
//				.logout()
//				.logoutUrl("/logout")                             //登出按鈕
//				.logoutSuccessUrl("/login")                       //登出以後頁面
////		        .logoutSuccessHandler(springLogoutSuccessHandler) //登出額外處理
//				.permitAll();
//		http.headers().frameOptions().sameOrigin();
//	}
//}
