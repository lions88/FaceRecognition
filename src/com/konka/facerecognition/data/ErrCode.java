package com.konka.facerecognition.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 错误码-信息提示
 */
public class ErrCode {
	public static Map<Integer, String> sErrCodeMap;
	public static final int INVALID_PARAM_CODE = 0;
	public static final int IO_EXCEPTION_CODE = -1;
	public static final int JSON_EXCEPTION_CODE = -2;
	
	static{
		sErrCodeMap = new HashMap<Integer, String>();
		
		sErrCodeMap.put(0, "参数不合法");
		sErrCodeMap.put(-1, "IO异常");
		sErrCodeMap.put(-2, "json解析异常");
		
		sErrCodeMap.put(1004, "参数有误");			//MISSING_ARGUMENTS
		sErrCodeMap.put(1005, "参数无效");			//INVALID_ARGUMENTS
		sErrCodeMap.put(1006, "描述过长");			//TAG_TOO_LONG
		
		sErrCodeMap.put(1301, "不支持的图片格式");		//IMAGE_ERROR_UNSUPPORTED_FORMAT
		sErrCodeMap.put(1302, "下载图片失败");		//IMAGE_ERROR_FAILED_TO_DOWNLOAD
		sErrCodeMap.put(1303, "图片太大");			//IMAGE_ERROR_FILE_TOO_LARGE
		
		sErrCodeMap.put(1401, "人名不合法");		//PERSON_BAD_NAME
		sErrCodeMap.put(1402, "此人已存在"); 		//PERSON_ALREADY_EXIST
		sErrCodeMap.put(1403, "识别出多张人脸");		//FACE_ASSIGN_TO_MULTIPLE_PERSONS	
		sErrCodeMap.put(1404, "此脸不属于此人"); 		//FACE_DOES_NOT_BELONG_TO_PERSON
		
		sErrCodeMap.put(1501, "分组名称不合法");		//GROUP_BAD_NAME
		sErrCodeMap.put(1502, "分组已经存在");		//GROUP_ALREADY_EXIST
		sErrCodeMap.put(1503, "已经添加过此人了"); 	//GROUP_ALREADY_INCLUDES_PERSON
	}
	
	/**
	 * 根据错误码返回对应的错误信息
	 * @param errCode
	 * @return
	 */
	public static String getErrMsg(int errCode){
		if(sErrCodeMap.containsKey(errCode)){
			return sErrCodeMap.get(errCode);
		}
		return "unknow error";
	}
	
	public static String getErrMsg(String errCodeStr){
		int errCode = Integer.parseInt(errCodeStr);
		if(sErrCodeMap.containsKey(errCode)){
			return sErrCodeMap.get(errCode);
		}
		return "unknow error";
	}
}
