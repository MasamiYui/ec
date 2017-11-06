package tk.mybatis.springboot.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Base64;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

public class CrytoUtil {
	public static final Keccak.Digest256 d = new Keccak.Digest256();
	 /** 
     * 字节数据转字符串专用集合 
     */  
    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
	
	/**
	 * KeccakHash
	 * @param raw
	 * @return
	 */
	public static byte[] KeccakEncode(
			byte[] raw
			){
        d.update(raw);
        byte[] hashedData = d.digest();
        return hashedData;
	}
	
	/**
	 * sign
	 * @param data
	 * @param keyPair
	 * @return
	 */
	public static SignatureData sign(
			byte[] data,//需签名数据
			ECKeyPair keyPair//密钥对
			){
		return Sign.signMessage(data, keyPair);
	}
	
	
	/**
	 * 生成ECKeyPair
	 * @return
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static final ECKeyPair generateNewECKeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException{
		return Keys.createEcKeyPair();
	}
	
	

  
    /** 
     * 生成RSA密钥对 
     */  
    public static KeyPair genRSAKeyPair() {  
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
        KeyPairGenerator keyPairGen = null;  
        try {  
            keyPairGen = KeyPairGenerator.getInstance("RSA");  
        } catch (NoSuchAlgorithmException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        // 初始化密钥对生成器，密钥大小为96-1024位  
        keyPairGen.initialize(1024,new SecureRandom());  
        // 生成一个密钥对，保存在keyPair中  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        return keyPair;
    }  
    
    
    public static byte[] rsaEncode(
    		PublicKey publicKey,
    		byte[] rawData
    		) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    	Cipher cipher = null; 
    	cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        byte[] encodedData = cipher.doFinal(rawData);  
        return encodedData;
    }
    
    /** 
     * 从字符串中加载公钥 
     *  
     * @param publicKeyStr 
     *            公钥数据字符串 
     * @throws Exception 
     *             加载公钥时产生的异常 
     */  
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)  
            throws Exception {  
        try {  
            byte[] buffer = Base64.decode(publicKeyStr);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法");  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("公钥非法");  
        } catch (NullPointerException e) {  
            throw new Exception("公钥数据为空");  
        }  
    }  
    
    
    /**
     * 从字符串中加载私钥
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)  
            throws Exception {  
        try {  
            byte[] buffer = Base64.decode(privateKeyStr);  
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法");  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("私钥非法");  
        } catch (NullPointerException e) {  
            throw new Exception("私钥数据为空");  
        }  
    }  
    
    
    public static byte[] rsaDecrypt(RSAPrivateKey privateKey, byte[] cipherData)  
            throws Exception {  
        if (privateKey == null) {  
            throw new Exception("解密私钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA");  
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
            byte[] output = cipher.doFinal(cipherData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此解密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("解密私钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("密文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("密文数据已损坏");  
        }  
    }  
    
    
    public static void main(String[] args) throws Exception {
/*    	byte[] encoded = rsaEncode(genRSAKeyPair().getPublic(),"尹伊伊|汉|湖村电信湖南省益阳市电湖电信湖南省益阳市农村电信湖南省益阳市益阳".getBytes());
    	System.out.println(new String(encoded));
    	String hex = Numeric.toHexString(encoded);
    	System.out.println(hex);
    	System.out.println(new String(Numeric.hexStringToByteArray(hex)));*/
    	
    	/*String rawData = "0x54dfa54624b59b28618732a96e173520373b9d04e477e1a0cbf"
    			+ "c467bba4498cb20e39f1e2f046fef682721257a5e79d1efb69337293d79a4e"
    			+ "fb492884cf696d8ec2c1a47bdccfa7c409e6bb9f43036f077b39558a48305b21"
    			+ "4c8b8a950890e434d1d329810350b91bc77dc7f2c0d70d7f19f3f0b53f981490310b904ec08964f";
    	String privateKeyStr = "+YGRyxAm/xrMKawJAW1JjPXAVZcTHkDOeWkL/tvaVc3atQu1Ym0EP3Vex1UN"
    			+ "isMhmA/pss8BwZ4cvQ4gxiZtWUDjGml99Fud0It5yWwJAOBopUKNlewMPK4mHFvUpFOWAnRZmcpw"
    			+ "JMRSZm1nEj7jBO7lFcXa2b0yG7Vs5TIPBHg8yt1L3S7Hy1Yy6alYleA==";
    	RSAPrivateKey rsaPrivateKey = CrytoUtil.loadPrivateKeyByStr(new String(Numeric.hexStringToByteArray(privateKeyStr)));
    	*/
