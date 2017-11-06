package tk.mybatis.springboot.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class EthereumUtil {
	public static  final Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/"));
	public static final String adminAddress = "0xe559eddf4367634912316d71d4f0b52766c64a79";
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000_000_000L);
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_700_000);
    //public static final String IdCardContractAddress = "0xfbe761b501f6b34f540db79949760ac11e487ae5";
    public static final String IdCardContractAddress = "0xf4c95185a414f24d7095ea5d3fc631789d66895e";
    public static Credentials credentials = null;
    static{
        try {
        	//credentials = WalletUtils.loadCredentials("123", "/usr/local/project/ECBlockChain/keystore");
        	credentials = WalletUtils.loadCredentials("123", "C:\\Users\\yin\\Desktop\\keystore");
        } catch (IOException | CipherException e) {
			e.printStackTrace();
		}
    }
    
	
    public static BigInteger getNonce(Web3j web3j,String address) throws InterruptedException, ExecutionException {
    	EthGetTransactionCount ethGetTransactionCount;
		ethGetTransactionCount = web3j.ethGetTransactionCount(
				address, DefaultBlockParameterName.LATEST).sendAsync().get();
    	return ethGetTransactionCount.getTransactionCount();
    }
    
	public static final String getKeystoreStr(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, JsonProcessingException{
		return new ObjectMapper()
				.writeValueAsString(Wallet
						.createStandard(password,
								Keys.createEcKeyPair()));
	}
	
	@SuppressWarnings("rawtypes")
	public static final String addIdCardInformation(String idCardNo, String data) throws InterruptedException, ExecutionException{
		Function function = new Function("add", Arrays.<Type>asList(new Utf8String(idCardNo), new Utf8String(data)), Collections.<TypeReference<?>>emptyList());
		String functionEncodedStr = FunctionEncoder.encode(function);
		BigInteger nonce = getNonce(web3j, adminAddress);
		System.out.println("nonce:"+nonce);
		RawTransaction rawTransaction =  RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, IdCardContractAddress, functionEncodedStr);
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);
		System.out.println(hexValue);
		//String  response = EthereumUtil.web3j.ethSendRawTransaction(hexValue).sendAsync().get().getRawResponse();
		//System.out.println(response);
		String txHash = EthereumUtil.web3j.ethSendRawTransaction(hexValue).sendAsync().get().getTransactionHash();
		return txHash;
	}
	
	@SuppressWarnings("rawtypes")
	public static final int selectIdCardState(String idCardNo) throws InterruptedException, ExecutionException{
		Function function = new Function("stateOf", 
	                Arrays.<Type>asList(new Utf8String(idCardNo)), 
	                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
		 String data = FunctionEncoder.encode(function);
		 org.web3j.protocol.core.methods.request.Transaction tx = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(adminAddress, IdCardContractAddress, data);
		 return preHexToStandardNumber(web3j.ethCall(tx, DefaultBlockParameterName.LATEST).sendAsync().get().getResult());
	}
	
	@SuppressWarnings("rawtypes")
	public static final String selectIdCardInformation(String idCardNo) throws InterruptedException, ExecutionException{
        Function function = new Function("idCardOf", 
                Arrays.<Type>asList(new Utf8String(idCardNo)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
		 String data = FunctionEncoder.encode(function);
		 System.out.println("data:"+data);
		 org.web3j.protocol.core.methods.request.Transaction tx = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(adminAddress, IdCardContractAddress, data);
		 return FunctionReturnDecoder.decode(web3j.ethCall(tx, DefaultBlockParameterName.LATEST).sendAsync().get().getResult(), function.getOutputParameters()).get(0).getValue().toString();
	}
	
	
	
	public static final int preHexToStandardNumber(String str){
		return Integer.parseInt(Numeric.cleanHexPrefix(str));
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, CipherException {
		//System.out.println(selectIdCardInformation("43090219941234129"));
		//String raw = "200000000000000000000000000000000000000000000000000000000000000102307838623333313762383730633734633166633466346635623531346661613564653434626130396161636536366637393737636137633033396533303134396464353936616461356534303364666264323436336133613134646432383037393534363366633238663263393133393137616439666661333237626234313734363035393231396337663539656331336138363236383738643232326164376364393135333433353932373530643862616134326536303537383338356231343231633466636237383565396563633163353536393738653435633032313034393233323230386261373835653163333938346563623136363566363665633037000000000000000000000000000000000000000000000000000000000000";
		
		//System.out.println(Numeric.toHexStringNoPrefix(new BigInteger(raw)));
		//byte[] a = Numeric.hexStringToByteArray("102307832363961323836316130316266356465366239633135323163346661316462306265306230393032306161636534646331646461306232376537386131313836343065623137303931613533363137363466613832393834386362343835353266373664306263353937656631616164373037363539323137333639663161386637316436383032653338323237303231653939636234303761386264303133613030366634363865313663626336393039373835613862376633343734343461326430313435396266313535323336363366376361373039376364613061643364646134626335323363373530363261333734373462383031666161346138");
		//System.out.println(a.length);
		//System.out.println(Numeric.toHexString(a));
		/*credentials = WalletUtils.loadCredentials("123", "src/main/resources/Idcard/keystore");
		String value = Idcard.load(IdCardContractAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT).idCardOf(new Utf8String("43090219941234129")).get().getValue();
		System.out.println(value);*/
		System.out.println(selectIdCardState("430902199412341300"));
		
	}

	public static TransactionReceipt selectTransactionReceiptByTransactionHash(String txHash) throws InterruptedException, ExecutionException {
		TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(txHash).sendAsync().get().getResult();
		return transactionReceipt;
	}
	

	
	
}
