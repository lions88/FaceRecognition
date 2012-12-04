package com.konka.facerecognition.data;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc FacePlus结果返回类
 * 
 */
public class Result {
	public static enum TYPE{
		OK,
		FAILED,
	}
	
	public TYPE type;
	
	//if(reType==FAILED) reData instanceof String(error message)
	//else reData instanceof DetectReturn, CompareReturn, RecognizeReturn and so on
	public Object data;
}
