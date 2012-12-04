package com.konka.facerecognition.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konka.facerecognition.api.ApiHandle;
import com.konka.facerecognition.api.Face;
import com.konka.facerecognition.api.Group;
import com.konka.facerecognition.api.HttpUtils;
import com.konka.facerecognition.api.Image;
import com.konka.facerecognition.api.Person;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 解析info模块返回数据 
 * 		this should only be called on class of FacePlus
 */
public class SubInfoFP {
	//一些info模块结果返回类
	public static class FaceInfoReturn{
		public Face face;
		public Image image;
		public Person person;
	}
	
	public static class ImageInfoReturn{
		public Image image;
		public ArrayList<Face> faceList;
	}
	
	public static class SessionInfoReturn{
		//session状态枚举
		public static enum SESSION_STATUS{
			INQUEUE,	//队列中
			SUCC,		//成功
			EXPIRED,	//超时
			FAILED,		//失败
		}
		
		public SESSION_STATUS status;
		public String sessionId;
		//image和faceList只在status==SUCC时返回
		public Image image;
		public ArrayList<Face> faceList;
	}
	
	public static class AppInfoReturn{
		public String description;
		public String name;
	}
	
	public static class QuotaInfoReturn{
		public int quotaAll;
		public int quotaSearch;
	}
	
	private ApiHandle mApiHanlde;
	
	public SubInfoFP(ApiHandle apiHandle){
		mApiHanlde = apiHandle;
	}
	
	/**
	 * get some face info
	 * @param faceIds
	 * @return arrayList<FaceInfoReturn>
	 */
	public Result getFaceInfo(List<String> faceIds){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getFaceInfo(faceIds);
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
			
			JSONArray jsonArray = json.getJSONArray("face_info");
			int len = jsonArray.length();
			ArrayList<FaceInfoReturn> dataList = new ArrayList<FaceInfoReturn>();
			for(int i=0; i<len; i++){
				FaceInfoReturn subData = new FaceInfoReturn();
				JSONObject jsonTmp = jsonArray.getJSONObject(i);
				subData.face = new Face(jsonTmp);
				Image img = new Image();
				img.id = jsonTmp.getString("img_id");
				img.url = jsonTmp.getString("url");
				subData.image = img;
				subData.person = new Person(jsonTmp);
				
				dataList.add(subData);
			}
			
			result.type = Result.TYPE.OK;
			result.data = dataList;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	//获取一张image的信息, 包括其中包含的face等信息
	public Result getImageInfo(String imgId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getImageInfo(imgId);
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
			
			ImageInfoReturn data = new ImageInfoReturn();
			
			Image image = new Image();
			image.url = json.getString("url");
			image.id = json.getString("img_id");
			
			ArrayList<Face> faceList = new ArrayList<Face>();
			JSONArray jsonArray = json.getJSONArray("face");
			int len = jsonArray.length();
			for(int i=0; i<len; i++){
				Face face = new Face(jsonArray.getJSONObject(i));
				faceList.add(face);
			}
			
			data.image = image;
			data.faceList = faceList;
			
			result.type = Result.TYPE.OK;
			result.data = data;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	public Result getSessionInfo(String sessionId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getSessionInfo(sessionId);
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
			
			SessionInfoReturn data = new SessionInfoReturn();
			data.status = SessionInfoReturn.SESSION_STATUS.valueOf(json.getString("status"));
			data.sessionId = json.getString("session_id");
			
			if( !json.isNull("result") ){
				JSONObject jsonResult = json.getJSONObject("result");
				data.image = new Image(jsonResult);
				data.faceList = new ArrayList<Face>();
				JSONArray jsonFaceArray = jsonResult.getJSONArray("face");
				int len = jsonFaceArray.length();
				for(int i=0; i<len; i++){
					Face face = new Face(jsonFaceArray.getJSONObject(i));
					data.faceList.add(face);
				}
			}
			
			result.type = Result.TYPE.OK;
			result.data = data;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	public Result getMyAppInfo(){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getMyAppInfo();
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
			
			AppInfoReturn data = new AppInfoReturn();
			data.description = json.getString("description");
			data.name = json.getString("name");
			
			result.type = Result.TYPE.OK;
			result.data = data;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 返回该App中的所有group
	 * @return 正常返回groupList
	 */
	public Result getAllGroupListInfo(){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getAllGroupListInfo();
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
			
			ArrayList<Group> groupList = new ArrayList<Group>();
			JSONArray jsonArray = json.getJSONArray("group");
			int len = jsonArray.length();
			for(int i=0; i<len; i++){
				Group group = new Group(jsonArray.getJSONObject(i));
				groupList.add(group);
			}
			
			result.type = Result.TYPE.OK;
			result.data = groupList;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 返回该App中的所有Person
	 * @return 正常返回personList
	 */
	public Result getAllPersonListInfo(){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getAllPersonListInfo();
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
			
			ArrayList<Person> personList = new ArrayList<Person>();
			JSONArray jsonArray = json.getJSONArray("person");
			int len = jsonArray.length();
			for(int i=0; i<len; i++){
				Person person = new Person(jsonArray.getJSONObject(i));
				personList.add(person);
			}
			
			result.type = Result.TYPE.OK;
			result.data = personList;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	//返回剩余Quota
	public Result getQuotaInfo(){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHanlde.getQuotaInfo();
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
			
			QuotaInfoReturn data = new QuotaInfoReturn();
			data.quotaAll = json.getInt("QUOTA_ALL");
			data.quotaSearch = json.getInt("QUOTA_SEARCH");
			
			result.type = Result.TYPE.OK;
			result.data = data;
			//正常时直接返回Boolean类型
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
}
