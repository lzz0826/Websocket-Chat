package tw.tony.com.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public  class OlineUser {

	Map<String, String> olineUser = new HashMap<String, String>();

	
	public void setUser(String account, String username) {
		olineUser.put(account, username);
	}

	
	public String getUser(String account) {
		return olineUser.get(account);
	}

	
	public void deleteKey(String accoun) {
		olineUser.remove(accoun);
	}
	
	
	public Collection<String> getAllValues() {
		return olineUser.values();
	}

	
	public Collection<String> getAllKey() {
		return olineUser.keySet();
	}
	
	
	public Set<Entry<String, String>> getAllKeyValues() {
		
		return olineUser.entrySet();
		
	}
	
	

}
