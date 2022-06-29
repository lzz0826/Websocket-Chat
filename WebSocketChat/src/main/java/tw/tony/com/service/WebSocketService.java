package tw.tony.com.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import tw.tony.com.controller.IndexController;
import tw.tony.com.mapper.MessageLogMapper;
import tw.tony.com.mapper.UserMapper;
import tw.tony.com.model.Chat;
import tw.tony.com.model.MessageLog;
import tw.tony.com.model.ResultSet;
import tw.tony.com.model.SessionId;
import tw.tony.com.service.redis.RedisService;
import tw.tony.com.util.JwtToken;
import tw.tony.com.util.Tools;
import tw.tony.com.util.WebSocketServer;

@Service
public class WebSocketService {

	@Autowired
	private RedisService redisService;

	@Autowired
	private MessageLogMapper messageLogMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JwtToken jwtToken;

	@Autowired
	private IndexController indexController;

	private static Logger log = LoggerFactory.getLogger(WebSocketService.class); // slf4j

	public void delWebSocket(String sessionId) throws IOException {
		WebSocketServer.closeWebSocket(sessionId);

	}

	// 群發
	// @param message
	// @return
	public ResultSet sendChatAll(String message, String token, String account) {
		String checkToken;
		ResultSet resultSet = new ResultSet();
		Chat chat = new Chat();
		MessageLog messageLog = new MessageLog();

		// 設置私有ID
		chat.setChat_username(Tools.sessionValidate("username"));
		chat.setChat_text("(全體)說:" + message);
		String uid = userMapper.getUserByUid(account).getUid();
		String sid = userMapper.getUserBySid(account).getSid();

		// 紀錄聊天訊息
		messageLog.setUid(uid);
		messageLog.setSid(sid);
		messageLog.setMessage(message);

		try {
			checkToken = redisService.get(uid).toString();
			System.out.println("redis有值");
		} catch (Exception e) {
			checkToken = "";
			System.out.println("redis沒值");
		}
		System.out.println("token   " + token);
		System.out.println("checkToken   " + checkToken);
		if (Tools.sessionValidate("username") == null && Tools.sessionValidate("uid") == null) {
			// 未登入
			resultSet.fail("用戶未登入");
		} else if (checkToken.equals("")) {
			resultSet.fail("你被T了");

		} else if (checkToken.equals(token)) {
			try {
				messageLogMapper.addMessageLog(messageLog);
				WebSocketServer.BroadCastInfo(chat);
				resultSet.success("OK");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			resultSet.fail("其他問題");
		}
		/*
		 * try { WebSocketServer.BroadCastInfo(chat); resultSet.success("ok"); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		return resultSet;
	}

	// 單對單
	public ResultSet sendChatOne(String message, String privateId, String account, String token,
			String pirvateAccount) {
		String checkToken;

		MessageLog messageLog = new MessageLog();
		ResultSet resultSet = new ResultSet();
		Chat chat = new Chat();
		// 設置私有ID
		chat.setChat_username(Tools.sessionValidate("username"));
		chat.setChat_text("(私人)說:" + message);

		String uid = userMapper.getUserByUid(account).getUid();
		String sid = userMapper.getUserBySid(account).getSid();

		messageLog.setUid(uid);
		messageLog.setSid(sid);
		messageLog.setMessage("(To:" + pirvateAccount + ")" + message);

		try {
			checkToken = redisService.get(uid).toString();
			System.out.println("redis有值");
		} catch (Exception e) {
			checkToken = "";
			System.out.println("redis沒值");
		}

		System.out.println("token   " + token);
		System.out.println("checkToken   " + checkToken);

		if (Tools.sessionValidate("username") == null && Tools.sessionValidate("uid") == null) {
			// 未登入
			resultSet.fail("用戶未登入");
		} else if (checkToken.equals("")) {
			resultSet.fail("你被T了");

		} else if (checkToken.equals(token)) {
			try {
				messageLogMapper.addMessageLog(messageLog);
				resultSet.success(WebSocketServer.sendmessage(chat, privateId));
				resultSet.success("OK");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			resultSet.fail("其他問題");
		}
		/*
		 * try { WebSocketServer.BroadCastInfo(chat); resultSet.success("ok"); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		return resultSet;

	}

}
