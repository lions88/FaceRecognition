package com.konka.facerecognition.data;

import java.util.ArrayList;

/**
 * 
 * @author liliang
 * @date 2012-11-29
 * @desc 搜索结果返回
 * 
 */
public class SearchReturn {
	public boolean hasUntraindFace;
	public String sessionId;
	public ArrayList<CandidateFace> candidateFaceList;
	
	public class CandidateFace{
		String faceId;			//匹配的faceId
		double similarity;		//匹配度（百分比）
		public CandidateFace(String faceId, double similarity){
			this.faceId = faceId;
			this.similarity = similarity;
		}
	}
}
