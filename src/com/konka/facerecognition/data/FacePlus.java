package com.konka.facerecognition.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konka.facerecognition.Debug;
import com.konka.facerecognition.api.ApiHandle;
import com.konka.facerecognition.api.Face;
import com.konka.facerecognition.api.HttpUtils;
import com.konka.facerecognition.api.Image;
import com.konka.facerecognition.api.ApiHandle.TRAIN_TYPE;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 对接api/ApiHandle，对http获取的String数据做json解析；如果异常返回对应的错误信息
 * 
 */
public class FacePlus {
	private static final String TAG = "FacePlus";
	
	private static FacePlus sInstance; 
	
	public static FacePlus getInstance(){
		if(sInstance == null)
			sInstance = new FacePlus();
		return sInstance;
	}
	
	private ApiHandle mApiHandle;
	private SubPersonFP mSubPerson;
	private SubGroupFP mSubGroup;
	private SubInfoFP mSubInfo;
	
	private FacePlus(){
		mApiHandle = new ApiHandle();
		mSubPerson = new SubPersonFP(mApiHandle);
		mSubGroup = new SubGroupFP(mApiHandle);
		mSubInfo = new SubInfoFP(mApiHandle);
	}
	
	//上传一张图片，得到检测结果
	public Result detect(String imgPath){
		Debug.debug(TAG, "detect()");
		try {
			Map<String, String> retMap = mApiHandle.detect(imgPath);
			return doWithDetectResult(retMap);
		} catch (IOException e1) {
			e1.printStackTrace();
			Result result = new Result();
			result.type = Result.TYPE.FAILED;
			result.data = ErrCode.getErrMsg(-1);
			return result;
		}
	}
	
	//通过网络图片的url地址返回检测结果
	public Result detectByPicUrl(String imgUrl){
		Debug.debug(TAG, "detectByPicUrl()");
		try {
			Map<String, String> retMap = mApiHandle.detectByPicUrl(imgUrl);
			return doWithDetectResult(retMap);
		} catch (IOException e1) {
			e1.printStackTrace();
			Result result = new Result();
			result.type = Result.TYPE.FAILED;
			result.data = ErrCode.getErrMsg(-1);
			return result;
		}
	}
	
