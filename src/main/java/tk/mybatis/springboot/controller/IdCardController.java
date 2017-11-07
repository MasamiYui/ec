package tk.mybatis.springboot.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.springboot.model.UserKey;
import tk.mybatis.springboot.service.CrytoService;
import tk.mybatis.springboot.service.IdCardService;
import tk.mybatis.springboot.service.UserKeyService;
import tk.mybatis.springboot.util.CommonResult;
import tk.mybatis.springboot.util.CommonUtil;
import tk.mybatis.springboot.util.EthereumUtil;
import tk.mybatis.springboot.util.IdCardUtil;
import tk.mybatis.springboot.util.JedisUtil;

@Controller
@RequestMapping("/idcard")
public class IdCardController {
	
	public static final int DefaultTime = 10*60;//10min
	public static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private IdCardService idCardService;
	
	@Autowired
	private UserKeyService userKeyService;
	
	@Autowired
	CrytoService crytoService;
	
	
	@RequestMapping("/add")
	@ResponseBody
	public CommonResult add(
			String name,//姓名
			String sex,//性别
			String nation,//民族
			String date,//出生日期
			String address,//住址
			String idCardNo,//身份证编号
			String passwd,
			String url//照片的Http地址
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, Exception{
		if(url == null || url.equals("")) {
			return new CommonResult(301, "请先上传 图片", null);
		}
		Map idCardmap = null;
		if(idCardNo.length() == 18){
			idCardmap = IdCardUtil.getIdCardInfo(idCardNo);
			if(!idCardmap.get("date").equals(date)){
				return new CommonResult(301, "错误的日期或身份证号码", null);
			}			
		}else if(idCardNo.length() == 15){
			idCardmap = IdCardUtil.getIdCardInfo(idCardNo);
			if(!idCardmap.get("date").equals(date)){
				return new CommonResult(301, "错误的日期或身份证号码", null);
		}
		}else{
			return new CommonResult(301, "错误的身份证号码", null);
		}
		
		int result = 0;
		String publicKey = null;
		result = userKeyService.check(idCardNo);
		
		if(result >0){
			UserKey userKey = userKeyService.getDetail(idCardNo);
			publicKey = userKey.getPublic_key();
		}else{
			result = userKeyService.addNewKeyPair(idCardNo,passwd);
			if(result <1){
				return new CommonResult(301, "err to insert new keyPair", null);
			}
			UserKey userKey = userKeyService.getDetail(idCardNo);
			publicKey = userKey.getPublic_key();
		}
		
		result = idCardService.check(idCardNo);
		if(result >0){
			return new CommonResult(301, "idCardNo exist", null);
		}
		
		String txHash = idCardService.addToBlockChain(name, nation, address, idCardNo, publicKey);
		if(txHash==null || txHash.equals("")){
			return new CommonResult(301, "blockchain insert err", null);
		}
		
		result = idCardService.addToDB(name, sex, nation, date, address, idCardNo, txHash, url);
		if(result == 1){
			return new CommonResult(200, "ok", null);
		}else{
			return new CommonResult(301, "db insert err", null);
		}
		
	}
	
	
	//重新添加一次
	@RequestMapping("/readd")
	@ResponseBody
	public CommonResult reAdd(
			String id
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, Exception{
	
		return null;

	}
	
	@RequestMapping("/list/{page}/{pageSize}")
	@ResponseBody
	public CommonResult query(@PathVariable Integer page, @PathVariable Integer pageSize){
		@SuppressWarnings("rawtypes")
		PageInfo data = idCardService.idCardList(page,pageSize);
		return new CommonResult(200, "ok", data);
	}
	
	
	
	@RequestMapping("/confirm")
	@ResponseBody
	public CommonResult confirm(String idCardNo) throws Exception{
		//System.out.println("idCardNo："+idCardNo);
		int result = idCardService.confirm(idCardNo);
		if(result == 1){
			return new CommonResult(200, "ok", 1);
		}else if(result ==2){
			return new CommonResult(201, "未录入区块，重新提交，请稍后检查", null);
		}else if(result ==3){
			return new CommonResult(201, "未录入区块，重新提交失败", null);
		}else{
			
		}return new CommonResult(201, "位置错误", null);
		
	}
	
	@RequestMapping("/blockchain_data")
	@ResponseBody
	public CommonResult getData(String idCardNo) throws InterruptedException, ExecutionException{
		String data = EthereumUtil.selectIdCardInformation(idCardNo);
		return new CommonResult(200, "ok", data);
	}
	
	@RequestMapping("/decode")
	@ResponseBody
	public CommonResult decodeData(
			String idCardNo,
			String privateKeyStr
			) throws Exception{
		//String data = EthereumUtil.selectIdCardInformation(idCardNo);
		Hashtable table = crytoService.decodeIdCard(idCardNo, privateKeyStr);
    	return new CommonResult(200, "ok", null);
	}
	

	@RequestMapping("/blockdata")
	@ResponseBody
	public CommonResult detailBlockData(
			String id
			) throws Exception{
		Hashtable data = idCardService.getBlockDataDetailById(Integer.parseInt(id));
    	return new CommonResult(200, "ok", data);
	}
	
	
	@RequestMapping("/get/byidcardno")
	@ResponseBody
	public CommonResult getIdCardsByLikeIdCardNo(
			String idCardNo,
			String page
			) throws Exception{
		PageInfo data = idCardService.getIdCardsByLikeIdCardNo(idCardNo, Integer.parseInt(page));
    	return new CommonResult(200, "ok", data);
	}
	
	@RequestMapping("/get/byname")
	@ResponseBody
	public CommonResult getIdCardsByIdLikeName(
			String name,
			String page
			) throws Exception{
		PageInfo data = idCardService.getIdCardsByLikeName(name, Integer.parseInt(page));
    	return new CommonResult(200, "ok", data);
	}
	
	@RequestMapping("/auth")
	@ResponseBody
	public CommonResult auth(
			String idCardNo,
			String password,
			HttpSession session
			) throws Exception{
		HashMap dataMap = idCardService.auth(idCardNo, password);
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
	}
	
	@RequestMapping("/authresult")
	@ResponseBody
	public CommonResult authResult(
			HttpSession session
			) throws Exception{
		String authStr = (String) session.getAttribute("idCardAuth");
		if(authStr=="" || authStr.equals(null)){
			return new CommonResult(301, "验证失败", null);
		}else{
			return new CommonResult(200, "ok", objectMapper.readValue(authStr, Map.class));
		}
	}
	//authcheck
	@RequestMapping("/authcheck")
	@ResponseBody
	public CommonResult authCheck(
			String checkid
			) throws Exception{
		System.out.println(checkid);
		String authStr = JedisUtil.get(checkid);
		if(authStr=="" || authStr.equals(null)){
			return new CommonResult(301, "验证失败", null);
		}else{
			return new CommonResult(200, "ok", objectMapper.readValue(authStr, Map.class));
		}
	}
	
	
	
}
