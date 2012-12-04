package com.konka.facerecognition.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.konka.facerecognition.data.Configure;
import com.konka.facerecognition.data.ErrCode;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc open api 操作类。
 * 	
 * 	返回类型Map<String, String>. 
 * 	key
 * 		code:http状态码。
 * 		return：json String
 */
public class ApiHandle {
	//参数枚举
	enum Parameters {
		api_key,
		api_secret,
		url,
		mode,
		count,
		tag, 
		type,
		name,
		
		face_id,
		key_face_id,
		face_id1,
		face_id2,
		
		person_id,
		person_name,

		group_id,
		group_name,
		
		img_id,
		session_id,
	}
	
	//训练类型
	public static enum TRAIN_TYPE{
		all,
		search,		//只训练搜索模块
		recognize,	//只训练识别模块
	}
	
	private TreeMap<String, String> commonParams;
	private HashMap<String, String> invalidParamsMap;
	
	public ApiHandle(){
		commonParams = new TreeMap<String, String>();
		commonParams.put(Parameters.api_key.name(), Configure.getValue(Configure.API_KEY));
		commonParams.put(Parameters.api_secret.name(), Configure.getValue(Configure.API_SECRET));
	
		invalidParamsMap = new HashMap<String, String>();
		invalidParamsMap.put(HttpUtils.CODE, String.valueOf(ErrCode.INVALID_PARAM_CODE));
		invalidParamsMap.put(HttpUtils.RETURN, "invalid parameter");
	}
	
	/**
	 * 得到charset的编码格式字符串
	 * @param str 要转码的字符转
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String encode(String str, String charset){
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return URLEncoder.encode(str);
		}
	}
	//转UTF-8
	@SuppressWarnings("deprecation")
	public String encode(String str){
		return URLEncoder.encode(str);
	}
	
	/**
	 * 根据网络图片的url地址对图片进行识别
	 * @param imgUrl url of image
	 * @return http code and content
	 * @throws IOException
	 */
	public Map<String, String> detectByPicUrl(String imgUrl) throws IOException{
		if(imgUrl == null || imgUrl.equals(""))
			return invalidParamsMap;
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		paramMap.put(Parameters.url.name(), encode(imgUrl));
		//paramMap.put(Parameters.mode.name(), "oneface");
		return HttpUtils.doGet(BaseURL.DETECT_URL, paramMap);
	}
	
	/**
	 * 识别本地图片
	 * @param is 本地图片二进制流
	 * @return
	 * @throws IOException 
	 	
	public Map<String, String> detect(byte[] bytes) throws IOException{
		return HttpUtils.doPostImage(BaseURL.DETECT_URL, commonParams, bytes);
	}*/
	
	/**
	 * 识别本地图片
	 * @param is 本地图片路径
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> detect(String imgPath) throws IOException{
		return HttpUtils.doPostImage(BaseURL.DETECT_URL, commonParams, imgPath);
	}
	
	/**
	 * 计算两个Face的相似性以及五官相似度
	 * @throws IOException 
	 */
	public Map<String, String> compare(String faceId1, String faceId2) throws IOException{
		if(faceId1 == null || faceId1.equals("") ||
				faceId2 == null || faceId2.equals("")){
			return invalidParamsMap;
		}
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		paramMap.put(Parameters.face_id1.name(), faceId1);
		paramMap.put(Parameters.face_id2.name(), faceId2);
		
		return HttpUtils.doGet(BaseURL.COMPARE_URL, paramMap);
	}
	
