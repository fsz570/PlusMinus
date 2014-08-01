package com.fsz570.plus_minus;

import java.util.Random;

import com.fsz570.plus_minus.vo.VoOp2;
import com.fsz570.plus_minus.vo.VoOp3;
import com.fsz570.plus_minus.vo.VoOp4;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.CountDownTimer;

public class PlusMinus extends Activity   implements OnClickListener{
	
	LinearLayout operationLayout = null;
	TextView countDownText = null;
	TextView scoreText = null;
	
	int maxOperatorQty = 4;
	int maxOperandQty = 5;
	ImageView[] operatorAry = new ImageView[maxOperatorQty];
	int[] operatorValue = new int[maxOperatorQty];
	ImageView[] operandAry = new ImageView[maxOperandQty];
	int[] operandValue = new int[maxOperandQty];
	
	final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	final int OPERATOR_TEXT_SIZE = 24;
	final int OPERAND_TEXT_SIZE = 48;
	final int OPERATOR_NONE = 0;
	final int OPERATOR_PLUS = 1;
	final int OPERATOR_MINUS = -1;
	int operatorQty = 2;

	int maxOperandValue = 9;
	int currentOperator = 0;
	int totalScore = 0;

	boolean clear = false;
	CountDownTimer countDown = null;
	ImageButton plsuBtn = null;
	ImageButton minusBtn = null;
	Button clearBtn = null;
	Button againBtn = null;

	ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
	PlusMinusDBAdapter dbAdapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_minus);
        
    	String operators = Prefs.getOperators(this);
        
        try{
        	operatorQty = Integer.parseInt(operators);
        }catch(Exception e){
        	
        }
        
        operationLayout = (LinearLayout)findViewById(R.id.operation_layer);
        
        scoreText = (TextView)findViewById(R.id.score_text);
        scoreText.setText(String.valueOf(totalScore));
        
        countDownText = (TextView)findViewById(R.id.count_down_text);
        
        plsuBtn = (ImageButton)findViewById(R.id.plus_btn);
        plsuBtn.setOnClickListener(this);
        
        minusBtn = (ImageButton)findViewById(R.id.minus_btn);
        minusBtn.setOnClickListener(this);
        
        clearBtn = (Button)findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);
        
        againBtn = (Button)findViewById(R.id.again_btn);
        againBtn.setOnClickListener(this);
        
        buildOperation();
        
        startGame();
    }
    
    private void startGame(){
    	currentOperator = 0;
        countDown();
        buildRandomOperation();
        
    	for(int i=0;i<operatorQty;i++){
    		operatorAry[i].setImageResource(R.drawable.empty_background);
    	}
        
		plsuBtn.setEnabled(true);
		minusBtn.setEnabled(true);
		clearBtn.setEnabled(true);
        againBtn.setEnabled(false);
        againBtn.setVisibility(View.INVISIBLE);
    }
    
    private void buildOperation(){
    	
    	for(int i=0;i<operatorQty;i++){
    		operandAry[i] = new ImageView(this);
    		operandAry[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
    		operandAry[i].setImageResource(R.drawable.empty_background);
    		operationLayout.addView(operandAry[i], new LinearLayout.LayoutParams(WC, WC));
    		
    		operatorAry[i] = new ImageView(this);
    		operatorAry[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
    		operatorAry[i].setImageResource(R.drawable.empty_background);
    		operationLayout.addView(operatorAry[i], new LinearLayout.LayoutParams(WC, WC));
    	}
    	
    	operandAry[operatorQty] = new ImageView(this); //Operand qty = operator qty + 1
    	operandAry[operatorQty].setScaleType(ImageView.ScaleType.FIT_CENTER);
    	operandAry[operatorQty].setImageResource(R.drawable.empty_background);
    	operationLayout.addView(operandAry[operatorQty], new LinearLayout.LayoutParams(WC, WC));
        
        TextView equal = new TextView(this);
        equal.setText(R.string.equal_operator);
        equal.setTextSize(OPERATOR_TEXT_SIZE);
        operationLayout.addView(equal, new LinearLayout.LayoutParams(WC, WC));
        
        ImageView result = new ImageView(this);
        result.setScaleType(ImageView.ScaleType.FIT_CENTER);
        result.setImageResource(R.drawable.number_10);
        operationLayout.addView(result, new LinearLayout.LayoutParams(WC, WC));
    }
    
    private void buildRandomOperation(){
    	VoOp2 op2 = null;
    	VoOp3 op3 = null;
    	VoOp4 op4 = null;
    	
    	dbAdapter = new PlusMinusDBAdapter(this);
    	
    	if(operatorQty == 2){
    		op2 = dbAdapter.fetchOp2(nextRandomOp2());
        	operandAry[0].setImageResource(getNumberImage(op2.getNum1()));
        	operandValue[0] = op2.getNum1();
        	
        	operandAry[1].setImageResource(getNumberImage(op2.getNum2()));
        	operandValue[1] = op2.getNum2();
        	
        	operandAry[2].setImageResource(getNumberImage(op2.getNum3()));
        	operandValue[2] = op2.getNum3();
    	}else if(operatorQty == 3){
    		op3 = dbAdapter.fetchOp3(nextRandomOp3());
    		
        	operandAry[0].setImageResource(getNumberImage(op3.getNum1()));
        	operandValue[0] = op3.getNum1();
        	
        	operandAry[1].setImageResource(getNumberImage(op3.getNum2()));
        	operandValue[1] = op3.getNum2();
        	
        	operandAry[2].setImageResource(getNumberImage(op3.getNum3()));
        	operandValue[2] = op3.getNum3();
        	
        	operandAry[3].setImageResource(getNumberImage(op3.getNum4()));
        	operandValue[3] = op3.getNum4();
    	}else if(operatorQty == 4){
    		op4 = dbAdapter.fetchOp4(nextRandomOp4());
    		
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
    	}

    }
    
    private int nextRandomOp2(){
    	Random randomOperand = new Random(System.currentTimeMillis());
    	
    	return randomOperand.nextInt(107)+1;
    }
    
    private int nextRandomOp3(){
    	Random randomOperand = new Random(System.currentTimeMillis());
    	
    	return randomOperand.nextInt(1802)+1;
    }
    
    private int nextRandomOp4(){
    	Random randomOperand = new Random(System.currentTimeMillis());
    	
    	return randomOperand.nextInt(28889)+1;
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.plus_btn :
				operatorAry[currentOperator].setImageResource(R.drawable.plus_pressed);
				operatorValue[currentOperator] = OPERATOR_PLUS;
				currentOperator++;				
				break;
			case R.id.minus_btn :
				operatorAry[currentOperator].setImageResource(R.drawable.minus_pressed);
				operatorValue[currentOperator] = OPERATOR_MINUS;
				currentOperator++;
				break;
				
			case R.id.clear_btn :				
				for(int i=0;i<operatorQty;i++){
					operatorAry[i].setImageResource(R.drawable.empty_background);
					operatorValue[i] = OPERATOR_NONE;
				}
				currentOperator = 0;
				plsuBtn.setEnabled(true);
				minusBtn.setEnabled(true);
				break;
			case R.id.again_btn :	
				currentOperator = 0;
				reload();
				break;
		}
		
		if(currentOperator >= operatorQty){
			if(checkAnswer()){
				int leftSeconds = Integer.parseInt((String)countDownText.getText());
				int lastScore = Integer.parseInt((String)scoreText.getText());
				
				clear = true;
				countDown.cancel();
				countDownText.setTextColor(Color.WHITE);
            	countDownText.setTextSize(48);
            	countDownText.setText(R.string.clear);
            	clearBtn.setEnabled(false);
            	
            	againBtn.setEnabled(true);
                againBtn.setVisibility(View.VISIBLE);
                
                totalScore = calScore(leftSeconds, lastScore);
                scoreText.setText(String.valueOf(totalScore));
			}else{
				clear = false;
			}
			
			plsuBtn.setEnabled(false);
			minusBtn.setEnabled(false);
		}
	}
	
	private boolean checkAnswer(){
		int total = 0;
		
		total = operandValue[0];
		
		for(int i=0;i<operatorQty;i++){

			switch(operatorValue[i]){
				case OPERATOR_PLUS:
					total+=operandValue[i+1];
					break;
				case OPERATOR_MINUS:
					total-=operandValue[i+1];
					break;
			}

		}
		
		return (total==10);
	}
	
    private void countDown(){
    	   	
    	int seconds = 10;
    	
        countDownText.setText(Prefs.getTimes(this));
        
        try{
        	seconds = Integer.parseInt(Prefs.getTimes(this));
        }catch(Exception e){
        	
        }
        
        countDown = new CountDownTimer(seconds*1000, 1000) {

            public void onTick(long millisUntilFinished) {
            	long seconds = millisUntilFinished / 1000;
            	
            	countDownText.setText(""+seconds);
            	
            	
            	if(seconds<=5){
            		countDownText.setTextColor(Color.RED);
            		playTone(ToneGenerator.TONE_PROP_PROMPT);
            	}else{
            		playTone(ToneGenerator.TONE_PROP_BEEP);
            	}
            }

            public void onFinish() {
            	clearBtn.setEnabled(false);
            	plsuBtn.setEnabled(false);
    			minusBtn.setEnabled(false);
            	countDownText.setTextColor(Color.WHITE);
            	countDownText.setTextSize(48);
            	countDownText.setText(R.string.game_over);
            	
            	againBtn.setEnabled(true);
                againBtn.setVisibility(View.VISIBLE);
                dbAdapter.updateHighScore("David Wu",totalScore);
            }
         }.start();
    }
    
    private int calScore(int leftSeconds, int currentScore){
    	int finalScore = 0;
    	
    	int times = Integer.parseInt(Prefs.getTimes(this));
    	int operators = Integer.parseInt(Prefs.getOperators(this)); 
    	
    	if(operators == 4){
    		finalScore = leftSeconds * 3;
    	}else if(operators == 3){
    		if(times - leftSeconds > 9){
    			finalScore = 1;
    		}else{
    			finalScore = leftSeconds * 2;
    		}
    	}else if(operators == 2){
    		if(times - leftSeconds > 6){
    			finalScore = 1;
    		}else{
    			finalScore = leftSeconds;
    		}
    	}
    	
    	return finalScore + currentScore;
    }
    
    private void reload() {
    	startGame();
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
    }
    
    private void playTone(int tone){
    	//toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP); do
    	//toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK); da da
    	//toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2); do do
    	//toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK); count down
    	//toneGenerator.startTone(ToneGenerator.TONE_PROP_PROMPT); count down
    	toneGenerator.startTone(tone);
    }
    
    private int getNumberImage(int num){
    	
    	switch(num){
	    	case 1 : return R.drawable.number_1;
	    	case 2 : return R.drawable.number_2;
	    	case 3 : return R.drawable.number_3;
	    	case 4 : return R.drawable.number_4;
	    	case 5 : return R.drawable.number_5;
	    	case 6 : return R.drawable.number_6;
	    	case 7 : return R.drawable.number_7;
	    	case 8 : return R.drawable.number_8;
	    	case 9 : return R.drawable.number_9;
    	}
    	
    	return R.drawable.number_0; //Should not happen
    }

}
