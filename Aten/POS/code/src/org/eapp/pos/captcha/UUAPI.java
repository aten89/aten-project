package org.eapp.pos.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import javax.imageio.ImageIO;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class UUAPI {
	private final static String DLL_NAME = "UUWiseHelper";	//DLL文件名
	private final static String DLL_PATH;//绝对路径
	static {
		File f1 = new File(UUAPI.class.getResource(".").getFile(), DLL_NAME);
		DLL_PATH = f1.getPath();
	}
	private String username	= "ato200";	//UU用户名
	private String password	= "1314520"; //UU密码
	
	private int softID = 104468; //软件ID 获取方式：http://dll.uuwise.com/index.php?n=ApiDoc.GetSoftIDandKEY
	private String softKey = "d909a3bc62894ccf9ccc9623a69007b8"; //软件KEY 获取方式：http://dll.uuwise.com/index.php?n=ApiDoc.GetSoftIDandKEY
	private String dllVerifyKey = "B678C850-501A-4537-A6B3-35FDA0D50C75"; //校验API文件是否被篡改，实际上此值不参与传输，关系软件安全，高手请实现复杂的方法来隐藏此值，防止反编译,获取方式也是在后台获取软件ID和KEY一个地方
	private boolean	checkStatus = false;
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setSoftID(int softID) {
		this.softID = softID;
	}
	public void setSoftKey(String softKey) {
		this.softKey = softKey;
	}
	public void setDllVerifyKey(String dllVerifyKey) {
		this.dllVerifyKey = dllVerifyKey;
	}

	
	public interface UUDLL extends Library {
		//载入优优云的静态库
		UUDLL INSTANCE = (UUDLL) Native.loadLibrary(DLL_PATH, UUDLL.class);		
		public int uu_reportError(int id);		
		public int uu_setTimeOut(int nTimeOut);
		public int uu_loginA(String UserName, String passWord);
		public int uu_recognizeByCodeTypeAndBytesA (byte[] picContent, int piclen, int codeType, byte[] returnResult);
		public void uu_getResultA(int nCodeID,String pCodeResult);
		public int uu_getScoreA (String UserName, String passWord);	//查题分
		public int uu_easyRecognizeFileA(int softid,String softkey,String userName,String password,String imagePath,int codeType,byte[] returnResult);//一键识别函数
		public int uu_easyRecognizeBytesA(int softid,String softkey,String username,String pasword,byte[] picContent,int piclen,int codeType,byte[] returnResult);
		public void uu_CheckApiSignA(int softID,String softKey,String guid,String filemd5,String fileCRC,byte[] returnResult); //api校验函数
	}
	
	
	/**
	 * 获取用户当前帐户内剩余题分
	 * @return
	 */
	public int getScore() {
		return UUDLL.INSTANCE.uu_getScoreA(username, password);
	}
	
	/**
	 * 获取验证码
	 * @param data
	 * @param codeType
	 * @return
	 * @throws IOException
	 */
	public String[] getCaptcha(byte[] data,int codeType) throws IOException {
		if(!checkStatus){
			String rs[]={"-19004","API校验失败,或未校验"};
			return rs;
		}
		
//		File f = new File(picPath);
		byte[] by = null;
		try {
			by = toByteArray(new ByteArrayInputStream(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] resultBtye = new byte[100];		//为识别结果申请内存空间
		int codeID = UUDLL.INSTANCE.uu_easyRecognizeBytesA(softID, softKey, username, password, by, by.length, codeType, resultBtye);
		String resultResult = null;
		try {
			resultResult = new String(resultBtye,"GB2312");//如果是乱码，这改成UTF-8试试
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		resultResult=resultResult.trim();
		
		//下面这两条是为了防止被破解	
		
		String rs[]={String.valueOf(codeID),checkResult(resultResult, codeID)};
		return rs;
	}
	
	/**
	 * 验证码获取不对报告
	 * @param codeID
	 * @return
	 */
	public int reportError(int codeID) {
		return UUDLL.INSTANCE.uu_reportError(codeID);
	}
	
	/**
	 * 检查API接口是否成功
	 * @return
	 * @throws IOException
	 */
	public boolean checkAPI() throws IOException {
		String FILEMD5=getFileMD5(DLL_NAME+".dll"); //API文件的MD5值
		String FILECRC=doChecksum(DLL_NAME+".dll");	//API文件的CRC32值
		String GUID=md5(Long.toString(Math.round(Math.random()*11111+99999)));	//随机值，此值一定要每次运算都变化
		
		//本地验证结果:	
		String okStatus=md5(softID+(dllVerifyKey.toUpperCase())+GUID.toUpperCase()+FILEMD5.toUpperCase()+FILECRC.toUpperCase());

		byte[] CheckResultBtye=new byte[512];
		/**
		 * uu_CheckApiSignA用于防止别人替换优优云的API文件
		 * 后面对结果再进行校验则是避免被HOOK，从而防止恶意盗码
		 * */
		UUDLL.INSTANCE.uu_CheckApiSignA(softID, softKey.toUpperCase(), GUID.toUpperCase(), FILEMD5.toUpperCase(), FILECRC.toUpperCase(), CheckResultBtye);

		String  checkResultResult = new String(CheckResultBtye,"UTF-8");
		checkResultResult=checkResultResult.trim();
		
		checkStatus=true;
		return checkResultResult.equals(okStatus);
	}
	
	
	private String checkResult(String dllResult, int CodeID) {
		//dll返回的是错误代码
		if(dllResult.indexOf("_") < 0)
			return dllResult;
		
		//对结果进行校验
		String[] re = dllResult.split("_");
		String verify = re[0];
		String code = re[1];
		String localMd5 = null;
		try {
			localMd5 = md5(softID + dllVerifyKey + CodeID + code.toUpperCase()).toUpperCase();
			//System.out.println("local checkValue:"+localMd5+"code:"+code);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(localMd5.equals(verify))	//判断本地验证结果和服务器返回的验证结果是否一至，防止API被hook
			return code;
		else
			return "校验失败";
	}
	
	private byte[] toByteArray(InputStream imageFile) throws IOException {
		ByteArrayOutputStream buf = null;
		
		try {
			BufferedImage img = ImageIO.read(imageFile);
			buf = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", buf);
			return buf.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (buf != null) {
				buf.close();
			}
		}
	}
	
	//CRC32函数开始
	private String doChecksum(String fileName) throws IOException {
		CheckedInputStream cis = null;
        try {
         	// Computer CRC32 checksum
        	cis = new CheckedInputStream(UUAPI.class.getResourceAsStream(fileName), new CRC32());
            byte[] buf = new byte[256 * 1024];
            while(cis.read(buf) >= 0) {
            	
            }

            long checksum = cis.getChecksum().getValue();
            //System.out.println( Integer.toHexString(new Long(checksum).intValue()));
            return Integer.toHexString(new Long(checksum).intValue());

        } finally {
        	if (cis != null) {
        		cis.close();
        	}
        }

    }
	//CRC32函数结束
	
	//MD5校验函数开始
	private String getFileMD5(String inputFile) throws IOException {
		InputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			fileInputStream = UUAPI.class.getResourceAsStream(inputFile);
			digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
			byte[] buffer =new byte[256 * 1024];
			while (digestInputStream.read(buffer) > 0) {
				
			}
			messageDigest= digestInputStream.getMessageDigest();
			byte[] resultByteArray = messageDigest.digest();
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			digestInputStream.close();
			fileInputStream.close();
		}
	}
	private String md5(String s) throws IOException {
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			return byteArrayToHex(md);
		}catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	}
	private String byteArrayToHex(byte[] byteArray) {
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
		char[] resultCharArray =new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b& 0xf];
		}
		return new String(resultCharArray);
	}
	//MD5校验函数结束
}
