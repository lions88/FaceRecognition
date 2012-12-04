package com.konka.facerecognition.api;

/**
 * 
 * @author liliang
 * @date 2012-11-28
 * @desc 种族识别 
 * 
 */
public class Race {
	
	public static enum RACE{
		Asian,
		White,
		Black,
	}
	
	//识别置信度（百分比）
	double confidence;
	
	//亚洲人or黑人or白人
	RACE raceType;
}
