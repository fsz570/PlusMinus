package com.fsz570.plus_minus;

public class Constants {

	//Text related constants
	public static final int OPERATOR_TEXT_SIZE = 24;
	public static final int OPERAND_TEXT_SIZE = 48;
	
	//Game related constants
	public static final int OPERATOR_NONE = 0;
	public static final int OPERATOR_PLUS = 1;
	public static final int OPERATOR_MINUS = -1;
	
	//Sound related constants
	public static final int MAX_STREAM_SIZE = 2;		//�P�ɳ̦h������n������
	public static final int SRC_QUALITY = 0;			//Default, �ثe�S���@��
	public static final int SRC_PRIORITY = 1;			//Default, �ثe�S���@��
	public static final float INIT_TICKING_RATE = 1.0f;	//�@��B��ɪ��n���W�v
	//public static final float ERGY_TICKING_RATE = 1.3f;	//�̫᤭���n���W�v
	//public static final float ERGY_TICKING_RATE = 1.1f;	//�̫᤭���n���W�v
	public static final float[] ERGY_TICKING_RATE = {1.2f, 1.2f, 1.16f, 1.12f, 1.08f, 1.04f};	//�̫᤭���n���W�v
	
	public static final int[] OP_MAX = {0,0,107,1802,28889};//�B�⤸���̤j�ռ�

}
