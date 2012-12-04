package com.konka.facerecognition;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.konka.facerecognition.api.Group;
import com.konka.facerecognition.api.Person;
import com.konka.facerecognition.data.CompareReturn;
import com.konka.facerecognition.data.DetectReturn;
import com.konka.facerecognition.data.FacePlus;
import com.konka.facerecognition.data.Result;

public class DemoActivity extends Activity implements OnClickListener{
	private static final String TAG = "DemoActivity";
	
	//private ApiHandle apiHandler = new ApiHandle();
	private FacePlus mFacePlus = FacePlus.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo);
		
		findViewById(R.id.detect_btn).setOnClickListener(this);
		findViewById(R.id.compare_btn).setOnClickListener(this);
		findViewById(R.id.recognized_btn).setOnClickListener(this);
		findViewById(R.id.search_btn).setOnClickListener(this);
		findViewById(R.id.train_btn).setOnClickListener(this);
		findViewById(R.id.verify_btn).setOnClickListener(this);
		
		findViewById(R.id.create_person_btn).setOnClickListener(this);
		findViewById(R.id.delete_person_btn).setOnClickListener(this);
		findViewById(R.id.add_face_to_person_btn).setOnClickListener(this);
		findViewById(R.id.remove_face_from_person_btn).setOnClickListener(this);
		findViewById(R.id.get_person_info_btn).setOnClickListener(this);
		findViewById(R.id.set_person_info_btn).setOnClickListener(this);
		
		findViewById(R.id.create_group_btn).setOnClickListener(this);
		findViewById(R.id.delete_group_btn).setOnClickListener(this);
		findViewById(R.id.add_person_to_group_btn).setOnClickListener(this);
		findViewById(R.id.remove_person_from_group_btn).setOnClickListener(this);
		findViewById(R.id.get_group_info_btn).setOnClickListener(this);
		findViewById(R.id.set_group_info_btn).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.detect_btn:
			detectImgAsync();
			break;
		case R.id.compare_btn:
			compareAsync();
			break;
		case R.id.recognized_btn:
			//TODO
			break;
		case R.id.search_btn:
			//TODO
			break;
		case R.id.train_btn:
			//TODO
			break;
		case R.id.verify_btn:
			//TODO
			break;
			
		case R.id.create_person_btn:
			createPersonAsync();
			break;
		case R.id.delete_person_btn:
			//TODO
			break;
		case R.id.add_face_to_person_btn:
			addFace2PersonAsync();
			break;
		case R.id.remove_face_from_person_btn:
			//TODO
			break;
		case R.id.get_person_info_btn:
			getPersonInfoAsync();
			break;
		case R.id.set_person_info_btn:
			//TODO
			break;
			
		case R.id.create_group_btn:
			createGroupAsync();
			break;
		case R.id.delete_group_btn:
			//TODO
			break;
		case R.id.add_person_to_group_btn:
			addPerson2GroupAsync();
			break;
		case R.id.remove_person_from_group_btn:
			//TODO
			break;
		case R.id.get_group_info_btn:
			getGroupInfoAsync();
			break;
		case R.id.set_group_info_btn:
			//TODO
			break;
		default:
			break;
		}
	}
	
	private void detectImgAsync(){
		Debug.debug(TAG, "detectImgAsync");
		new Thread(){
			public void run(){
				//Map<String, String> resultMap;
				//try {
					//Bitmap bmp = BitmapFactory.decodeResource(MainActivity.this.getResources(),R.drawable.test);
					//ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					//bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					//byte[] content = baos.toByteArray();
					//resultMap = apiHandler.detect(content);
					//resultMap = apiHandler.detectByPicUrl("http://img.soufun.com/news/2011_03/15/news/1300174382076_000.jpg");
					//resultMap = apiHandler.detect("/mnt/usb/sda1/test.jpg");
					//if(resultMap == null)
					//	return;
					//Debug.debug(TAG, "code=" + resultMap.get(HttpUtils.CODE));
					//Debug.debug(TAG, "return=" + resultMap.get(HttpUtils.RETURN));
				//} catch (IOException e) {
				//	e.printStackTrace();
				//}
				Result result = mFacePlus.detect("/mnt/usb/sda1/test2.jpg");
				Debug.debug(TAG, "result=" + result.type.name());
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				DetectReturn data = (DetectReturn)result.data;
				Debug.debug(TAG, "sessionId=" + data.sessionId);
				Debug.debug(TAG, "img=" + data.image);
				int size = data.faceList.size();
				for(int i=0; i<size; i++){
					Debug.debug(TAG, "face[" + i + "]=" + data.faceList.get(i));
				}
			}
		}.start();
	}
	
	private void compareAsync(){
		new Thread(){
			public void run(){
				//安以轩的两个faceId
				Result result = mFacePlus.compare("0e15a2bb685bb156085b25275d80cc7c", 
						"e26396c0bcfada1cb8609b0b9f753795");
				Debug.debug(TAG, "result=" + result.type.name());
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				CompareReturn data = (CompareReturn)result.data;
				Debug.debug(TAG, "相似度" + data.similarity);
			}
		}.start();
	}
	
	private void createPersonAsync(){
		Debug.debug(TAG, "createPersonAsync");
		new Thread(){
			public void run(){
				List<String> faceIds = new ArrayList<String>();
				faceIds.add("0e15a2bb685bb156085b25275d80cc7c");
				Result result = mFacePlus.createPerson("安以轩", faceIds, "轩轩", null);
				Debug.debug(TAG, "result=" + result.type.name());
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				Person person = (Person)result.data;
				Debug.debug(TAG, person.toString());
			}
		}.start();
	}
	
	private void addFace2PersonAsync(){
		Debug.debug(TAG, "addFace2PersonAsync");
		new Thread(){
			public void run(){
				List<String> faceIds = new ArrayList<String>();
				faceIds.add("0e15a2bb685bb156085b25275d80cc7c");	//test.jpg的face id
				faceIds.add("e26396c0bcfada1cb8609b0b9f753795");	//test2.jpg的face id
				//安以轩person id
				String personId = "c063bfe42912e432df95a4b9faf869c9";	
				Result result = mFacePlus.addFace2Person(personId, faceIds);
				Debug.debug(TAG, "result=" + result.type.name());
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				Debug.debug(TAG, "执行" + result.data);
			}
		}.start();
	}
	
	private void getPersonInfoAsync(){
		new Thread(){
			public void run(){
				//安以轩person id
				String personId = "c063bfe42912e432df95a4b9faf869c9";
				Result result = mFacePlus.getPersonInfo(personId);
				Debug.debug(TAG, "result=" + result.type.name());
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				Person person = (Person)result.data;
				Debug.debug(TAG, person.toString());
			}
		}.start();
	}
	
	private void createGroupAsync(){
		new Thread(){
			public void run(){
				//KONKA GROUP id "a0e693761294640da25ae62a2e5ded39"
				Result result = mFacePlus.createGroup("KONKA", "康佳智能电视组", null);
				Debug.debug(TAG, "result=" + result.type.name());
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				Group group = (Group)result.data;
				Debug.debug(TAG, group.toString());
			}
		}.start();
	}
	
	private void addPerson2GroupAsync(){
		new Thread(){
			public void run(){
				String groupId = "a0e693761294640da25ae62a2e5ded39";	//konka group id
				List<String> personIds = new ArrayList<String>();
				personIds.add("c063bfe42912e432df95a4b9faf869c9");		//安以轩personId
				Result result = mFacePlus.addPerson2Group(groupId, personIds);
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				Debug.debug(TAG, "执行" + result.data);
			}
		}.start();
	}
	
	private void getGroupInfoAsync(){
		new Thread(){
			public void run(){
				String groupId = "a0e693761294640da25ae62a2e5ded39";	//konka group id
				Result result = mFacePlus.getGroupInfo(groupId);
				if(result.type == Result.TYPE.FAILED){
					Debug.debug(TAG, "err msg = " + result.data);
					return;
				}
				Group group = (Group)result.data;
				Debug.debug(TAG, group.toString());
			}
		}.start();
	}
}
