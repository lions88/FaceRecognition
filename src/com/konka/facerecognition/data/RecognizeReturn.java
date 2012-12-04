package com.konka.facerecognition.data;

import java.util.ArrayList;

import com.konka.facerecognition.api.Face;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 识别group中相似的person的返回结果
 * 
 */
public class RecognizeReturn {
	public String sessionId;
	public boolean hasUntrainedPerson;	//group中是否有未训练的person
	public ArrayList<Face> faceList;		//检测的图片的face数据
}
