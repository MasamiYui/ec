package tk.mybatis.springboot.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

@SuppressWarnings("rawtypes")
public interface IdCardService {
	public String addToBlockChain(
			String name,//姓名
			//String sex,//性别
			String nation,//民族
			//String date,//出生日期
			String address,//住址
			String idCardNo,//身份证编号
			String publicKey
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, Exception;
	
	public int addToDB(
			String name,//姓名
			String sex,//性别
			String nation,//民族
			String date,//出生日期
			String address,//住址
			String idCardNo,//身份证编号
			String publicKey,
			String url
			) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, JsonProcessingException, Exception;
	
	public int check(String idCardNo);
	
	public int confirm(String idCardNo) throws InterruptedException, ExecutionException, Exception;
	
	public int compare(String idCardNo, String jsonStr, String encodeData) throws Exception;

	public PageInfo idCardList(Integer page, Integer pageSize);

	public Hashtable getBlockDataDetailById(int id) throws InterruptedException, ExecutionException;

	public PageInfo getIdCardsByLikeIdCardNo(String idCardNo, int page);
	
	public PageInfo getIdCardsByLikeName(String name, int page);

	public HashMap auth(String idCardNo, String password) throws InterruptedException, ExecutionException, Exception;
	
	public String reAdd(String id) throws Exception;

	public Hashtable<String, Object> getIdCardDetailByIdCardNo(String idCardNo);

}
