package com.konka.facerecognition.data;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 鉴别是否是同一个人的结果返回
 */
public class VerifyReturn {
	public double confidence;		//准确度（百分比）
	public boolean isSamePerson;	//是否是同一个人
	public String sessionId;		
}
