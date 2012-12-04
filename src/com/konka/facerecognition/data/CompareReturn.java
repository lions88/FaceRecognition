package com.konka.facerecognition.data;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 人脸比较的返回结果类
 * 
 */
public class CompareReturn {
	public String sessionId;
	public double similarity;		//总体相似度（百分比）
	public double mouthSimilarity;	//嘴相似度
	public double eyeSimilarity;	//眼睛相似度
	public double noseSimilarity;	//鼻子相似度
	public double eyebrowSimilarity;//眉毛相似度
}
