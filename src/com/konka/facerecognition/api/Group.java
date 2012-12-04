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
 * @desc person所属的group分组类
 * 
 */
public class Group {
	private String id;
	private String name;
	private String tag;
	//改group所包含的person列表
	private List<Person> personList;
	
	public Group(){
		personList = new ArrayList<Person>();
	}
	public Group(JSONObject json) throws JSONException{
		personList = new ArrayList<Person>();
		
		if( !json.isNull("group_id") ){
			id = json.getString("group_id");
		}
		if( !json.isNull("group_name") ){
			name = json.getString("group_name");
		}
		if( !json.isNull("tag") ){
			tag = json.getString("tag");
		}
		if( !json.isNull("person") ){
			JSONArray jsonArray = json.getJSONArray("person");
			int size = jsonArray.length();
			Person person;
			for(int i=0; i<size; i++){
				person = new Person(jsonArray.getJSONObject(i));
				personList.add(person);
			}
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
	
	public List<Person> getPersonList(){
		return personList;
	}
	
	public String toString(){
		return "[group] = {id=" + id +"; name=" + name + "; tag="+ tag + "}"; 
	}
}
