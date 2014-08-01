package com.fsz570.plus_minus.vo;

public class VoOp2 {
	int id;
	int num1;
	int op1;
	int num2;
	int op2;
	int num3;
	
	public VoOp2(String[] attr){
		if(attr.length == 6){
			setId(Integer.parseInt(attr[0]));
			setNum1(Integer.parseInt(attr[1]));
			setOp1(Integer.parseInt(attr[2]));
			setNum2(Integer.parseInt(attr[3]));
			setOp2(Integer.parseInt(attr[4]));
			setNum3(Integer.parseInt(attr[5]));
		}
	}
	
	public VoOp2(int id, int num1, int op1, int num2, int op2, int num3){
		this.id = id;
		this.num1 = num1;
		this.op1 = op1;
		this.num2 = num2;
		this.op2 = op2;
		this.num3 = num3;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNum1() {
		return num1;
	}
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	public int getOp1() {
		return op1;
	}
	public void setOp1(int op1) {
		this.op1 = op1;
	}
	public int getNum2() {
		return num2;
	}
	public void setNum2(int num2) {
		this.num2 = num2;
	}
	public int getOp2() {
		return op2;
	}
	public void setOp2(int op2) {
		this.op2 = op2;
	}
	public int getNum3() {
		return num3;
	}
	public void setNum3(int num3) {
		this.num3 = num3;
	}
	
	
}
