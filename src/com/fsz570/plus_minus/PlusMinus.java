package com.fsz570.plus_minus;

import java.util.Random;

import com.fsz570.plus_minus.vo.VoOp2;
import com.fsz570.plus_minus.vo.VoOp3;
import com.fsz570.plus_minus.vo.VoOp4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

public class PlusMinus extends Activity   implements OnClickListener, SoundPool.OnLoadCompleteListener{
	
	private static final String TAG = "PlusMinus";
	
	private int TICKING_CLOCK_SOUND_ID = 0;		//滴答聲的 sound id, load 時取得
	private int TICKING_CLOCK_STREAM_ID = 0;	//滴答聲的 stream id, play 時取得, stop 時會用到
	
	LinearLayout operationLayout = null;
	TextView countDownText = null;
	TextView scoreText = null;
	SoundPool soundPool = null;
	
	int maxOperatorQty = 4;
	int maxOperandQty = 5;
	ImageView[] operatorAry = new ImageView[maxOperatorQty];
	int[] operatorValue = new int[maxOperatorQty];
	ImageView[] operandAry = new ImageView[maxOperandQty];
	int[] operandValue = new int[maxOperandQty];
	
	ImageView equal = null;
	
	final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	final LayoutParams OPERAND_LAYOUT_PARAM = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,	LinearLayout.LayoutParams.MATCH_PARENT,	1.0f);
	
	final LayoutParams OPERATOR_LAYOUT_PARAM = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,	LinearLayout.LayoutParams.MATCH_PARENT,	1.1f);
	
	final LayoutParams RESULT_LAYOUT_PARAM = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,	LinearLayout.LayoutParams.MATCH_PARENT,	1.0f);
	
	final int NUMBER_IMAGE_ARRAY[] = { R.drawable.number_0, R.drawable.number_1,
			R.drawable.number_2, R.drawable.number_3, R.drawable.number_4,
			R.drawable.number_5, R.drawable.number_6, R.drawable.number_7,
			R.drawable.number_8, R.drawable.number_9 };
	
	final int NUMBER_IMAGE_ARRAY_A[] = { R.drawable.number_0, R.drawable.number_1,
			R.drawable.number_2, R.drawable.number_3, R.drawable.number_4,
			R.drawable.number_5, R.drawable.number_6, R.drawable.number_7,
			R.drawable.number_8, R.drawable.number_9 };
	
	final int NUMBER_IMAGE_ARRAY_B[] = { R.drawable.number_0, R.drawable.number_1,
			R.drawable.number_2, R.drawable.number_3, R.drawable.number_4,
			R.drawable.number_5, R.drawable.number_6, R.drawable.number_7,
			R.drawable.number_8, R.drawable.number_9 };
	
//	final int NUMBER_IMAGE_ARRAY_A[] = { R.drawable.number_a_0, R.drawable.number_a_1,
//			R.drawable.number_a_2, R.drawable.number_a_3, R.drawable.number_a_4,
//			R.drawable.number_a_5, R.drawable.number_a_6, R.drawable.number_a_7,
//			R.drawable.number_a_8, R.drawable.number_a_9 };
	