	/**
	 * 从group id分组中找到与给出图片url匹配的person
	 * @param groupId
	 * @param imgUrl
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> recognizeByPicUrl(String groupId, String imgUrl) throws IOException{
		if(groupId == null || groupId.equals("") ||
				imgUrl == null || imgUrl.equals("")){
			return invalidParamsMap;
		}
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);
		paramMap.put(Parameters.url.name(), encode(imgUrl));
		
		return HttpUtils.doGet(BaseURL.RECOGNIZE_URL, paramMap);
	}
	//上传一张本地图片，在groupId中查询最相似的person
	public Map<String, String> recognize(String groupId, String imgPath) throws IOException{
		if(groupId == null || groupId.equals("") ){
			return invalidParamsMap;
		}
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);

		return HttpUtils.doPostImage(BaseURL.RECOGNIZE_URL, paramMap, imgPath);
	}
	
	/**
	 * 给定一个FaceId和一个GroupId，在该Group内搜索最相似的Face
	 * @throws IOException 
	 */
	public Map<String, String> search(String faceId, String groupId) throws IOException{
		if(faceId == null || groupId == null)
			return invalidParamsMap;
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.key_face_id.name(), faceId);
		paramMap.put(Parameters.group_id.name(), groupId);
		paramMap.put(Parameters.count.name(), "20");		//最多返回20个结果
		
