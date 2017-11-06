package tk.mybatis.springboot.test;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Test;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tk.mybatis.springboot.util.CommonUtil;
import tk.mybatis.springboot.util.CrytoUtil;
import tk.mybatis.springboot.util.EthereumUtil;




public class CrytoTest {
	
	 public static String str2HexStr(String str) {    
	        char[] chars = "0123456789ABCDEF".toCharArray();    
	        StringBuilder sb = new StringBuilder("");  
	        byte[] bs = str.getBytes();    
	        int bit;    
	        for (int i = 0; i < bs.length; i++) {    
	            bit = (bs[i] & 0x0f0) >> 4;    
	            sb.append(chars[bit]);    
	            bit = bs[i] & 0x0f;    
	            sb.append(chars[bit]);    
	        }    
	        return sb.toString();    
	    }   
	  
	   
	  
	  
	 /** 
	   * 把16进制字符串转换成字节数组 
	   * @param hexString 
	   * @return byte[] 
	   */  
	  public static byte[] hexStringToByte(String hex) {  
	   int len = (hex.length() / 2);  
	   byte[] result = new byte[len];  
	   char[] achar = hex.toCharArray();  
	   for (int i = 0; i < len; i++) {  
	    int pos = i * 2;  
	    result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));  
	   }  
	   return result;  
	  }  
	    
	 private static int toByte(char c) {  
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);  
	    return b;  
	 }  
	 
	    public static String toHexString1(byte[] b){  
	        StringBuffer buffer = new StringBuffer();  
	        for (int i = 0; i < b.length; ++i){  
	            buffer.append(toHexString1(b[i]));  
	        }  
	        return buffer.toString();  
	    }  
	    public static String toHexString1(byte b){  
	        String s = Integer.toHexString(b & 0xFF);  
	        if (s.length() == 1){  
	            return "0" + s;  
	        }else{  
	            return s;  
	        }  
	    }  
	
	@Test
	public void Test1() throws JsonProcessingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		String idCardJsonStr = CommonUtil.idCardInformationToJsonString("张三石", "男", "新疆维吾尔族", "2012-12-12", "湖南省益阳市湖南省益阳市湖南省益阳市湖南省益阳市湖南省益阳市", "430902123412341234");//将身份信息转换成json字符串
		//System.out.println(idCardJsonStr);
    	KeyPair keypair = CrytoUtil.genRSAKeyPair();
    	Cipher cipher = null; 
    	cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, keypair.getPublic());  
		byte[] hashedDate = CrytoUtil.KeccakEncode(idCardJsonStr.getBytes());//对身份信息进行Hash		
        byte[] output = cipher.doFinal(hashedDate);
        System.out.println(new String(output));
        
        String a = toHexString1(output);
        a = Numeric.toHexStringNoPrefix(output);
        System.out.println(a);
       byte[] b = Numeric.hexStringToByteArray(a);
        
       // byte[] b = hexStringToByte(a);
        System.out.println(new String(b));
        System.out.println(b.length);
        System.out.println(Numeric.toHexStringNoPrefix(b));
        
/*        //解密
        cipher.init(Cipher.DECRYPT_MODE, keypair.getPrivate());  
        byte[] output2 = cipher.doFinal(b);  
        System.out.println(new String(output2));*/
	}
	
	
	public static void main(String[] args) {
		Hashtable<String, String> dataMap = new Hashtable<String, String>();
/*		dataMap.put("name", name);
		dataMap.put("sex", sex);
		dataMap.put("nation", nation);
		dataMap.put("address", address);
		dataMap.put("date", date);
		dataMap.put("idCardNo", idCardNo);
		ObjectMapper mapper = new ObjectMapper();*/
		//mapper.writeValueAsString(dataMap);

	}

}
