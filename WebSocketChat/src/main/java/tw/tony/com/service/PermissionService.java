package tw.tony.com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.tony.com.mapper.PermissionMapper;

@Service
public class PermissionService {

	private static Logger log = LoggerFactory.getLogger(PermissionService.class); // slf4j

	@Autowired
	private PermissionMapper permissionMapper;

	// 用戶權限取得
	public String getUserPermission(String uid) {

		return permissionMapper.getUserPermissionByUid(uid);

	}

}
