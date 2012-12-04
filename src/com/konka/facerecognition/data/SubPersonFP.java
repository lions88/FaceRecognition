package com.konka.facerecognition.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.konka.facerecognition.api.ApiHandle;
import com.konka.facerecognition.api.HttpUtils;
import com.konka.facerecognition.api.Person;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 解析person模块返回数据 
 * 		this should only be called on class of FacePlus
 */
public class SubPersonFP {
	private ApiHandle mApiHanlde;
	
	public SubPersonFP(ApiHandle apiHandle){
		mApiHanlde = apiHandle;
	}
	
	/**
	 * 创建一个person
	 * @param name	可以为null
	 * @param faceIds	可以为null，如果不为null则将这些faceId加入到所创建的person中
	 * @param tag	可以为null。person的描述
	 * @param groupIds 可以为null。如果不为null则将所创建的person添加到这些group分组中
	 * @return 如果成功则返回person
	 */
	public Result createPerson(String name, List<String> faceIds, 
			String tag, List<String> groupIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.createPerson(name, faceIds, tag, groupIds);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//解析json
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Person person = new Person(json);
			
			result.type = Result.TYPE.OK;
			result.data = person;
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 删除一个person
	 * @param personId
	 * @return
	 */
	public Result deletePerson(String personId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.deletePerson(personId);
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
	 * 获取person信息
	 * @param personId
	 * @return 正常返回person类型，include person的face集合，以及person所属的group集合
	 */
	public Result getPersonInfo(String personId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getPersonInfo(personId);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Person person = new Person(json);
			
			result.type = Result.TYPE.OK;
			result.data = person;
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 设置person信息
	 * @param personId
	 * @param name	可以为null
	 * @param tag 可以为null，但name和tag不能同时为空
	 * @return	person
	 */
	public Result setPersonInfo(String personId, String name, String tag){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.setPersonInfo(personId, name, tag);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		try {
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			Person person = new Person(json);
			
			result.type = Result.TYPE.OK;
			result.data = person;
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 
	 * @param personId face被加入的personId 
	 * @param faceId  要加入的faceId
	 * @return 只返回是否成功的标志
	 */
	public Result addFace2Person(String personId, List<String> faceIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.addFace2Person(personId, faceIds);
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
	 * 删除Person中的一个或多个Face
	 * @param personId
	 * @param faceId
	 * @return 正常只返回是否成功的标识
	 */
	public Result removeFaceFromPerson(String personId, List<String> faceIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.removeFaceFromPerson(personId, faceIds);
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
