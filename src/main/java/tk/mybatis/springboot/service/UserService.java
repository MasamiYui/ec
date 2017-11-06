package tk.mybatis.springboot.service;

import java.util.Hashtable;

public interface UserService {
	public int addUser(String name,String phone, String idCardNo, String passwd);
	
	public int checkUser(String phone, String idCardNo);

	public Hashtable<String, Object> checkLogin(String phone, String passwd);
}
