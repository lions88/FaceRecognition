package com.konka.facerecognition.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.konka.facerecognition.api.ApiHandle;
import com.konka.facerecognition.api.Group;
import com.konka.facerecognition.api.HttpUtils;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 解析group模块返回数据 
 * 		this should only be called on class of FacePlus
 */
public class SubGroupFP {
	private ApiHandle mApiHanlde;
	
	public SubGroupFP(ApiHandle apiHandle){
		mApiHanlde = apiHandle;
	}
	
	/**
	 * create a group
	 * @param groupName 不能为空
	 * @param tag	may be null
	 * @param personIds may be null. 如果不空则将这些person加入到新创建的group中
	 * @return 新创建的group
	 */
	public Result createGroup(String groupName, String tag, List<String> personIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.createGroup(groupName, tag, personIds);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Group group = new Group(json);
			
			result.type = Result.TYPE.OK;
			result.data = group;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 
	 * @param groupIds 要删除的一些groupId集合
	 * @return success or not
	 */
	public Result deleteGroup(List<String> groupIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.deleteGroup(groupIds);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Boolean isSucc = json.getBoolean("success");
			
			result.type = Result.TYPE.OK;
			result.data = isSucc;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * get group info
	 * @param groupId
	 * @return group include its person list
	 */
	public Result getGroupInfo(String groupId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getGroupInfo(groupId);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Group group = new Group(json);
			
			result.type = Result.TYPE.OK;
			result.data = group;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * set group info
	 * @param groupId
	 * @param groupName. may be null
	 * @param tag. may by null，但groupName和tag不能同时为空
	 * @return group
	 */
	public Result setGroupInfo(String groupId, String groupName, String tag){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.setGroupInfo(groupId, groupName, tag);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Group group = new Group(json);
			
			result.type = Result.TYPE.OK;
			result.data = group;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * add person to group
	 * @param groupId
	 * @param personIds
	 * @return success or not
	 */
	public Result addPerson2Group(String groupId, List<String> personIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.addPerson2Group(groupId, personIds);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Boolean isSucc = json.getBoolean("success");
			
			result.type = Result.TYPE.OK;
			result.data = isSucc;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * remove some persons form group
	 * @param groupId
	 * @param personIds
	 * @return success or not
	 */
	public Result removePersonFromGroup(String groupId, List<String> personIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.removePersonFromGroup(groupId, personIds);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Boolean isSucc = json.getBoolean("success");
			
			result.type = Result.TYPE.OK;
			result.data = isSucc;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
}
