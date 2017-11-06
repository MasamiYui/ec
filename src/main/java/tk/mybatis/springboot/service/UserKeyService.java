package tk.mybatis.springboot.service;

import tk.mybatis.springboot.model.UserKey;


public interface UserKeyService {
	
	public int addNewKeyPair(String idCardNo, String passwd);
	
	public int check(String idCardNo);
	
	public UserKey getDetail(String idCardNo);
}
