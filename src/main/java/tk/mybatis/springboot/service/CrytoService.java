package tk.mybatis.springboot.service;

import java.util.HashMap;
import java.util.Hashtable;

public interface CrytoService {
	Hashtable<String, String> decodeIdCard(String idCardNo, String privateKey) throws Exception;

	HashMap decodeIdCardByPrivateKey(String encodedDate, String privateKey) throws Exception;

}
