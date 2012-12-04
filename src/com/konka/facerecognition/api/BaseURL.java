package com.konka.facerecognition.api;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 存储一些url地址
 * 
 */
public class BaseURL {
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";
	public static final String BASE = "api.faceplusplus.com/";
	//识别
	public static final String RECOGNITION = "recognition/";
	//person
	public static final String PERSON = "person/";
	//group
	public static final String GROUP = "group/";
	//info查询
	public static final String INFO = "info/";
	
	//检测
	public static final String DETECT_URL = HTTP + BASE + "detection/detect";
	
	//compare two face
	public static final String COMPARE_URL = HTTP + BASE + RECOGNITION + "compare";
	
	//识别
	public static final String RECOGNIZE_URL = HTTP + BASE + RECOGNITION + "recognize";
	
	//搜索
	public static final String SEARCH_URL = HTTP + BASE + RECOGNITION + "search";
	
	//训练
	public static final String TRAIN_URL = HTTP + BASE + RECOGNITION + "train";
	
	//鉴别
	public static final String VERIFY_URL = HTTP + BASE + RECOGNITION + "verify";
	
	//add face
	public static final String ADD_FACE_URL = HTTP + BASE + PERSON + "add_face";
	
	//remove face
	public static final String REMOVE_FACE_URL = HTTP + BASE + PERSON + "remove_face";
	
	//create person
	public static final String CREATE_PERSON_URL = HTTP + BASE + PERSON + "create";
	
	//delete person
	public static final String DELETE_PERSON_URL = HTTP + BASE + PERSON + "delete";
	
	//get person information
	public static final String GET_PERSON_INFO_URL = HTTP + BASE + PERSON + "get_info";
	
	//set person information
	public static final String SET_PERSON_INFO_URL = HTTP + BASE + PERSON + "set_info";
	
	//add person
	public static final String ADD_PERSON_URL = HTTP + BASE + GROUP + "add_person";
	
	//remove person
	public static final String REMOVE_PERSON_URL = HTTP + BASE + GROUP + "remove_person";
	
	//create group
	public static final String CREATE_GROUP_URL = HTTP + BASE + GROUP + "create";
	
	//delete group
	public static final String DELETE_GROUP_URL = HTTP + BASE +GROUP + "delete";
	
	//get group information
	public static final String GET_GROUP_INFO_URL = HTTP + BASE + GROUP + "get_info";
	
	//set group information
	public static final String SET_GROUP_INFO_URL = HTTP + BASE + GROUP + "set_info";
	
	//get my application info
	public static final String GET_APP_INFO_URL = HTTP + BASE + INFO + "get_app";
	
	//get face information
	public static final String GET_FACE_INFO_URL = HTTP + BASE + INFO + "get_face";
	
	//get group list of my application
	public static final String GET_GROUP_LIST_URL = HTTP + BASE + INFO + "get_group_list";
	
	//get person list of my application
	public static final String GET_PERSON_LIST_URL = HTTP + BASE + INFO + "get_person_list";
	
	//get a image information
	public static final String GET_IMAGE_INFO_URL = HTTP + BASE + INFO + "get_image";
	
	//get quota information
	public static final String GET_QUOTA_INFO_URL = HTTP + BASE + INFO + "get_quota";
	
	//get session information
	public static final String GET_SESSION_INFO_URL = HTTP + BASE + INFO + "get_session";
	
}
