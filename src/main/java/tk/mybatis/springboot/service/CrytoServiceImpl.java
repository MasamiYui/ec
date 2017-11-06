package tk.mybatis.springboot.service;

import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Hashtable;

import org.springframework.stereotype.Service;
import org.web3j.utils.Numeric;

import tk.mybatis.springboot.util.CrytoUtil;
import tk.mybatis.springboot.util.EthereumUtil;

@Service
public class CrytoServiceImpl implements CrytoService{

	@Override
	public Hashtable<String, String> decodeIdCard(String idCardNo,
			String privateKeyStr) throws Exception {
		String blockChainData = EthereumUtil.selectIdCardInformation(idCardNo);//在以太坊上查询加密的字符串
		RSAPrivateKey privateKey = CrytoUtil.loadPrivateKeyByStr(privateKeyStr);//加载私钥
    	byte[] data = CrytoUtil.rsaDecrypt(privateKey, Numeric.hexStringToByteArray(blockChainData));//解密
		System.out.println(new String(data));
    	return null;
	}

	@Override
	public HashMap decodeIdCardByPrivateKey(String encodedDate,
			String privateKeyStr) throws Exception {
		RSAPrivateKey privateKey = CrytoUtil.loadPrivateKeyByStr(privateKeyStr);//加载私钥
    	byte[] data = CrytoUtil.rsaDecrypt(privateKey, Numeric.hexStringToByteArray(encodedDate));//解密
		String decodedData = new String(data);
		if(decodedData.equals("") || decodedData == null){
			return null;
		}else{
			//430902199010111011
			HashMap dataMap = new HashMap();
			System.out.println(decodedData);
			String[] decodedDataArr = decodedData.split("\\|");
			System.out.println(decodedDataArr.length);
			dataMap.put("name", decodedDataArr[0]);
			dataMap.put("nation", decodedDataArr[1]);
			dataMap.put("address", decodedDataArr[2]);
			return dataMap;
		}
	}

}
