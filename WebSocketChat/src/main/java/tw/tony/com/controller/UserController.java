package tw.tony.com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.tony.com.model.ResultSet;
import tw.tony.com.model.User;
import tw.tony.com.service.UserService;
import tw.tony.com.util.Tools;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/loginUserByAjax")
	@ResponseBody
	public ResultSet login(@RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            HttpServletRequest request) {
		return userService.userLogin(account, password, request);	
	}
	
	
	@RequestMapping("/addUserByAjax")
	@ResponseBody
	public ResultSet addUserByAjax(@RequestParam(value = "sid") String sid,
			                       @RequestParam(value = "username") String username,
			                       @RequestParam(value = "password") String password,
			                       @RequestParam(value = "sex") String sex,
			                       HttpServletRequest request) {
		
		System.out.println(sid);
		System.out.println(username);
		System.out.println(password);
		System.out.println(sex);

        User user = new User();
        user.setUid(Tools.createUserRandomPrivateId());
        user.setSid(sid);
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setIp(Tools.getUserIp(request));
        user.setBrowser(Tools.getBrowserVersion(request));
        user.setWeight("1");
        return userService.userAdd(user);	
	}
	
	
	@RequestMapping("/updateUserByAjax") 
	@ResponseBody
	public ResultSet updateUserByAjax(@RequestParam(value = "uid") String uid,
		                              @RequestParam(value = "sid") String sid ,
			                          @RequestParam(value = "username") String username ,
			                          @RequestParam(value = "password") String password,
			                          @RequestParam(value = "sex") String sex ,
			                          @RequestParam(value = "ip") String ip ,
			                          @RequestParam(value = "browser") String browser,
			                          @RequestParam(value = "weight") String weight
			                          ) {
        User user = new User();
        user.setUid(uid);
        user.setSid(sid);
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setIp(ip);
        user.setBrowser(browser);
        user.setWeight(weight);
		return userService.userUpdate(user);	
	}
	
	
    @RequestMapping("/deleteUserByAjax")
    @ResponseBody
    public ResultSet deleteUserByAjax(@RequestParam(value = "uid") String uid) {

        return userService.userDelete(uid);
    }

    @RequestMapping("/showUserList")
    @ResponseBody
    public ResultSet showUserList(@RequestParam(value = "pageNum") int pageNum) {
        return userService.getUserAllByAdmin(pageNum);
    }

    @RequestMapping("/showUser")
    @ResponseBody
    public ResultSet showUser(@RequestParam(value = "sid") String sid){
        return userService.getUserByAdmin(sid);
	
    }

}
