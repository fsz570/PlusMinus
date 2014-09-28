package com.fsz570.plus_minus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

//遊戲開始畫面, 提供開始遊戲與設定兩個功能選項
public class Game extends Activity  implements OnClickListener{
	
	private static final String TAG = "Game";
	ProgressDialog ringProgressDialog = null;
	
	//TODO 將 Start Game 與 Setting 改為圖檔
	//TODO 增加 How to play
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Start Game
        View startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(this);
        
        //Setting
        View settingBtn = findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(this);
        
        initialGame();
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
	
	private void initialGame(){
		
		ringProgressDialog = ProgressDialog.show(Game.this, "Please wait ...", "Initializing ...", true);
		Log.d(TAG, "Showing progress dialog.");
              

        	Runnable runnable = new Runnable() {
        	    @Override
        	    public void run() {

        	        Log.d(TAG, "initialGame()");
        	        //Instant the DB Adapter will create the DB is it not exist.
        	    	PlusMinusDBAdapter dbAdapter = new PlusMinusDBAdapter(Game.this);

        	        // code that needs 6 seconds for execution
        	        try{
        	        	dbAdapter.createDataBase();

        	        }catch(Exception e){
        	        	Log.d(TAG, "run()");
        	        	Log.d(TAG, e.getMessage());
        	        }finally{
        	        	dbAdapter.close();	
        	        }
        	        // after finishing, close the progress bar
        	        Log.d(TAG, "Dismiss progress dialog.");
        	    	ringProgressDialog.dismiss();
        	    	dbAdapter = null;
        	    }
        	};

        	new Thread(runnable).start();

	}

}