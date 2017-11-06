package tk.mybatis.springboot.service;

import java.security.KeyPair;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.springboot.mapper.UserKeyMapper;
import tk.mybatis.springboot.model.UserKey;
import tk.mybatis.springboot.util.CrytoUtil;

@Service
public class UserKeyServiceImpl implements UserKeyService {

	@Autowired
	private UserKeyMapper userKeyDao;
	
	@Override
	public int addNewKeyPair(String idCardNo, String passwd) {
		KeyPair keyPair = CrytoUtil.genRSAKeyPair();
		String privateKey = new String(Base64.encode(keyPair.getPrivate().getEncoded()));
		String publicKey = new String(Base64.encode(keyPair.getPublic().getEncoded()));
		int reusult = userKeyDao.add(new UserKey(idCardNo,publicKey,privateKey,passwd));
		return reusult;
	}

	@Override
	public int check(String idCardNo) {
		return userKeyDao.check(idCardNo);
	}

	@Override
	public UserKey getDetail(String idCardNo) {
		return userKeyDao.selectDetailByIdCardNo(idCardNo);
	}

}
