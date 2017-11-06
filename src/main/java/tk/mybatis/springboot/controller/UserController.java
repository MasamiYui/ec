package tk.mybatis.springboot.controller;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.springboot.service.IdCardService;
import tk.mybatis.springboot.service.UserService;
import tk.mybatis.springboot.util.CommonResult;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IdCardService idCardService;
	
	@RequestMapping("/add")
	@ResponseBody
	public CommonResult addUser(
			String name,
			String phone,
			String idCardNo,
			String passwd
			) {
		int checkResult = userService.checkUser(phone, idCardNo);
		if(checkResult == 2) {
			return new CommonResult(301, "手机已经被注册", null);
		}else if(checkResult == 3) {
			return new CommonResult(301, "身份证已经被注册", null);
		}else if(checkResult == 1) {
			int result = userService.addUser(name, phone, idCardNo, passwd);
			if(result == 1) {
				return new CommonResult(200, "ok", null);
			}else{
				return new CommonResult(301, "添加错误", null);
			}
		}
		return new CommonResult(301, "error", null);
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public CommonResult login(
			String phone,
			String passwd,
			HttpSession session
			) {
		Hashtable<String, Object> checkResult = userService.checkLogin(phone, passwd);
		if(checkResult == null) {
			return new CommonResult(301, "手机号或密码错误", null);
		}
		session.setAttribute("user", checkResult);
		return new CommonResult(200, "ok", null);
	}
	
	
	@RequestMapping("/cards/list")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public CommonResult cardsList(
			HttpSession session
			) {
		Hashtable<String,Object> resultData = new Hashtable<>();
		
		//session 获取用户信息
		Hashtable<String,Object> userData = (Hashtable<String, Object>)session.getAttribute("user");
		//搜寻用户的身份证
		Hashtable<String, Object> idCardData = idCardService.getIdCardDetailByIdCardNo((String)userData.get("idcard_no"));
		
		resultData.put("user_info", userData);
		resultData.put("idcard_info", idCardData);
		//userTable.get("idCardNo")
		return new CommonResult(200, "ok", resultData);
	}
	
	
	
}