		return HttpUtils.doGet(BaseURL.SEARCH_URL, paramMap);
	}
	
	/**
	 * 对一个或多个Group进行训练(针对Recognition 或者 Search应用)。请注意:
	 *	在一个Group内进行Recognize或者Search之前，必须先对该Group进行Train
	 *	当一个Group内的数据被修改后(例如增删Person, 增删Person相关的Face等)，为使这些修改生效，Group应当被重新Train
	 *	训练的结果可以通过/info/get_session 查询。当训练完成时，返回值中将包含{"success": true}
	 * @throws IOException 
	 */
	public Map<String, String> train(String groupId, TRAIN_TYPE eTrainType) throws IOException{
		if(groupId == null || groupId.equals(""))
			return invalidParamsMap;
		if(eTrainType == null)
			eTrainType = TRAIN_TYPE.all;
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);
		paramMap.put(Parameters.type.name(), eTrainType.name());
		
		return HttpUtils.doGet(BaseURL.TRAIN_URL, paramMap);
	}
	
	/**
	 * 给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。注意这个Person必须已经被训练过（即其所在的一个Group被训练过
	 * @param faceId
	 * @param personId
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> verify(String faceId, String personId) throws IOException{
		if(faceId == null || faceId.equals("") ||
				personId == null || personId.equals("")){
			return invalidParamsMap;
		}
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.face_id.name(), faceId);
		paramMap.put(Parameters.person_id.name(), personId);
		
		return HttpUtils.doGet(BaseURL.VERIFY_URL, paramMap);
	}
	
	//===================================person==================================//
	/**
	 * create a person
	 * @param name 
	 * @param faceIds: face id数组, may be null. 表示将这些face加入到该Person中
	 * @param tag		person描述 may be null, 长度不超过255
	 * @param groupIds 该Person被create之后就会被加入到这些组中。 may be null.
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> createPerson(String name, List<String> faceIds, 
			String tag, List<String> groupIds) throws IOException{
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		if(name != null){
			paramMap.put(Parameters.person_name.name(), encode(name));
		}
		int size = faceIds.size();
		if(faceIds != null && size > 0){
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<size; i++){
				sb.append(faceIds.get(i));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			paramMap.put(Parameters.face_id.name(), sb.toString());
		}
		if(tag != null){
			paramMap.put(Parameters.tag.name(), encode(tag));
		}
		
		if(groupIds != null && groupIds.size() > 0){
			StringBuffer sb2 = new StringBuffer();
			size = groupIds.size();
			for(int i=0; i<size; i++){
				sb2.append(groupIds.get(i));
				sb2.append(",");
			}
			sb2.deleteCharAt(sb2.length()-1);
			paramMap.put(Parameters.group_id.name(), sb2.toString());
		}
		return HttpUtils.doGet(BaseURL.CREATE_PERSON_URL, paramMap);
	}
	
	public Map<String, String> deletePerson(String personId) throws IOException{
		if(personId == null || personId.equals(""))
			return invalidParamsMap;
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.person_id.name(), personId);
		
		return HttpUtils.doGet(BaseURL.DELETE_PERSON_URL, paramMap);
	}
	
	public Map<String, String> getPersonInfo(String personId) throws IOException{
		if(personId == null || personId.equals(""))
			return invalidParamsMap;
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.person_id.name(), personId);
		
		return HttpUtils.doGet(BaseURL.GET_PERSON_INFO_URL, paramMap);
	}
	
	public Map<String, String> setPersonInfo(String personId, String name, String tag) 
			throws IOException{
		if(personId == null || personId.equals(""))
			return invalidParamsMap;
		if(name == null && tag == null)
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		paramMap.put(Parameters.person_id.name(), personId);
		if(name != null){
			paramMap.put(Parameters.person_name.name(), encode(name));
		}
		if(tag != null){
			paramMap.put(Parameters.tag.name(), encode(tag));
		}
		
		return HttpUtils.doGet(BaseURL.SET_PERSON_INFO_URL, paramMap);
	}
	
	public Map<String, String> addFace2Person(String personId, List<String> faceIds)
		throws IOException{
		if(personId == null || personId.equals("") ||
				faceIds == null || faceIds.size()<1){
			return invalidParamsMap;
		}
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		paramMap.put(Parameters.person_id.name(), personId);
		
		int size = faceIds.size();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<size; i++){
			sb.append(faceIds.get(i));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		paramMap.put(Parameters.face_id.name(), sb.toString());
		
		return HttpUtils.doGet(BaseURL.ADD_FACE_URL, paramMap);
	}
	
	public Map<String, String> removeFaceFromPerson(String personId, List<String> faceIds)
		throws IOException{
		if(personId == null || personId.equals("") ||
				faceIds == null || faceIds.size()<1){
			return invalidParamsMap;
		}
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		paramMap.put(Parameters.person_id.name(), personId);
		
		int size = faceIds.size();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<size; i++){
			sb.append(faceIds.get(i));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		paramMap.put(Parameters.face_id.name(), sb.toString());
		
		return HttpUtils.doGet(BaseURL.REMOVE_FACE_URL, paramMap);
	}
	
	//==================================end person==================================//
	
	//===================================group==================================//
	/**
	 * 创建分组
	 * @param groupName 组名字 (不能包含字符"@" ，且长度不得超过255)
	 * @param tag		组描述 may be null (长度不得超过255)
	 * @param personIds may be null, 如果不为空 将这些person加入到此group中
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> createGroup(String groupName, String tag, List<String> personIds) throws IOException{
		if(groupName == null || groupName.equals(""))
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams); 
		paramMap.put(Parameters.group_name.name(), encode(groupName));
		if(tag != null){
			paramMap.put(Parameters.tag.name(), encode(tag));
		}
		if(personIds != null && personIds.size() > 0){
			int size = personIds.size();
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<size; i++){
				sb.append(personIds.get(i));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			paramMap.put(Parameters.person_id.name(), sb.toString());
		}
		return HttpUtils.doGet(BaseURL.CREATE_GROUP_URL, paramMap);
	}
	//删除分组
	public Map<String, String> deleteGroup(List<String> groupIds) throws IOException{
		if(groupIds == null || groupIds.size() < 1)
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		StringBuffer sb = new StringBuffer();
		int size = groupIds.size();
		for(int i=0; i<size; i++){
			sb.append(groupIds.get(i));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		paramMap.put(Parameters.group_id.name(), sb.toString());
		
		return HttpUtils.doGet(BaseURL.DELETE_GROUP_URL, paramMap); 
	}
	/**
	 * 获取分组信息
	 * @param groupId 分组id (如果groupid="none",此时将返回所有未加入任何Group的Person)
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> getGroupInfo(String groupId) throws IOException{
		if(groupId==null || groupId.equals(""))
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);
		
		return HttpUtils.doGet(BaseURL.GET_GROUP_INFO_URL, paramMap);
	}
	
	/**
	 * set group info
	 * @param groupId	要修改的组id
	 * @param groupName	new group name	
	 * @param tag		new group tag	(groupName和tag不能同时为空)
	 * @return
	 * @throws IOException 
	 */
	public Map<String, String> setGroupInfo(String groupId, String groupName, String tag) throws IOException{
		if(groupId == null || groupId.equals("") )
			return invalidParamsMap;
		
		if(groupName == null && tag == null)
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);
		if(groupName != null)
			paramMap.put(Parameters.name.name(), encode(groupName));
		if(tag != null)
			paramMap.put(Parameters.tag.name(), encode(tag));
		
		return HttpUtils.doGet(BaseURL.SET_GROUP_INFO_URL, paramMap);
	}
	//add person to a group
	public Map<String, String> addPerson2Group(String groupId, List<String> personIds) throws IOException{
		if(groupId == null || personIds == null || personIds.size() < 1)
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);
		
		StringBuffer sb = new StringBuffer();
		int size = personIds.size();
		for(int i=0; i<size; i++){
			sb.append(personIds.get(i));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		paramMap.put(Parameters.person_id.name(), sb.toString());
		
		return HttpUtils.doGet(BaseURL.ADD_PERSON_URL, paramMap);
	}
	//remove person from a group
	public Map<String, String> removePersonFromGroup(String groupId, List<String> personIds) throws IOException{
		if(groupId == null || personIds == null || personIds.size() < 1)
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.group_id.name(), groupId);
		
		StringBuffer sb = new StringBuffer();
		int size = personIds.size();
		for(int i=0; i<size; i++){
			sb.append(personIds.get(i));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		paramMap.put(Parameters.person_id.name(), sb.toString());
		
		return HttpUtils.doGet(BaseURL.REMOVE_PERSON_URL, paramMap);
	}
	//================================end group==================================//
	
	//================================info 查询===================================//
	//给定一组Face，返回相应的信息(包括源图片, 相关的person等等)
	public Map<String, String> getFaceInfo(List<String> faceIds) throws IOException{
		if(faceIds == null || faceIds.size() < 1 )
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		int size = faceIds.size();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<size; i++){
			sb.append(faceIds.get(i));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		paramMap.put(Parameters.face_id.name(), sb.toString());
		
		return HttpUtils.doGet(BaseURL.GET_FACE_INFO_URL, paramMap);
	}
	
	//获取一张image的信息, 包括其中包含的face等信息
	public Map<String, String> getImageInfo(String imgId) throws IOException{
		if(imgId == null || imgId.equals(""))
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.img_id.name(), imgId);
		
		return HttpUtils.doGet(BaseURL.GET_IMAGE_INFO_URL, paramMap);
	}
	
	//获取session相关状态和结果
	//可能的status：INQUEUE(队列中), SUCC(成功), EXPIRED(session 超时) 和FAILED(失败)
	//当status是SUCC时，返回结果中还包含session对应的结果
	public Map<String, String> getSessionInfo(String sessionId) throws IOException{
		if(sessionId == null || sessionId.equals(""))
			return invalidParamsMap;
		
		TreeMap<String, String> paramMap = new TreeMap<String, String>(commonParams);
		paramMap.put(Parameters.session_id.name(), sessionId);
		
		return HttpUtils.doGet(BaseURL.GET_SESSION_INFO_URL, paramMap);
	}
	
	//get my application info
	public Map<String, String> getMyAppInfo() throws IOException{
		return HttpUtils.doGet(BaseURL.GET_APP_INFO_URL, commonParams);
	}
	
	//返回该App中的所有group
	public Map<String, String> getAllGroupListInfo() throws IOException{
		return HttpUtils.doGet(BaseURL.GET_GROUP_LIST_URL, commonParams);
	}
	
	//返回该App中的所有Person
	public Map<String, String> getAllPersonListInfo() throws IOException{
		return HttpUtils.doGet(BaseURL.GET_PERSON_LIST_URL, commonParams);
	}
	
	//返回剩余Quota
	public Map<String, String> getQuotaInfo() throws IOException{
		return HttpUtils.doGet(BaseURL.GET_QUOTA_INFO_URL, commonParams);
	}
	
	//================================end info===================================//
}
