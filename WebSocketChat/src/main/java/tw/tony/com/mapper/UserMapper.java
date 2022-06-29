package tw.tony.com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import tw.tony.com.model.User;

@Mapper
public interface UserMapper {
	
	User getSid(String uid);
	
	User getUsername(String uid);
		
    User getUserBySid(String sid);
    
    User getUserByUid(String sid);
    
    User getAll(String sid);

    void userAdd(User user);

    void userUpdate(User user);

    void userDelete(String uid);

    void userLoginUpdate(String uid, String ip, String browser);

    List<User> getUserListByAdmin();

    User getUserByAdmin(String sid);

}
