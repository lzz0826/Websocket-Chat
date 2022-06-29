package tw.tony.com.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.tony.com.mapper.PermissionMapper;
import tw.tony.com.mapper.UserMapper;
import tw.tony.com.model.OlineUser;
import tw.tony.com.model.ResultSet;
import tw.tony.com.model.SessionId;
import tw.tony.com.model.User;
import tw.tony.com.service.UserService;
import tw.tony.com.service.WebSocketService;
import tw.tony.com.service.redis.RedisService;
import tw.tony.com.util.WebSocketServer;

@Controller
@RequestMapping("/api/websocket")

public class ChatController {

	@Autowired
	private UserService userService;

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OlineUser olineUser;


	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisService redisService;

	@Autowired
	private SessionId sessionId;
	
	
	


	// 取得上線中的用戶名單
	@RequestMapping("/onlineuser")
	@ResponseBody
	public String getOnlineUser() {
		System.out.println(olineUser.getAllKeyValues());
		try {
			String resultString = objectMapper.writeValueAsString(olineUser.getAllKeyValues());
			System.out.println(resultString);
			return resultString;

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	// 刪除上線中會員名單
	@ResponseBody
	@PostMapping("/cullOnlineuser")
	public String deleteOnlineUser(@RequestParam Map<String, String> map) throws IOException {
		String account = map.get("account");
		User uid = userMapper.getUserByUid(account);
		String cullAccount = map.get("cullAccount");
		String premission = permissionMapper.getUserPermissionByUid(uid.getUid());
		System.out.println("account   " + map.get("account"));
		System.out.println("cullAccount   " + map.get("cullAccount"));
		System.out.println("premission   " + premission);
		if (premission.equals("user:admin")) {
			userService.deleteRedisToken(cullAccount);
			webSocketService.delWebSocket(cullAccount);
			
			
			return "成功";
		} else {
			return "你的權限不足";
		}
	}
	
	
	

	// 群發消息
	// @param message
	// @return

	@RequestMapping("/sendAll")
	@ResponseBody
	public ResultSet sendAllMessage(@RequestParam(required = true, value = "chatText") String message,
			@RequestParam String token, @RequestParam String account) {
//		System.out.println(token);
//		System.out.println(account);
		return webSocketService.sendChatAll(message, token, account);
	}
	
	
	// 指定SessioID發消息
	// @param message 消息內容
	// @param privateId 連接私有ID
	// @retrun
	@RequestMapping(value = "/sendOne", method = RequestMethod.GET)
	@ResponseBody
	public ResultSet sendOneMessage(@RequestParam(required = true, value = "chatText") String message,
			@RequestParam String token, @RequestParam String account, @RequestParam String pirvateUid) {
		String sessionId = null;
//		System.out.println("token   " + token);
//		System.out.println("account   " + account);
		System.out.println("pirvateAccount  " + pirvateUid);
		try {
			sessionId = (String) redisService.get("sessionId" + pirvateUid);
			System.out.println("sessionId   " + sessionId);
		} catch (Exception e) {
			e.getMessage();
			System.out.println("error   " + e.getMessage());
		}
		return webSocketService.sendChatOne(message, sessionId, account, token,pirvateUid);
	}

}