/*    	byte[] data = "尹伊伊|汉|湖村电信湖南省益阳市电湖电信湖南省益阳市农村电信湖南省益阳市益阳".getBytes();
    	KeyPair pair = genRSAKeyPair();
    	String a = Numeric.toHexString(Base64.encode(pair.getPublic().getEncoded()));//这是保存在是数据库里的值（十六进制）
    	byte[] b = Numeric.hexStringToByteArray(a);
    	PublicKey puk = loadPublicKeyByStr(new String(b));
    	byte[] dataB = rsaEncode(puk, data);
    	String c = new String(Base64.encode(pair.getPrivate().getEncoded()));//数据库里存储的私钥值
    	System.out.println(c);*/
/*    	String dataB = "0x54dfa54624b59b28618732a96e173520373b9d04e477e1a0cbfc467bba4498cb20e39f1e2f046fef682721257a5e79d1efb69337293d79a4efb492884cf696d8ec2c1a47bdccfa7c409e6bb9f43036f077b39558a48305b214c8b8a950890e434d1d329810350b91bc77dc7f2c0d70d7f19f3f0b53f981490310b904ec08964f";
    	RSAPrivateKey privateKey = loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKCsPFLjVZIYaCj426onXvrASwGjRwh+mzLwLI2xvJCD+pN3R6KjWiQ+bjJQG5NonxOMBdl1YSZUDyXmDvbhmA2OjjScLpNiVksy6JPxy2GckXz28z0oySxhBen81KWrRxZPJY9mnruR4ewgUEO9PIqkGFo5Jlsa6ttU7DBvX/olAgMBAAECgYBohULExQBghqITrTfwq6X8xuTETvwhiYLakyTYlDvPYxu5nImsQkmcdERpVHVr4k5BgAA6I0KslVg5Vjx72+t2Ne7C00UtdFz44HztEAdc56J5RBFAUOER8u3ZfWpla74KdX41P6PxVy7KFQqWi1PDV0Ob5lJ4ejkFsoQtLGz1zQJBANa8BAMUiTMObqwPkVt31W2iVZDpphvcU/HRSqPpqi4dNPQ7wMhsZy99r1g9nwSEpzBsz0+NmG/2AuWdS1gsvncCQQC/jKBbqUW/mh5wIH/MXEXbGYq821t1bF59fGyyHnS60UXuaOGECe+VATSoe996ejQsYdPVneI38dZp+gs2CidDAkEAplcIyJYM3ccNRi3dDLzcUMwSemn0KdrYEBdvOWAVWxl23sa2xMrsd6ZlwPYjC23y0RlI0jg+YGRyxAm/xrMKawJAW1JjPXAVZcTHkDOeWkL/tvaVc3atQu1Ym0EP3Vex1UNisMhmA/pss8BwZ4cvQ4gxiZtWUDjGml99Fud0It5yWwJAOBopUKNlewMPK4mHFvUpFOWAnRZmcpwJMRSZm1nEj7jBO7lFcXa2b0yG7Vs5TIPBHg8yt1L3S7Hy1Yy6alYleA==");
    	byte[] dataC = rsaDecrypt(privateKey, Numeric.hexStringToByteArray(dataB));
    	System.out.println(new String(dataC));
    	*/
    	
	
    }
	
}
