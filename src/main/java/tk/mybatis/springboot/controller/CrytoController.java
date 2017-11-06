package tk.mybatis.springboot.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import com.fasterxml.jackson.core.JsonProcessingException;

import tk.mybatis.springboot.service.IdCardService;
import tk.mybatis.springboot.util.CommonResult;
import tk.mybatis.springboot.util.EthereumUtil;

@Controller
@RequestMapping("/cryto")
public class CrytoController {
	
	@Autowired
	private IdCardService idCardService;
	
	@RequestMapping("/encode/hash")
	@ResponseBody
	public CommonResult hashEncode(
			String raw
			){
		
		return null;
	}
	
	@RequestMapping("/decode")
	@ResponseBody
	public CommonResult decode(
			String encodedStr
			){
		return null;
	}
	
	@RequestMapping("/new_keypair")
	@ResponseBody
	public CommonResult generateNewKeyStore(
			String password
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, CipherException{
		ECKeyPair keyPair = Keys.createEcKeyPair();
		String privateKey = keyPair.getPrivateKey().toString();
		String publicKey = keyPair.getPublicKey().toString();
		Hashtable<String, String> keyTable = new Hashtable<String, String>();
		keyTable.put("publicKey", publicKey);
		keyTable.put("privateKey",privateKey);
		return new CommonResult(200, "ok", keyTable);
	}
	
	
	
	@RequestMapping("/check_data")
	@ResponseBody
	public CommonResult checkData(
			String password
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, CipherException{
		return new CommonResult(200, "ok", "checkData");
	}
	
	
	@RequestMapping("/save_user_key")
	@ResponseBody
	public CommonResult saveUser(
			String password
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, CipherException{
		return new CommonResult(200, "ok", EthereumUtil.getKeystoreStr(password));
	}
	
/*	@RequestMapping("/decode")
	@ResponseBody
	public CommonResult check(
			String idCardNo,
			String jsonStr,
			String encodeData
			) throws Exception{
		int result = idCardService.compare(idCardNo, jsonStr, encodeData);
		return new CommonResult(200, "ok", result);
	}*/
	
	
	
	
	
	
}
