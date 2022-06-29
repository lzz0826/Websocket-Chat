package tw.tony.com.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.tony.com.service.UserService;
import tw.tony.com.util.Tools;

@Controller
public class IndexController {
	
	
	@Autowired
	private UserService userService;
	
	
	private static Logger log = LoggerFactory.getLogger(IndexController.class);// slf4j

	@RequestMapping("/")
	public String showIndexPage() {
		log.info("主頁面重定向頁面以載入");
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String showHomePage() {
//		String username = Tools.sessionValidate("username");
//		String uid = Tools.sessionValidate("uid");
//		String sid = Tools.sessionValidate("sid");
		String permission = Tools.sessionValidate("permission");
		if ("user:admin".equals(permission)) {
			return "redirect:/admin";
		} else if ("user:user".equals(permission)) {
			return "redirect:/user";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping("/chatroom")
	public String showChatroomPage() {
		log.info("聊天頁面以加載");
		return "chatroom";
	}
	
	@RequestMapping("/privateChatroom")
	public String showPrivateChatroomPage() {
		log.info("私人聊天頁面以加載");
		return "privateChatroom";
	}

	@RequestMapping("/login")
	public String showLoginPage() {
		log.info("登入頁面以加載");
		return "login";
	}

	@RequestMapping("/user")
	public String showUserPage(Model model) {
		log.info("當前轉跳管理員頁面");
		model.addAttribute("username", Tools.sessionValidate("username"));
		return "user";
	}

	@RequestMapping("/admin")
	public String showAdminPage(Model model) {
		log.info("當前轉跳個人頁面");
		model.addAttribute("username1", Tools.sessionValidate("username"));
		return "admin";
	}

	@RequestMapping("/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/login";

	}
	
	@ResponseBody
	@RequestMapping("/deleteLogout")
	public String deleteLogout(@RequestParam String currentUser) {
		userService.deleteOlineUser(currentUser);
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "123";
	}

}
