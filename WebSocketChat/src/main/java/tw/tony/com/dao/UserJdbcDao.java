package tw.tony.com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tw.tony.com.model.User;

@Repository
public class UserJdbcDao {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 取得警報參考值
	public List<User> getUserAll(String account) {
		String sqlStr = "SELECT * FROM `test100`.chat_user WHERE `uid` = '"+ account+"'";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<User>(User.class));

	}
	
}
