package tw.tony.com.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper {
	
	String getUserPermissionByUid(String uid);

	void userPermissionAdd(String uid,String permission);
}