//	
//	final int NUMBER_IMAGE_ARRAY_B[] = { R.drawable.number_b_0, R.drawable.number_b_1,
//			R.drawable.number_b_2, R.drawable.number_b_3, R.drawable.number_b_4,
//			R.drawable.number_b_5, R.drawable.number_b_6, R.drawable.number_b_7,
//			R.drawable.number_b_8, R.drawable.number_b_9 };
	
	Animation countDownAnim = null;
	int remainTimes = -1;
	
	int operatorQty = 2;
	boolean isEndless = false;
	boolean isHowToPlay = false;

	int maxOperandValue = 9;
	int currentOperator = 0;
	int totalScore = 0;
	int completeCount = 0;
	boolean isRightAnswer = false;
	final int ROTATE_NUMBER_COUNT = 10;

	boolean clear = false;
	CountDownTimer countDown = null;
	CountDownTimer rightAnswerCountDown = null;
	
	LinearLayout btnParentLayout = null;
	ImageButton plsuBtn = null;
	ImageButton minusBtn = null;
	Button clearBtn = null;
	Button againBtn = null;

	PlusMinusDBAdapter dbAdapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.plus_minus);

        init();
    }
    
    //TODO 要再加強暫停後繼續時, 重回原遊戲畫面的功能
    protected void onResume(){
    	super.onResume();
    	
    	if (soundPool != null){
    		soundPool.autoResume();
    	}else{
    		initSoundPool();
    	}
    	Log.d(TAG, "onResume() : autoResume");
    }
    
    //TODO 要再加強暫停後繼續時, 重回原遊戲畫面的功能
    protected void onPause(){
    	super.onPause();
    	
    	soundPool.autoPause();
    	Log.d(TAG, "onPause() : autoPause");
    }
    
    //Stop sound and release soundPol
    protected void onStop(){
    	super.onStop();
    	
    	if(soundPool != null){
	    	soundPool.stop(TICKING_CLOCK_STREAM_ID);
	    	soundPool.release();
	    	soundPool = null;
	    	Log.d(TAG, "onStop() : stop");
    	}
    	
    	if(countDown != null){
    		countDown.cancel();
    	}
    }
    
    private void init(){
        initSoundPool();
        
    	String operators = Prefs.getOperators(this);
        
        try{
        	operatorQty = Integer.parseInt(operators);
        }catch(Exception e){
        	
        }
        
        isEndless = Prefs.getEndless(this);
        isHowToPlay = Prefs.getHowToPlay(this);
        
        operationLayout = (LinearLayout)findViewById(R.id.operation_layer);
        
        scoreText = (TextView)findViewById(R.id.score_text);
        scoreText.setText(String.valueOf(totalScore));
        
        countDownText = (TextView)findViewById(R.id.count_down_text);
        
        
        btnParentLayout = (LinearLayout)findViewById(R.id.operator_layer);
        
        plsuBtn = (ImageButton)findViewById(R.id.plus_btn);
        plsuBtn.setOnClickListener(this);
        
        minusBtn = (ImageButton)findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(this);
        
        clearBtn = (Button)findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);
        
        againBtn = (Button)findViewById(R.id.again_btn);
        againBtn.setOnClickListener(this);
        
        btnParentLayout.removeView(againBtn);
        
        countDownAnim = AnimationUtils.loadAnimation(PlusMinus.this, R.anim.count_down_anim);
        
        completeCount = 0;
        buildOperation();
    }
    
    private void buildOperation(){
    	
    	for(int i=0;i<operatorQty;i++){
    		operandAry[i] = new ImageView(this);
    		operandAry[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
    		operandAry[i].setImageResource(R.drawable.empty_background);
    		//operationLayout.addView(operandAry[i], new LinearLayout.LayoutParams(WC, WC));
    		operationLayout.addView(operandAry[i], OPERAND_LAYOUT_PARAM);
    		
    		operatorAry[i] = new ImageView(this);
    		operatorAry[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
    		operatorAry[i].setImageResource(R.drawable.empty_operator);
    		//operationLayout.addView(operatorAry[i], new LinearLayout.LayoutParams(WC, WC));
    		operationLayout.addView(operatorAry[i], OPERATOR_LAYOUT_PARAM);
    	}
    	
    	operandAry[operatorQty] = new ImageView(this); //Operand qty = operator qty + 1
    	operandAry[operatorQty].setScaleType(ImageView.ScaleType.FIT_CENTER);
    	operandAry[operatorQty].setImageResource(R.drawable.empty_background);
    	//operationLayout.addView(operandAry[operatorQty], new LinearLayout.LayoutParams(WC, WC));
    	operationLayout.addView(operandAry[operatorQty], OPERAND_LAYOUT_PARAM);
        
        equal = new ImageView(this);
        equal.setScaleType(ImageView.ScaleType.FIT_CENTER);
        equal.setImageResource(R.drawable.equal);
        //operationLayout.addView(result, new LinearLayout.LayoutParams(WC, WC));
        operationLayout.addView(equal, OPERATOR_LAYOUT_PARAM);
        
        ImageView result = new ImageView(this);
        result.setScaleType(ImageView.ScaleType.FIT_CENTER);
        result.setImageResource(R.drawable.number_10);
        //operationLayout.addView(result, new LinearLayout.LayoutParams(WC, WC));
        operationLayout.addView(result, RESULT_LAYOUT_PARAM);
    }
    
    
    private void startGame(){
    	currentOperator = 0;
        buildRandomOperation();
        
    	for(int i=0;i<operatorQty;i++){
    		operatorAry[i].setImageResource(R.drawable.empty_operator);
    	}
    	equal.setImageResource(R.drawable.equal);
        
    	btnParentLayout.removeAllViews();
    	btnParentLayout.addView(plsuBtn);
    	btnParentLayout.addView(minusBtn);
    	btnParentLayout.addView(clearBtn);
    	
    	plsuBtn.setEnabled(true);
    	minusBtn.setEnabled(true);
    	clearBtn.setEnabled(true);

    	//如果是第一次開始, 取得預設秒數
    	try{
    		int initialTimes = Integer.parseInt(Prefs.getTimes(this));
    		remainTimes = initialTimes;
        }catch(Exception e){
        	
        }
        
		countDownText.setTextColor(Color.WHITE);
    	countDownText.setTextSize(48);
        countDownText.setText(""+remainTimes);
        
        countDownAnim = AnimationUtils.loadAnimation(PlusMinus.this, R.anim.count_down_anim);
        
		if (isHowToPlay) {
			final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
			
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface arg0) {
					playGame();
				}
			});

			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			dialog.setContentView(R.layout.activity_how_to);
			
			Button myDialogButton = (Button)dialog.findViewById(R.id.howto_btn);
			myDialogButton.setOnClickListener(new View.OnClickListener() {

			    @Override
			    public void onClick(View v) {
			    	//playGame();
			    	dialog.dismiss();
			    }
			});
			dialog.show();

			showHowToPlay(dialog);
		}else{
			playGame();
		}
    }
    
    private void playGame(){
        countDown();
        TICKING_CLOCK_STREAM_ID = soundPool.play(TICKING_CLOCK_SOUND_ID, 1.0f, 1.0f, 1, -1, Constants.INIT_TICKING_RATE);    	
    }
    
    private void buildRandomOperation(){
    	VoOp2 op2 = null;
    	VoOp3 op3 = null;
    	VoOp4 op4 = null;
    	
    	dbAdapter = new PlusMinusDBAdapter(this);
    	
    	if(operatorQty == 2){
    		op2 = dbAdapter.fetchOp2(nextRandomOp(operatorQty));
    	}else if(operatorQty == 3){
    		op3 = dbAdapter.fetchOp3(nextRandomOp(operatorQty));
    	}else if(operatorQty == 4){
    		op4 = dbAdapter.fetchOp4(nextRandomOp(operatorQty));
    	}
    	
    	if(operatorQty == 2){
        	operandAry[0].setImageResource(getNumberImage(op2.getNum1()));
        	operandValue[0] = op2.getNum1();
        	
        	operandAry[1].setImageResource(getNumberImage(op2.getNum2()));
        	operandValue[1] = op2.getNum2();
        	
        	operandAry[2].setImageResource(getNumberImage(op2.getNum3()));
        	operandValue[2] = op2.getNum3();
        	
        	Log.d(TAG, op2.toString());
    	}else if(operatorQty == 3){   		
        	operandAry[0].setImageResource(getNumberImage(op3.getNum1()));
        	operandValue[0] = op3.getNum1();
        	
        	operandAry[1].setImageResource(getNumberImage(op3.getNum2()));
        	operandValue[1] = op3.getNum2();
        	
        	operandAry[2].setImageResource(getNumberImage(op3.getNum3()));
        	operandValue[2] = op3.getNum3();
        	
        	operandAry[3].setImageResource(getNumberImage(op3.getNum4()));
        	operandValue[3] = op3.getNum4();
        	
        	Log.d(TAG, op3.toString());
    	}else if(operatorQty == 4){
        	operandAry[0].setImageResource(getNumberImage(op4.getNum1()));
        	operandValue[0] = op4.getNum1();
        	
        	operandAry[1].setImageResource(getNumberImage(op4.getNum2()));
        	operandValue[1] = op4.getNum2();
        	
        	operandAry[2].setImageResource(getNumberImage(op4.getNum3()));
        	operandValue[2] = op4.getNum3();
        	
        	operandAry[3].setImageResource(getNumberImage(op4.getNum4()));
        	operandValue[3] = op4.getNum4();
        	
        	operandAry[4].setImageResource(getNumberImage(op4.getNum5()));
        	operandValue[4] = op4.getNum5();
        	
        	Log.d(TAG, op4.toString());
    	}

    }

    private int nextRandomOp(int op){
    	Random randomOperand = new Random(System.currentTimeMillis());
    	
    	return randomOperand.nextInt(Constants.OP_MAX[op])+1;
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.plus_btn :
				operatorAry[currentOperator].setImageResource(R.drawable.plus_operator);
				operatorValue[currentOperator] = Constants.OPERATOR_PLUS;
				currentOperator++;				
				break;
			case R.id.minus_btn :
				operatorAry[currentOperator].setImageResource(R.drawable.minus_operator);
				operatorValue[currentOperator] = Constants.OPERATOR_MINUS;
				currentOperator++;
				break;
				
			case R.id.clear_btn :				
				for(int i=0;i<operatorQty;i++){
					operatorAry[i].setImageResource(R.drawable.empty_operator);
					operatorValue[i] = Constants.OPERATOR_NONE;
				}
				currentOperator = 0;
				plsuBtn.setEnabled(true);
				minusBtn.setEnabled(true);
				equal.setImageResource(R.drawable.equal);
				break;
			case R.id.again_btn :	
				currentOperator = 0;
				reload();
				break;
		}
		
		if(currentOperator >= operatorQty){
			if(checkAnswer()){
				rightAnswer();
			}else{
				clear = false;
				equal.setImageResource(R.drawable.not_equal);
			}
			
			plsuBtn.setEnabled(false);
			minusBtn.setEnabled(false);
		}
	}
	//TODO 答對時多一點效果
	private void rightAnswer(){
		int leftSeconds = Integer.parseInt((String)countDownText.getText());
		int lastScore = Integer.parseInt((String)scoreText.getText());
		
		clear = true;
		countDown.cancel();
		
		countDownText.setTextColor(Color.WHITE);
    	countDownText.setTextSize(48);
    	
    	if(completeCount == ROTATE_NUMBER_COUNT){
    		countDownText.setTextColor(Color.RED);
    		countDownText.setText(R.string.rotate);
    	}else{
    		countDownText.setText(R.string.clear);
    	}
		Animation correctAnswerAnim = AnimationUtils.loadAnimation(PlusMinus.this, R.anim.correct_answer_anim);
		countDownText.startAnimation(correctAnswerAnim);
		//countDownText.clearAnimation();
		
		soundPool.stop(TICKING_CLOCK_STREAM_ID);
		
    	clearBtn.setEnabled(false);
    	
        completeCount++;
        isRightAnswer = true;
        
        totalScore = calScore(leftSeconds, lastScore);
        //不顯示分數，改為顯示次數
        //scoreText.setText(String.valueOf(totalScore));
        scoreText.setText(String.valueOf(completeCount));
    	
		for(int i=0;i<=operatorQty;i++){
	    	operandAry[i].clearAnimation();
		}
		//如果是無限關卡, 繼續下一個遊戲
		if(isEndless){
			nextGame();
		}else{
               
			btnParentLayout.removeAllViews();
            btnParentLayout.addView(againBtn);
            
            remainTimes = 0;
            
    		for(int i=0;i<=operatorQty;i++){
		    	operandAry[i].clearAnimation();
    		}
		}
	}
	
	private void nextGame(){
		

			//停止一秒後繼續下一個
			rightAnswerCountDown = new CountDownTimer(1000, 1000) {
	
	            public void onTick(long millisUntilFinished) {
	            	//Do nothing
	            }
	
	            public void onFinish() {
	            	if (completeCount > ROTATE_NUMBER_COUNT){
	            		for(int i=0;i<=operatorQty;i++){
	        		    	operandAry[i].startAnimation(AnimationUtils.loadAnimation(PlusMinus.this, R.anim.number_rotate_anim));
	            		}
	            	}
	            	startGame();
	            }
	         }.start();
		
	}
	
	private boolean checkAnswer(){
		int total = 0;
		
		total = operandValue[0];
		
		for(int i=0;i<operatorQty;i++){

			switch(operatorValue[i]){
				case Constants.OPERATOR_PLUS:
					total+=operandValue[i+1];
					break;
				case Constants.OPERATOR_MINUS:
					total-=operandValue[i+1];
					break;
			}

		}
		
		return (total==10);
	}

    private void countDown(){

    	//如果是第一次開始, 取得預設秒數
    	try{
    		int initialTimes = Integer.parseInt(Prefs.getTimes(this));
    		remainTimes = initialTimes;
        }catch(Exception e){
        	
        }
        
        countDown = new CountDownTimer(remainTimes*1000, 1000) {

            public void onTick(long millisUntilFinished) {
            	int seconds = (int)millisUntilFinished / 1000;
            	
            	countDownText.setText(""+seconds);
            	
            	if(seconds==5){
            		//countDownText.setTextColor(R.color.count_down_5_color);
            		countDownText.setTextColor(Color.parseColor("#FFD700"));
            		
            		countDownAnim = AnimationUtils.loadAnimation(PlusMinus.this, R.anim.count_down_5_anim);
            	}
            	
            	//改變聲音頻率
            	if(seconds<=5){
	        		if(soundPool != null ){
	        			soundPool.setRate(TICKING_CLOCK_STREAM_ID, Constants.ERGY_TICKING_RATE[seconds]);
	        		}
            	}
            	
            	countDownText.startAnimation(countDownAnim);
            	remainTimes = seconds;
            }

            public void onFinish() {
            	
    			countDownText.clearAnimation();
            	countDownText.setTextColor(Color.WHITE);
            	countDownText.setTextSize(48);
            	countDownText.setText(R.string.game_over);
            	
            	Animation correctAnswerAnim = AnimationUtils.loadAnimation(PlusMinus.this, R.anim.correct_answer_anim);
        		countDownText.startAnimation(correctAnswerAnim);
            	
            	soundPool.stop(TICKING_CLOCK_STREAM_ID);
               
    			btnParentLayout.removeAllViews();
                btnParentLayout.addView(againBtn);
                
                remainTimes = 0;
                
                completeCount = 0;
                isRightAnswer = false;
        		for(int i=0;i<=operatorQty;i++){
    		    	operandAry[i].clearAnimation();
        		}
            }
         }.start();
    }
    
    //計算成績的公式簡化
    private int calScore(int leftSeconds, int currentScore){
    	
    	return leftSeconds + currentScore;
    }
    
    //再來一次
    private void reload() {
    	
    	if(!isRightAnswer){
    		scoreText.setText("0");
    	}
    	
    	startGame();
    }
    
    private int getNumberImage(int num){   	
    	
    	if(completeCount < 5){
    		return NUMBER_IMAGE_ARRAY[num];
    	}else if(completeCount < 10){
    		return NUMBER_IMAGE_ARRAY_A[num];
    	}else{
    		return NUMBER_IMAGE_ARRAY_B[num];
    	}
    }
    
    //Initial sound pool, load sounds
    private void initSoundPool(){
		soundPool = new SoundPool(Constants.MAX_STREAM_SIZE,
				AudioManager.STREAM_MUSIC, Constants.SRC_QUALITY);
		
		TICKING_CLOCK_SOUND_ID = soundPool.load(this,
				R.raw.ticking_clock, Constants.SRC_PRIORITY);
//		TICKING_CLOCK_SOUND_ID = soundPool.load(this,
//				R.raw.clock, Constants.SRC_PRIORITY);
		
		soundPool.setOnLoadCompleteListener(this);
    }
    
 // 在此設定 sound load 完成後再開始遊戲
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		startGame();
	}
    
    private void showHowToPlay(Dialog dialog){

    	RelativeLayout howToPlayLayout = (RelativeLayout) dialog.findViewById(R.id.how_to_play_layout); 
    	
    	Animation animation = (Animation)AnimationUtils.loadAnimation(this, R.anim.how_to_play_anim);
        LayoutAnimationController lac = new LayoutAnimationController(animation);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(1f);
        howToPlayLayout.setLayoutAnimation(lac);
    	
    	stopHowToPlay();
    }
    
    @TargetApi(9)
	private void stopHowToPlay(){
		isHowToPlay = false;		
		
		//The apply method only available after SDK level 9
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			//saveHowToPlay();
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("howToPlay", isHowToPlay).apply();
		}else{
			Log.d(TAG, "SDK level < 9");
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("howToPlay", isHowToPlay).commit();
		}
	}
   
   
}
