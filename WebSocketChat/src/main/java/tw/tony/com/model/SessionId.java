package tw.tony.com.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

@Component
public class SessionId {

	Map<String, String> userSessionId = new HashMap<String, String>();

	public void setSessionId(String uid, String sessionId) {

		userSessionId.put(uid, sessionId);
	}

	public String getSessionId(String uid) {
		return userSessionId.get(uid);
	}

	public void deleteSessionId(String uid) {
		userSessionId.remove(uid);

	}

	public Collection<String> getAllValuer() {
		return userSessionId.values();
	}

	public Collection<String> getAllKey() {

		return userSessionId.keySet();
	}

	public Set<Entry<String, String>> getAllKeyAndValues() {

		return userSessionId.entrySet();
	}

}
