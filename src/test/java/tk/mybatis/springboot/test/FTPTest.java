package tk.mybatis.springboot.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPTest {
    public static void main(String[] args) throws SocketException, IOException {
    	FTPClient ftpClient= new FTPClient();  
    	ftpClient.connect("123.207.35.82",21);  
        ftpClient.login("blockchain", "blockchain");  
        FileInputStream inputStream=new FileInputStream(new File("D:\\temp\\shangdiyinhao.jpg"));  
        //设置上传的路径  
        ftpClient.changeWorkingDirectory("/usr/local/project/img");  
        //修改上传格式  
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
        //第一个参数：服务器端文档名  
        //第二个参数，上传文档的inputStream  
        ftpClient.storeFile("test1605.jpg", inputStream);  
        //关闭链接  
        ftpClient.logout();  
	}
    

}
