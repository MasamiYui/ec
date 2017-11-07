package tk.mybatis.springboot.controller;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import tk.mybatis.springboot.service.IdCardService;
import tk.mybatis.springboot.service.UserService;
import tk.mybatis.springboot.util.CommonResult;
import tk.mybatis.springboot.util.CommonUtil;
import tk.mybatis.springboot.util.IdCardUtil;
import tk.mybatis.springboot.util.JedisUtil;

@Controller
@RequestMapping("/user")
@SuppressWarnings("unchecked")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IdCardService idCardService;
	
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	public static final int DefaultTime = 10*60;//10min
	
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
	
	
	//身份证检查
	@RequestMapping("/cards/idcard/check")
	@ResponseBody
	public CommonResult idCardCheck(
			String idCardNo,
			String passwd,
			HttpSession session
			) throws InterruptedException, ExecutionException, Exception {
		//session 获取用户信息
		Hashtable<String,Object> userData = (Hashtable<String, Object>)session.getAttribute("user");
		String trueIdcardNo = (String)userData.get("idcard_no");
		//与session里的比对
		if(!trueIdcardNo.equals(idCardNo)) {
			return new CommonResult(301, "错误的身份证号码", null);
		}
		int checkResult = userService.checkIdCard(trueIdcardNo, passwd);
		if(checkResult == 1) {//密码正确
			//添加 idcard session
			//Hashtable<String, Object> idCardData = idCardService.getIdCardDetailByIdCardNo(trueIdcardNo);
			//session.setAttribute("idCard", idCardData);
			HashMap dataMap = idCardService.auth(idCardNo, passwd);
			if(dataMap != null ){
				dataMap.putAll(IdCardUtil.getIdCardInfo(idCardNo));
				dataMap.put("idCardNo", idCardNo);
				String uuid = CommonUtil.GenerateUUID();
				dataMap.put("uuid", uuid);
				String authStr = objectMapper.writeValueAsString(dataMap);
				JedisUtil.add(uuid, authStr, DefaultTime);//添加到jedis，设置10min过期
				session.setAttribute("idCardAuth", authStr);//设置session
				return new CommonResult(200, "ok", dataMap);
			}else{
				return new CommonResult(301, "验证失败", null);			
			}
		}else {
			return new CommonResult(301, "错误的密码", null);
		}
	}
	
	
	
	
	
}
