package com.konka.facerecognition.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 
 * 
 */
public class Person {
	private String id;
	private String name;
	private String tag;
	//person所包含的face_id集合
	private List<String> faceIdList;
	//person所属的group集合
	private List<Group> groupList;
	//识别准确度，只对部分case有效
	private double confidence;	
	
	public Person(){
		faceIdList = new ArrayList<String>();
		groupList = new ArrayList<Group>();
	}
	
	public Person(JSONObject json) throws JSONException{
		faceIdList = new ArrayList<String>();
		groupList = new ArrayList<Group>();
		
		if( !json.isNull("person_id") ){
			id = json.getString("person_id");
		}
		if( !json.isNull("person_name") ){
			name = json.getString("person_name");
		}
		if( !json.isNull("tag") ){
			tag = json.getString("tag");
		}
		JSONArray jsonArray;
		if( !json.isNull("face_id")){
			jsonArray = json.getJSONArray("face_id");
			//TODO
		}
		if( !json.isNull("group") ){
			jsonArray = json.getJSONArray("group");
			int size = jsonArray.length();
			Group group;
			for(int i=0; i<size; i++){
				group = new Group(jsonArray.getJSONObject(i));
				groupList.add(group);
			}
		}
		
		if( !json.isNull("confidence")){
			confidence = json.getDouble("confidence");
		}
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTag(){
		return tag;
	}
	
	public List<String> getFaceIdList(){
		return faceIdList;
	}
	
	public List<Group> getGroupList(){
		return groupList;
	}
	
	public Double getConfidence(){
		return confidence;
	}
	
	public String toString(){
		return "[person] = {id=" + id +"; name=" + name + "; tag="+ tag + "}"; 
	}
}
