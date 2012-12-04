package com.konka.facerecognition.api;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 性别属性
 * 
 */
public class Gender {
	public static enum GENDER{
		Male,
		Female,
	}
	
	//识别置信度
	double confidence;
	
	//男or女
	GENDER generType;
}	
