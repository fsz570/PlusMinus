package com.fsz570.plus_minus.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fsz570.plus_minus.R;
import com.fsz570.plus_minus.vo.VoOp2;
import com.fsz570.plus_minus.vo.VoOp3;
import com.fsz570.plus_minus.vo.VoOp4;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class VoUtil {
	
	private static final String TAG = "VoUtil";
	
	public static List<VoOp2> getOp2List(Context context){
		
		List<VoOp2> list = new ArrayList<VoOp2>();
		
		for(String data : fileReader(context, R.raw.op2)){
			try{
				list.add((VoOp2)voConverter(data, R.raw.op2));
			}catch (Exception e){
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
		}
		
		return list;
	}
	
	public static List<VoOp3> getOp3List(Context context){
		
		List<VoOp3> list = new ArrayList<VoOp3>();
		
		for(String data : fileReader(context, R.raw.op3)){
			try{
				list.add((VoOp3)voConverter(data, R.raw.op3));
			}catch (Exception e){
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
		}
		
		return list;
	}
	
	public static List<VoOp4> getOp4List(Context context){
		
		List<VoOp4> list = new ArrayList<VoOp4>();
		
		for(String data : fileReader(context, R.raw.op4)){
			try{
				list.add((VoOp4)voConverter(data, R.raw.op4));
			}catch (Exception e){
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
		}
		
		return list;
	}
	
	private static Object voConverter(String data, int rawId) throws Exception{
		Object vo = null;
		
		if(data == null){
			data = "";
		}
		
		String[] voAttr = data.split(",");
		
		
		if(rawId == R.raw.op2 && voAttr.length == 6){
			vo = new VoOp2(voAttr);
		}else if(rawId == R.raw.op3 && voAttr.length == 8){
			vo = new VoOp3(voAttr);
		}else if(rawId == R.raw.op4 && voAttr.length == 10){
			vo = new VoOp4(voAttr);
		}else
		
		if(vo == null){
			throw new Exception("Parse error for : " + data);
		}
		
		return vo;
	}
	
	public static List<String> fileReader(Context context, int rawId){
		Resources myRes = context.getResources();
		String data = null;
		List<String> list = new ArrayList<String>();
		
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(myRes.openRawResource(rawId)));
			
			do{
				data = br.readLine();
				if(data == null){
					break;
				}
				list.add(data);
				//Log.i(TAG, data);
			}while(true);
			
		}catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
		
		return list;
	}
}
