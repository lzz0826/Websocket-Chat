package tw.tony.com.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;

import com.github.pagehelper.Page;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

public class Tools {

	// 生成私人ID
	// @return privateID
	public static String createRandomPrivateId(int type) {
		String[] arr = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
				"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		String[] str = new String[] { "group", "message" };
		int i = 0;
		StringBuilder privateId = new StringBuilder();
		while (i < 15) {
			int randomNum = (int) (Math.random() * 52); // 隨機取得
			privateId.insert(0, arr[randomNum]); // 拼接
			i++;
		}
		privateId.insert(0, str[type]);
		return privateId.toString();
	}

	// 生成私人用戶ID
	// @return privateId
	public static String createUserRandomPrivateId() {
		String[] arr = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		int i = 0;
		StringBuilder pirvateId = new StringBuilder();
		while (i < 12) {
			int randomNum = (int) (Math.random() * 10);
			pirvateId.insert(0, arr[randomNum]);
			i++;
		}
		pirvateId.insert(0, "user");
		return pirvateId.toString();
	}

	// 獲取伺服器時間
	// @return time
	private static String getServerTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	// 取得IP
	// @return ip
	public static String getUserIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.length() > 0) {
			String[] ips = ip.split(",");
			for (String s : ips) {
				if (s.trim().length() > 0 && !"unknown".equalsIgnoreCase(s.trim())) {
					ip = s.trim();
					break;
				}
			}
		}
		return ip;
	}

	// 獲取瀏覽器訊息
	// @param request
	// @return browser
	public static String getBrowserVersion(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent"); // 獲取瀏覽器訊息
		UserAgent userAgent = UserAgent.parseUserAgentString(ua); // 轉成UserAgent對象
		Browser browserInfo = userAgent.getBrowser(); // 獲取瀏覽器訊息
		return browserInfo.getName();
	}

	// 獲取系統訊息
	// @param request
	// @return system

	public static String getSystemVersion(HttpServletRequest request) {
		String us = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(us);
		OperatingSystem so = userAgent.getOperatingSystem();
		return so.getName();
	}

	// 用戶Session驗證
	// @return username
	public static String sessionValidate(String key) {
		/*
		 * servlet自带session HttpSession session = request.getSession();
		 */
		// Shiro獲取取session
		String returnVal = null;
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		switch (key) {
		case "username":
			returnVal = (String) session.getAttribute("username");
			break;
		case "uid":
			returnVal = (String) session.getAttribute("uid");
			break;
		case "sid":
			returnVal = (String) session.getAttribute("sid");
			break;
		case "permission":
			returnVal = (String) session.getAttribute("permission");
			break;
		}
		return returnVal;
	}

	// 用戶是否為超管管理員Session驗證
	// @retrun

	public static boolean usernameSessionIsAdminValidate(String role) {
		try {
			if (role.equals("2") || role.equals("1")) {
				return true;
			}
		} catch (NullPointerException e) { // 防止ShiroHttpServletRequest返回空指针
			return false;
		}
		return false;
	}

	/**
	 * 主頁各標籤翻頁選擇 前端html的”pageNumPrev“、”pageNumNext“、”pageTotal“會出现红色下波浪线，可忽略
	 *
	 * @param model
	 * @param pageNum
	 * @param pages
	 * @param setPageNum
	 */

	public static void indexPageHelperJudge(Model model, int pageNum, Page pages, int setPageNum) {
		if (pageNum == 1) {
			// 如果當前頁面低一頁,則上一頁設為1
			model.addAttribute("pageNumPrev", 1);
		} else {
			// 否則上一頁設為當前頁-1
			model.addAttribute("pageNumPrev", pageNum - 1);
		}
		if (pageNum == pages.getTotal() / setPageNum + 1) {
			// 如果當前頁為最後一頁,則下一頁一直是最後一頁
			model.addAttribute("pageNumNext", pages.getTotal() / setPageNum + 1);
		} else {
			// 否則,下一頁為當前頁+1
			model.addAttribute("pageNumNext", pageNum + 1);
		}
		model.addAttribute("pageTotal", pages.getTotal() / setPageNum + 1);
	}

}
