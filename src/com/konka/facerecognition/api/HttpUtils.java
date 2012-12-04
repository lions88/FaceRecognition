package com.konka.facerecognition.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.konka.facerecognition.Debug;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc http相关辅助类
 * 
 */
public class HttpUtils {
	private static final String TAG = "HttpUtils";
	public static final int TIMEOUT = 15000;
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	
	public static final String CODE = "code";
	public static final String RETURN = "return";

	public static final String CODE_OK_STR = "200";
	public static final int CODE_OK = 200;
	
	//构造参数串
	public static String formatParams(Map<String, String> paramMap) {
		StringBuffer params = new StringBuffer();
		if (paramMap != null) {
			for (Iterator<String> iter = paramMap.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String value = paramMap.get(name);
				params.append(name);
				params.append('=');
				params.append(value);
				if (iter.hasNext())
					params.append('&');
			}
		}
		return params.toString();
	}
	
	//http get 请求
	public static Map<String, String> doGet(String baseUrl, TreeMap<String, String> paramMap) 
			throws IOException{
		String urlStr = baseUrl + "?" + formatParams(paramMap);
		Debug.debug(TAG, "url=" + urlStr);
		URL url = new URL(urlStr);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setConnectTimeout(TIMEOUT);
		con.setRequestMethod(METHOD_GET);
		con.setInstanceFollowRedirects(false);	//true
		/*if (con.getResponseCode() == 302) {
			//deal with redirect 302
			return null;
		}*/
		Map<String, String> resultMap = new HashMap<String, String>();
		InputStream is;
		try {
			is = con.getInputStream();
		} catch (IOException e) {
			is = con.getErrorStream();
		}
		
		int statusCode = con.getResponseCode();
		Debug.debug(TAG, "[http]code="+statusCode);
		resultMap.put(CODE, String.valueOf(statusCode));
		String content = getContent(is);
		resultMap.put(RETURN, content);
		Debug.debug(TAG, "[http]content=" + content);
		
		con.disconnect();
		
		return resultMap;
	}

	/**
	 * http post 上传一张照片 	采用apache HttpClient
	 * @param baseUrl	detect url
	 * @param paramMap  include api_key and api_secret
	 * @param imgPath	upload image's path
	 * @return			json string
	 * @throws IOException
	 */
	public static Map<String, String> doPostImage(String baseUrl, 
			TreeMap<String, String> paramMap, String imgPath) throws IOException{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(baseUrl);
		File file = new File(imgPath);

		 //设置通信协议版本
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		
		MultipartEntity mpEntity = new MultipartEntity(); //文件传输
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("img", cbFile);
		
		for(Iterator<String> it=paramMap.keySet().iterator(); it.hasNext();){
			String name = it.next();
			StringBody sbValue = new StringBody(paramMap.get(name));
			mpEntity.addPart(name, sbValue);
		}
		
		httpPost.setEntity(mpEntity);
		System.out.println("executing request " + httpPost.getRequestLine());

		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity resEntity = response.getEntity();
		System.out.println(response.getStatusLine());//通信Ok

		Map<String, String> resultMap = new HashMap<String, String>();
		if (resEntity != null) {
			//("post img return : " + EntityUtils.toString(resEntity,"utf-8"));
			int statusCode = response.getStatusLine().getStatusCode();
			resultMap.put(CODE, String.valueOf(statusCode));
			String content = EntityUtils.toString(resEntity,"utf-8");
			resultMap.put(RETURN, content);
			Debug.debug(TAG, "[http]code=" + statusCode);
			Debug.debug(TAG, "[http]return=" + content);
	        //resEntity.consumeContent();
		}
		httpClient.getConnectionManager().shutdown();
		
		return resultMap;
	}
	
	public static String getContent(InputStream is) throws UnsupportedEncodingException {
		return new String(getContentBytes(is), "UTF-8");
	}

	public static byte[] getContentBytes(InputStream is) throws UnsupportedEncodingException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int len = 0;
		try {
			while((len = is.read(buf)) != -1) {
				os.write(buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toByteArray();
	}
	
	/**
	 * httpURLConnection方式上传一张图片（二进制流）
	 * 		此函数由于http请求头缺少WWW-Authenticate会抛出异常“Received authentication challenge is null”
	 * @param baseUrl
	 * @param paramMap
	 * @param content
	 * @return
	 * @throws IOException 
	 */
	public static Map<String, String> doPostImage(String baseUrl, 
			TreeMap<String, String> paramMap, byte[] content) throws IOException{
		URL url = new URL(baseUrl);
		
        String BOUNDARY ="--------------et567z";//数据分隔线
        String MULTIPART_FORM_DATA ="Multipart/form-data";  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
        conn.setDoOutput(true);//允许输出
        conn.setUseCaches(false);//不使用Cache
        conn.setRequestMethod(METHOD_POST);
        conn.setConnectTimeout(TIMEOUT);
		conn.setReadTimeout(0);
        conn.setRequestProperty("Connection","Keep-Alive");
        conn.setRequestProperty("Charset","UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA +";boundary="+ BOUNDARY);
		//写入参数
        BOUNDARY += "--";
		StringBuffer params = new StringBuffer();
		for(Iterator<String> it=paramMap.keySet().iterator(); it.hasNext();){
			String name = it.next();
			String value = paramMap.get(name);
			params.append(BOUNDARY + "\r\n");
            params.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
            params.append(value);
            params.append("\r\n");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"img\";filename=\"temp.jpg\"\r\n");
        sb.append("Content-Type: image/jpg\r\n\r\n");
        
        byte[] fileDiv = sb.toString().getBytes("UTF-8");
        byte[] endData = ("\r\n" + BOUNDARY + "--\r\n").getBytes("UTF-8");
        byte[] ps = params.toString().getBytes("UTF-8");
        
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        os.write(ps);
        os.write(fileDiv);
        os.write(content, 0, content.length);
        os.write(endData);

        os.flush();
        os.close();
        System.out.println(sb.toString());
        
		Map<String, String> resultMap = new HashMap<String, String>();
		InputStream is;
		try {
			is = conn.getInputStream();
		} catch (IOException e) {
			is = conn.getErrorStream();
		}
		
		int statusCode = conn.getResponseCode();
		Debug.debug(TAG, "[http]code="+statusCode);
		resultMap.put(CODE, String.valueOf(statusCode));
		String content2 = getContent(is);
		resultMap.put(RETURN, content2);
		Debug.debug(TAG, "[http]content=" + content2);
		conn.disconnect();
		
		return resultMap;
	}
}
