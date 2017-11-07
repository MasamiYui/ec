package tk.mybatis.springboot.service;

import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.springboot.mapper.UserKeyMapper;
import tk.mybatis.springboot.mapper.UserMapper;
import tk.mybatis.springboot.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private UserKeyMapper userKeyDao;
	
	@Override
	public int addUser(String name, String phone, String idCardNo, String passwd) {
		int result = userDao.add(new User(name, phone, idCardNo, passwd, 1));
		return result;
	}

	@Override
	public int checkUser(String phone, String idCardNo) {
		//检查手机或身份证是否已经注册过
		int checkPhoneResult = userDao.checkPhone(phone);
		int checkIdCardResult = userDao.checkIdCardNo(idCardNo);
		if(checkPhoneResult == 1) {
			return 2;//手机已经被注册
		}
		if(checkIdCardResult == 1) {
			return 3;//身份证号已经被注册
		}
		return 1;//都没有被注册
	}

	@Override
	public Hashtable<String, Object> checkLogin(String phone, String passwd) {
		Hashtable<String, Object> result = userDao.checkLogin(phone, passwd);
		return result;
	}

	@Override
	public int checkIdCard(String idCardNo, String passwd) {
		if(userKeyDao.selectPasswdByIdCardNo(idCardNo).equals(passwd)) {
			return 1;//密码正确
		}
		return 0;//密码错误
	}

}
