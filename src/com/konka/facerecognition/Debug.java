package com.konka.facerecognition;

import android.util.Log;

public class Debug{
	//release版本改为false，关闭打印
	private static boolean isDebug = true;		
	private static boolean isWarn = true;
	private static boolean isError = true;

	public static void debug(String tag, String msg){
		if(isDebug)
			Log.v(tag, "###"+msg);
	}

	public static void warn(String tag, String msg){
		if(isWarn)
			Log.v(tag, "###"+msg);
	}

	public static void error(String tag, String msg, Throwable tr) {
		if(isError)
			Log.e(tag, msg, tr);
	}
}
