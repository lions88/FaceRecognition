package com.konka.facerecognition;

import com.konka.facerecognition.data.Configure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity{
	public static final String KEY = "6022c8b994c2b56861f94a39919e0f03";
	public static final String SECRET = "nP2VdyB03W4aaYuDnX8dydaOSTl47ZTL";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Configure.updateProperties(Configure.API_KEY, KEY);
		Configure.updateProperties(Configure.API_SECRET, SECRET);
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	Intent intent = new Intent(this, DemoActivity.class);
        startActivity(intent);
        finish();
    }
}