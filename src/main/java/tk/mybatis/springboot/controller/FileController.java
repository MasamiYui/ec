package tk.mybatis.springboot.controller;

import java.io.IOException;
import java.util.Hashtable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tk.mybatis.springboot.util.CommonResult;
import tk.mybatis.springboot.util.CommonUtil;
import tk.mybatis.springboot.util.FtpUtil;


@Controller
@RequestMapping("/file")
public class FileController {
	//private final Logger logger = LoggerFactory.getLogger(FileController.class);
	private static String UPLOADED_BASE = "/usr/local/project/img/";
	private static String HTTP_BASE = "http://123.207.35.82:8686/";
	
	@RequestMapping("/uploadIdCardImg")
	@ResponseBody
	 public CommonResult uploadIdCardImg (MultipartFile file) throws IOException{
		String fileName = CommonUtil.GenerateUUID()+".jpg";
		if (file.isEmpty()) {
			 	System.out.println("empty");
	            return  new CommonResult(200, "ok", null);
	    }
        boolean uploadResult = FtpUtil.uploadFile("123.207.35.82",
        		21,
        		"blockchain",
        		"blockchain",
        		UPLOADED_BASE,
        		"idcard",
        		fileName,
        		file.getInputStream());
        if(uploadResult == false) {
        	return new CommonResult(301, "ftp上传失败", null);
        }
        Hashtable<String, Object> dataTable = new Hashtable<String, Object>();
        dataTable.put("http_url", HTTP_BASE+"idcard/"+fileName);
	    return new CommonResult(200, "ok", dataTable);
	}  

}