	private Result doWithDetectResult(Map<String, String> retMap){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		String code = retMap.get(HttpUtils.CODE);
		//解析json
		JSONObject json;
		try {
			json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			//http状态码返回200
			DetectReturn data = new DetectReturn();
			if( !json.isNull("sesson_id") )
				data.sessionId = json.getString("session_id");
			
			if( !json.isNull("img_id") && !json.isNull("url") &&
					!json.isNull("img_width") && !json.isNull("img_height") ){
				Image image = new Image();
				image.id = json.getString("img_id");
				image.url = json.getString("url");
				image.width = json.getInt("img_width");
				image.height = json.getInt("img_height");
				data.image = image;
			}
			
			ArrayList<Face> faceList = new ArrayList<Face>();
			JSONArray jsonFaceArray = json.getJSONArray("face");
			int len = jsonFaceArray.length();
			for(int i=0; i<len; i++){
				Face face = new Face(jsonFaceArray.getJSONObject(i));
				faceList.add(face);
			}
			data.faceList = faceList;
			//正常返回
			result.type = Result.TYPE.OK;
			result.data = data;
			return result;
			
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 计算两个Face的相似性以及五官相似度
	 * @param faceId1
	 * @param faceId2
	 * @return
	 */
	public Result compare(String faceId1, String faceId2){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHandle.compare(faceId1, faceId2);
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
			
			CompareReturn data = new CompareReturn();
			if(	!json.isNull("session_id") )
				data.sessionId = json.getString("session_id");
			data.similarity = json.getDouble("similarity");
			JSONObject json2 = json.getJSONObject("component_similarity");
			data.mouthSimilarity = json2.getDouble("mouth");
			data.eyeSimilarity = json2.getDouble("eye");
			data.noseSimilarity = json2.getDouble("nose");
			data.eyebrowSimilarity = json2.getDouble("eyebrow");
			
			result.type = Result.TYPE.OK;
			result.data = data;
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 上传一张本地图片，在groupId中查询最相似的person
	 * @param groupId 被查询的分组
	 * @param imgPath 要查询的face图片	
	 * @return
	 */
	public Result recognize(String groupId, String imgPath){
		Map<String, String> retMap;
		try {
			retMap = mApiHandle.recognize(groupId, imgPath);
			return doWithRecognizeResult(retMap);
		} catch (IOException e) {
			Result result = new Result();
			result.type = Result.TYPE.FAILED;
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 根据网络图片url地址，在groupId中查询最相似的person
	 * @param groupId 被查询的分组
	 * @param imgPath 要查询的face图片	
	 * @return
	 */
	public Result recognizedByPicUrl(String groupId, String imgUrl){
		Map<String, String> retMap;
		try {
			retMap = mApiHandle.recognizeByPicUrl(groupId, imgUrl);
			return doWithRecognizeResult(retMap);
		} catch (IOException e) {
			e.printStackTrace();
			Result result = new Result();
			result.type = Result.TYPE.FAILED;
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
	}
	
	private Result doWithRecognizeResult(Map<String, String> retMap){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		String code = retMap.get(HttpUtils.CODE);
		try{
			JSONObject json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			RecognizeReturn data = new RecognizeReturn();
			if(	!json.isNull("session_id") )
				data.sessionId = json.getString("session_id");
			if( !json.isNull("has_untrained_person") )
				data.hasUntrainedPerson = json.getBoolean("has_untrained_person");
			
			JSONArray jsonFaceArray = json.getJSONArray("face");
			int len = jsonFaceArray.length();
			ArrayList<Face> faceList = new ArrayList<Face>();
			for(int i=0; i<len; i++){
				Face face = new Face(jsonFaceArray.getJSONObject(i));
				faceList.add(face);
			}
			data.faceList = faceList;
			
			result.type = Result.TYPE.OK;
			result.data = data;
			return result;
			
		}catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 给定一个FaceId和一个GroupId，在该Group内搜索最相似的Face
	 * @param faceId 要检索的faceId
	 * @param groupId 被查询的groupId
	 * @return
	 */
	public Result search(String faceId, String groupId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHandle.search(faceId, groupId);
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
			
			SearchReturn data = new SearchReturn();
			if( !json.isNull("has_untrained_face") )
				data.hasUntraindFace = json.getBoolean("has_untrained_face");
			if( !json.isNull("session_id") )
				data.sessionId = json.getString("session_id");
			
			JSONArray jsonArray = json.getJSONArray("candidate");
			int len = jsonArray.length();
			JSONObject json2;
			data.candidateFaceList = new ArrayList<SearchReturn.CandidateFace>();
			for(int i=0; i<len; i++){
				json2 = jsonArray.getJSONObject(i);
				String cFaceId = json2.getString("face_id");
				double cSimilarity = json2.getDouble("similarity");
				SearchReturn.CandidateFace candidateFace = data.new CandidateFace(cFaceId, cSimilarity);
				data.candidateFaceList.add(candidateFace);
			}
			
			result.type = Result.TYPE.OK;
			result.data = data;
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 对一个或多个Group进行训练(针对Recognition 或者 Search应用)
	 * @param groupId
	 * @param eTrainType
	 * @return Train所花费的时间较长, 因此该调用是异步的。仅返回session_id
	 *			训练的结果可以通过/info/get_session 查询。当训练完成时，返回值中将包含{"success": true}
	 */
	public Result train(String groupId, TRAIN_TYPE eTrainType){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHandle.train(groupId, eTrainType);
		} catch (IOException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.IO_EXCEPTION_CODE);
			return result;
		}
		String code = retMap.get(HttpUtils.CODE);
		//解析json
		JSONObject json;
		try {
			json = new JSONObject(retMap.get(HttpUtils.RETURN));
			if( !code.equals(HttpUtils.CODE_OK_STR) ){
				//http请求异常
				result.data = ErrCode.getErrMsg(json.getInt("error_code"));
				return result;
			}
			
			String sessionId = null;
			if( !json.isNull("session_id")){
				 sessionId = json.getString("session_id");
			}
			result.type = Result.TYPE.OK;
			//结果正常时只返回String类型的sessionId
			result.data = sessionId;
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	/**
	 * 给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。注意这个Person必须已经被训练过（即其所在的一个Group被训练过
	 * @param faceId 待verify的face_id
	 * @param personId	对应的Person
	 * @return
	 */
	public Result verify(String faceId, String personId){
		Result result = new Result();
		result.type = Result.TYPE.FAILED;
		Map<String, String> retMap;
		try {
			retMap = mApiHandle.verify(faceId, personId);
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
			
			VerifyReturn data = new VerifyReturn();
			if( !json.isNull("session_id") )
				data.sessionId = json.getString("session_id");
			data.isSamePerson = json.getBoolean("is_same_person");
			data.confidence = json.getDouble("confidence");
			
			result.type = Result.TYPE.OK;
			result.data = data;
			
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			result.data = ErrCode.getErrMsg(ErrCode.JSON_EXCEPTION_CODE);
			return result;
		}
	}
	
	//===================Person 模块==============================//
	public Result createPerson(String name, List<String> faceIds, 
			String tag, List<String> groupIds){
		return mSubPerson.createPerson(name, faceIds, tag, groupIds);
	}
	
	public Result deletePerson(String personId){
		return mSubPerson.deletePerson(personId);
	}
	
	public Result getPersonInfo(String personId){
		return mSubPerson.getPersonInfo(personId);
	}
	
	public Result setPersonInfo(String personId, String name, String tag){
		return mSubPerson.setPersonInfo(personId, name, tag);
	}
	
	public Result addFace2Person(String personId, List<String> faceIds){
		return mSubPerson.addFace2Person(personId, faceIds);
	}
	
	public Result removeFaceFromPerson(String personId, List<String> faceIds){
		return mSubPerson.removeFaceFromPerson(personId, faceIds);
	}
	
	//=======================group 模块==========================//
	public Result createGroup(String groupName, String tag, List<String> personIds){
		return mSubGroup.createGroup(groupName, tag, personIds);
	}
	
	public Result deleteGroup(List<String> groupIds){
		return mSubGroup.deleteGroup(groupIds);
	}
	
	public Result getGroupInfo(String groupId){
		return mSubGroup.getGroupInfo(groupId);
	}
	
	public Result setGroupInfo(String groupId, String groupName, String tag){
		return mSubGroup.setGroupInfo(groupId, groupName, tag);
	}
	
	public Result addPerson2Group(String groupId, List<String> personIds){
		return mSubGroup.addPerson2Group(groupId, personIds);
	}
	
	public Result removePersonFromGroup(String groupId, List<String> personIds){
		return mSubGroup.removePersonFromGroup(groupId, personIds);
	}
	
	//=======================info 模块============================//
	public Result getFaceInfo(List<String> faceIds){
		return mSubInfo.getFaceInfo(faceIds);
	}
	
	public Result getImageInfo(String imgId){
		return mSubInfo.getImageInfo(imgId);
	}
	
	public Result getSessionInfo(String sessionId){
		return mSubInfo.getSessionInfo(sessionId);
	}
	
	public Result getMyAppInfo(){
		return mSubInfo.getMyAppInfo();
	}
	
	public Result getAllGroupListInfo(){
		return mSubInfo.getAllGroupListInfo();
	}
	
	public Result getAllPersonListInfo(){
		return mSubInfo.getAllPersonListInfo();
	}
	
	public Result getQuotaInfo(){
		return mSubInfo.getQuotaInfo();
	}
}
