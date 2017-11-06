package tk.mybatis.springboot.util;

import java.util.Hashtable;


import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
	public static final String idCardInformationToJsonString(
			String name,//姓名
			String sex,//性别
			String nation,//民族
			String date,//出生日期
			String address,//住址
			String idCardNo//身份证编号
			) throws JsonProcessingException{
		Hashtable<String, String> dataMap = new Hashtable<String, String>();
		dataMap.put("name", name);
		dataMap.put("sex", sex);
		dataMap.put("nation", nation);
		dataMap.put("address", address);
		dataMap.put("date", date);
		dataMap.put("idCardNo", idCardNo);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dataMap);
	}
	
	public static final String GenerateUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
