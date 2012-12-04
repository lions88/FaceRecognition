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
 * @desc face类
 * 
 */
public class Face {
	//唯一id标识
	private String id;
	
	//脸的宽度(0~100之间的实数，表示检出的脸的宽度在图片中百分比)
	private double width;
	private double height;
	
	private PosAttr eyeLeft;
	private PosAttr eyeRight;
	
	private PosAttr center;
	
	private PosAttr mouthLeft;
	private PosAttr mouthRight;
	
	private Gender gender;		//性别
	private Age age;			//年龄
	private Race race;			//种族
	
	//与本face相似的person列表，只对部分case有效
	private List<Person> candidatePersonList;
	
	public Face(){
		
	}
	
	public Face(String jsonStr) throws JSONException{
		JSONObject json = new JSONObject(jsonStr);
		init(json);
	}
	
	public Face(JSONObject json) throws JSONException {
		init(json);
	}
	
	private void init(JSONObject json) throws JSONException{
		if( !json.isNull("face_id") )
			id = json.getString("face_id");
		
		//json数据格式不完全一致，分case解析
		if( !json.isNull("position") ){
			parseJson2(json);
			return;
		}
		
		if( !json.isNull("width") )
			width = json.getDouble("width");
		if( !json.isNull("heihgt") )
			height = json.getDouble("height");
		
		JSONObject json2;
		if( !json.isNull("eye_left") ){
			eyeLeft = new PosAttr();
			json2 = json.getJSONObject("eye_left");
			eyeLeft.xCoordinate = json2.getDouble("x");
			eyeLeft.yCoordinate = json2.getDouble("y");
		}
		if( !json.isNull("eye_right") ){
			eyeRight = new PosAttr();
			json2 = json.getJSONObject("eye_right");
			eyeRight.xCoordinate = json2.getDouble("x");
			eyeRight.yCoordinate = json2.getDouble("y");
		}
		if( !json.isNull("center") ){
			center = new PosAttr();
			json2 = json.getJSONObject("center");
			center.xCoordinate = json2.getDouble("x");
			center.yCoordinate = json2.getDouble("y");
		}
		if( !json.isNull("mouth_left") ){
			mouthLeft = new PosAttr();
			json2 = json.getJSONObject("mouth_left");
			mouthLeft.xCoordinate = json2.getDouble("x");
			mouthLeft.yCoordinate = json2.getDouble("y");
		}
		if( !json.isNull("mouth_right") ){
			mouthRight = new PosAttr();
			json2 = json.getJSONObject("mouth_right");
			mouthRight.xCoordinate = json2.getDouble("x");
			mouthRight.yCoordinate = json2.getDouble("y");
		}
		
		if( !json.isNull("attribute")){
			gender = new Gender();
			age = new Age();
			race = new Race();
			
			json2 = json.getJSONObject("attribute");
			
			JSONObject json3;
			json3 = json2.getJSONObject("gender");
			gender.confidence = json3.getDouble("confidence");
			gender.generType = Gender.GENDER.valueOf(json3.getString("value"));
			
			json3 = json2.getJSONObject("age");
			age.value = json3.getInt("value");
			age.range = json3.getInt("range");
			
			json3 = json2.getJSONObject("race");
			race.confidence = json3.getDouble("confidence");
			race.raceType = Race.RACE.valueOf(json3.getString("value"));
		}
	}
	
	public String getId(){
		return id;
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}
	
	public Gender getGender(){
		return gender;
	}
	
	public Age getAge(){
		return age;
	}
	
	public Race getRace(){
		return race;
	}
	
	//may be null
	public List<Person> getCandidatePersonList(){
		return candidatePersonList;
	}
	
	private void parseJson2(JSONObject json) throws JSONException{
		JSONObject jsonPos = json.getJSONObject("position");
		
		if( !json.isNull("face_id") )
			id = json.getString("face_id");
		
		JSONObject json2;
		if( !jsonPos.isNull("eye_left") ){
			eyeLeft = new PosAttr();
			json2 = jsonPos.getJSONObject("eye_left");
			eyeLeft.xCoordinate = json2.getDouble("x");
			eyeLeft.yCoordinate = json2.getDouble("y");
		}
		if( !jsonPos.isNull("eye_right") ){
			eyeRight = new PosAttr();
			json2 = jsonPos.getJSONObject("eye_right");
			eyeRight.xCoordinate = json2.getDouble("x");
			eyeRight.yCoordinate = json2.getDouble("y");
		}
		if( !jsonPos.isNull("center") ){
			center = new PosAttr();
			json2 = jsonPos.getJSONObject("center");
			center.xCoordinate = json2.getDouble("x");
			center.yCoordinate = json2.getDouble("y");
		}
		if( !jsonPos.isNull("mouth_left") ){
			mouthLeft = new PosAttr();
			json2 = jsonPos.getJSONObject("mouth_left");
			mouthLeft.xCoordinate = json2.getDouble("x");
			mouthLeft.yCoordinate = json2.getDouble("y");
		}
		if( !jsonPos.isNull("mouth_right") ){
			mouthRight = new PosAttr();
			json2 = jsonPos.getJSONObject("mouth_right");
			mouthRight.xCoordinate = json2.getDouble("x");
			mouthRight.yCoordinate = json2.getDouble("y");
		}
		
		if( !jsonPos.isNull("width") )
			width = jsonPos.getDouble("width");
		if( !jsonPos.isNull("height") )
			height = jsonPos.getDouble("height");
		
		if( !json.isNull("candidate") ){
			JSONArray jsonCandidateArray = json.getJSONArray("candidate");
			int len = jsonCandidateArray.length();
			candidatePersonList = new ArrayList<Person>();
			for(int i=0; i<len; i++){
				JSONObject jsonPerson = jsonCandidateArray.getJSONObject(i);
				Person person = new Person(jsonPerson);
				candidatePersonList.add(person);
			}
		}
	}
	
	public String toString(){
		return "[face]={id=" + id + "; gender=" + gender.generType.name() + "(" + gender.confidence+"); age="
				+ age.value + "(" + age.range + "); Race=" + race.raceType.name() + "(" + race.confidence + ")"; 
	}
}
