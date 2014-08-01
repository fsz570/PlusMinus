package com.fsz570.plus_minus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Game extends Activity  implements OnClickListener{
	
	private static final String TAG = "Game";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(this);
        
        View settingBtn = findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(this);
        
        Log.d(TAG, "Check and create DB!");
        PlusMinusDBAdapter dbAdapter = new PlusMinusDBAdapter(this);
        try{
        	dbAdapter.createDataBase();
        }catch(Exception e){
        	e.printStackTrace();
        	Log.d(TAG, e.getMessage());
        }finally{
        	dbAdapter.close();	
        }
        
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.start_btn :
			startActivity(new Intent(this, PlusMinus.class));
			break;
		case R.id.setting_btn :
			startActivity(new Intent(this, Prefs.class));
			break;
		}
		
	}
	

}