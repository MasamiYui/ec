package tk.mybatis.springboot.service;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

































import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.springboot.mapper.IdCardMapper;
import tk.mybatis.springboot.mapper.UserKeyMapper;
import tk.mybatis.springboot.model.IdCard;
import tk.mybatis.springboot.model.UserKey;
import tk.mybatis.springboot.util.CommonUtil;
import tk.mybatis.springboot.util.CrytoUtil;
import tk.mybatis.springboot.util.EthereumUtil;

@SuppressWarnings("rawtypes")
@Service
public class IdCardServiceImpl implements IdCardService{
	
	@Autowired
	private IdCardMapper idCardDao;
	
	@Autowired
	private UserKeyMapper userKeyDao;
	
	@Autowired
	private CrytoService crytoService;

	@Override
	public String addToBlockChain(String name, String nation,
			String address, String idCardNo, String publicKeyStr)
			throws Exception {
		//String idCardJson = CommonUtil.idCardInformationToJsonString(name, sex, nation, date, address, idCardNo);//将进行转成json
		String rawData = name+"|"+nation+"|"+address;
		//System.out.println(rawData);
		RSAPublicKey publicKey = CrytoUtil.loadPublicKeyByStr(publicKeyStr);
		String data = Numeric.toHexString(CrytoUtil.rsaEncode(publicKey,
				rawData.getBytes()));
		System.out.println("blockchaindata:"+data);
		System.out.println("length:"+data.length());
		String txHash = EthereumUtil.addIdCardInformation(idCardNo, data);
		System.out.println(txHash);
		return txHash;
	}

	@Override
	public int addToDB(String name, String sex, String nation, String date,
			String address, String idCardNo, String txHash, String url)
			throws InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchProviderException,
			JsonProcessingException, Exception {
		
		IdCard idCard = new IdCard(name, sex, nation, date, address, idCardNo, txHash, 
				String.valueOf(new Date().getTime()), url,0);
		int result = idCardDao.add(idCard);
		return result;
	}

	@Override
	public int check(String idCardNo) {
		return idCardDao.check(idCardNo);
	}

	@Override
	public int confirm(String idCardNo) throws Exception {
		int result = 0;
		int state = EthereumUtil.selectIdCardState(idCardNo);
		//System.out.println("state:"+state);
		if(state == 1){//正常
			result = idCardDao.updateState(idCardNo);			
		}else{
			String newTxHash = reAdd(idCardNo);
			if(newTxHash.equals("") || newTxHash == null){
				result = 3;//重新提交后还是有问题
			}else{
				result = 2;//重新提交，还需待会儿继续验证
			}
			
		}
		return result;
	}

	@Override
	public int compare(String idCardNo, String jsonStr, String encodeData) throws Exception {
		@SuppressWarnings("unused")
		int result = 0;
		String privateKeyStr = userKeyDao.selectDetailByIdCardNo(idCardNo).getPrivate_key();
		RSAPrivateKey rsaPrivateKey = CrytoUtil.loadPrivateKeyByStr(privateKeyStr);
		byte[] data = CrytoUtil.rsaDecrypt(rsaPrivateKey, encodeData.getBytes());
		System.out.println("d:"+data);
		@SuppressWarnings("unused")
		byte[] jsonData = CrytoUtil.KeccakEncode(jsonStr.getBytes());
		System.out.println("d2:"+jsonStr);
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo idCardList(Integer page, Integer pageSize) {
	     PageHelper.startPage(page, pageSize);  
		 List<HashMap> idCards = idCardDao.selectAllIdCard();
		 return new PageInfo(idCards);
	}

	@Override
	public Hashtable getBlockDataDetailById(int id) throws InterruptedException, ExecutionException {
		Hashtable<String, Object> dataTable = new Hashtable<String, Object>();
		HashMap idCardMap = idCardDao.selectIdCardById(id);
		String idCardNo = (String)idCardMap.get("idcard_no");
		String txHash = (String)idCardMap.get("txhash");
		String inputData = EthereumUtil.selectIdCardInformation(idCardNo);
		TransactionReceipt transactionReceipt = EthereumUtil.selectTransactionReceiptByTransactionHash(txHash);
		dataTable.put("blockNumber", transactionReceipt.getBlockNumber().toString());
		dataTable.put("txHash", txHash);
		dataTable.put("index", transactionReceipt.getTransactionIndex().toString());
		dataTable.put("inputData", inputData);
		dataTable.put("referenWeb", "https://ropsten.etherscan.io/tx/"+txHash);
		return dataTable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo getIdCardsByLikeIdCardNo(String idCardNo, int page) {
		 PageHelper.startPage(page, 10);  
		 List<HashMap> idCards = idCardDao.selectIdCardsByLikeIdCardNo("%"+idCardNo+"%");
		 return new PageInfo(idCards);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo getIdCardsByLikeName(String name, int page) {
		PageHelper.startPage(page, 10);  
		List<HashMap> idCards = idCardDao.selectIdCardsByLikeName("%"+name+"%");
		return new PageInfo(idCards);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap auth(String idCardNo, String password) throws Exception {
		//1.通过idCardNo和password查询用户的私钥
		String privateKey = userKeyDao.selectPrivateKeyByIdcardNoAndPassword(idCardNo,password);
		//2.通过idCardNo从区块链中获取用私钥签名后的信息
		String encodedDate = EthereumUtil.selectIdCardInformation(idCardNo);
		//3.解析
		HashMap data = crytoService.decodeIdCardByPrivateKey(encodedDate,privateKey);
		//4.添加用户图片信息
		String url = idCardDao.selectImgUrlByIdCardNo(idCardNo);
		data.put("url", url);
		return data;
	}

	@Override
	public String reAdd(String idCardNo) throws Exception {
		HashMap idCardMap = idCardDao.selectIdCardByCardNo(idCardNo);
		String name = (String)idCardMap.get("name");
		String nation = (String)idCardMap.get("nation");
		String address = (String)idCardMap.get("address");
		String rawData = name+"|"+nation+"|"+address;
		UserKey userKey = userKeyDao.selectDetailByIdCardNo(idCardNo);
		String publicKeyStr = userKey.getPublic_key();
		RSAPublicKey publicKey = CrytoUtil.loadPublicKeyByStr(publicKeyStr);
		String data = Numeric.toHexString(CrytoUtil.rsaEncode(publicKey,
				rawData.getBytes()));
		String txHash = EthereumUtil.addIdCardInformation(idCardNo, data);
		System.out.println(txHash);
		return txHash;
	}

	@Override
	public Hashtable<String, Object> getIdCardDetailByIdCardNo(String idCardNo) {
		Hashtable<String, Object> idCardData = idCardDao.selectIdCardDetailByIdCardNo(idCardNo);
		return idCardData;
	}

}
