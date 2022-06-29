package tw.tony.com.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.tony.com.controller.ChatController;
import tw.tony.com.dao.UserJdbcDao;

import tw.tony.com.mapper.UserMapper;
import tw.tony.com.model.Chat;
import tw.tony.com.model.OlineUser;
import tw.tony.com.model.SessionId;
import tw.tony.com.model.User;
import tw.tony.com.service.redis.RedisService;

@ServerEndpoint(value = "/chatOnline/websocket/{uid}", encoders = { WebSocketServerEncoder.class })
@Component
public class WebSocketServer {

	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class); // slf4j

	private static final AtomicInteger onlineCount = new AtomicInteger(0);
	// 當前再線數
	private static int cnt;
	// concurrent包的線程安全Set 用來存放每個客戶對應Session對象
	private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

	// 單線程用靜態 + @Autowired get物件來做
	private static RedisService redisService;

	@Autowired
	public void getRedisService(RedisService redisService) {
		WebSocketServer.redisService = redisService;
	}

	private static UserJdbcDao userJdbcDao;

	@Autowired
	public void getUserJpaDao(UserJdbcDao userJdbcDao) {
		WebSocketServer.userJdbcDao = userJdbcDao;
	}
	

	// 初始化
	@PostConstruct
	public void init() {
		log.info("websocket 以加載!");
	}

	// 連接建立成功調用方法
	// param session 加入連接的session
	// throws IOException IO異常
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "uid") String uid) throws IOException, InterruptedException {
		System.out.println("account  " + uid);
		List<User> user = userJdbcDao.getUserAll(uid);
		Chat chat = new Chat();
		redisService.set("sessionId" + uid, session.getId());
		SessionSet.add(session); // 在數據集中添加新打開的session
		cnt = onlineCount.incrementAndGet(); // 當前在線數+1
		log.info("有連線加入,當前連線數為: {}", cnt);
		chat.setChat_private_id("SystemIn");
		chat.setChat_text("新人:" + user.get(0).getUsername() + ",已以進入,當前再線人數:" + cnt);
		BroadCastInfo(chat);

		session.getUserProperties().put("uid" + session.getId(), user.get(0).getUid());
		session.getUserProperties().put("username" + session.getId(), user.get(0).getUsername());
	}

	@OnClose
	// param session 加入連接的seeion
	// throws IOException IO異常
	public void onClose(Session session) throws IOException {

//		session.getUserProperties().get("uid"+session.getId());
//		session.getUserProperties().get("username"+session.getId());

		String username = (String) session.getUserProperties().get("username" + session.getId());
		Chat chat = new Chat();
		SessionSet.remove(session); // 在數據集中移除關閉調用的Session
		cnt = onlineCount.decrementAndGet(); // 當前在線數-1
		chat.setChat_private_id("SystemOut");
		log.info("有連接關閉,當前連線數為: {}", cnt);

		chat.setChat_text("人員:" + username + " 離開了聊天室,當前再線人數為:" + cnt);

		BroadCastInfo(chat);

		session.getUserProperties().remove("uid" + session.getId());
		session.getUserProperties().remove("username" + session.getId());
	}

	// 收到客戶端消息後調用的方法
	// param message 客戶端發送過來的消息
	// param session 加入連接得session

	@OnMessage
	public void onMessage(String message, Session session) throws IOException, EncodeException {
		System.out.println(message);
		if (message.equals("connectOK")) {
			session.getBasicRemote().sendText(message);
		}

//		Chat chat = new Chat();
//		chat.setChat_text("收到消息, 消息內容: " + message);
//		log.info("來自客戶端得消息: {}",message);
//		sendmessage(session, chat);

	}

	// 出現錯誤
	// param session 加入連接的session
	// arame error 錯誤
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("onError");

		error.printStackTrace();

	}

	// 群發消息(單對多聊天)
	// param chat 聊天
	// throws IOException IO異常
	public static void BroadCastInfo(Chat chat) throws IOException {
		for (Session session : SessionSet) {
			if (session.isOpen()) {
				sendmessage(session, chat);
			}
		}
	}

	// 關閉 close WebSocke
	public static String closeWebSocket(String pirvateUid) throws IOException {

		String sessionId = "";
		Session session = null;
		sessionId = (String) redisService.get("sessionId" + pirvateUid);
		for (Session s : SessionSet) {
			if (s.getId().equals(sessionId)) {
				session = s;
				break;
			}
		}
		if (session != null) {
			session.close();
		} else {
			log.warn("沒有找到你指定的ID Session: {}", sessionId);
			return "沒有找到你指定的ID Session";
		}
		return "成功";
	}

	// 指定session發送消息 (單對單聊天)

	// param sessionId 加入連接的session
	// param chat 聊天
	// throws IOException IO異常
	public static String sendmessage(Chat cate, String sessionId) throws IOException {
		Session session = null;
		for (Session s : SessionSet) {
			if (s.getId().equals(sessionId)) {
				session = s;
				break;
			}
//			System.out.println("session  SS  " + s);
//			System.out.println("sessionId" + sessionId);
		}
		if (session != null) {
			sendmessagePirvate(session, cate);
		} else {
			log.warn("沒有找到你指定的ID Session: {}", sessionId);
			return "沒有找到你指定的ID Session";
		}
		return "成功";

	}

	// 發送消息
	// param session 加入連接的session
	// param chat 聊天
	private static void sendmessage(Session session, Chat chat) {
		try {
//            session.getBasicRemote().sendText(String.format("%s (來自伺服器，Session ID=%s)", chat.getChat_text(), session.getId()));
//            ObjectMapper objectMapper = new ObjectMapper();
//            session.getBasicRemote().sendText(objectMapper.writeValueAsString(chat));

			session.getBasicRemote().sendObject(chat); // 需要解碼器
		} catch (IOException | EncodeException e) {
			log.error("發送信息錯誤 : {}", e.getMessage());
			e.printStackTrace();

		}

	}

	// 發送消息
	// param session 加入連接的session
	// param chat 聊天
	private static void sendmessagePirvate(Session session, Chat chat) {
		try {
			session.getBasicRemote()
					.sendText(String.format("%s (來自伺服器，Session ID=%s)", chat.getChat_text(), session.getId()));
			ObjectMapper objectMapper = new ObjectMapper();
			session.getBasicRemote().sendText(objectMapper.writeValueAsString(chat));

//			session.getBasicRemote().sendObject(chat); // 需要解碼器
		} catch (IOException e) {
			log.error("發送信息錯誤 : {}", e.getMessage());
			e.printStackTrace();
		}

	}

}
