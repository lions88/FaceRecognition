package com.konka.facerecognition.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 
 * 
 */
public class Image {
	public String id;
	public int width;
	public int height;
	public String url;
	
	public Image(){
		
	}
	
	public Image(JSONObject json) throws JSONException{
		if( !json.isNull("url") ){
			url = json.getString("url");
		}
		if( !json.isNull("img_id") )
			id = json.getString("img_id");
		
		if( !json.isNull("img_width") )
			width = json.getInt("img_width");
		
		if( !json.isNull("img_height") )
			height = json.getInt("img_height");
	}
	
	public String toString(){
		return "[img]={" + id + ", " + width + ", " + height + ", " + url + "}";
	}
}
