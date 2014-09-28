package com.fsz570.plus_minus.vo;

public class VoOp4 {
	int id;
	int num1;
	int op1;
	int num2;
	int op2;
	int num3;
	int op3;
	int num4;
	int op4;
	int num5;
	
	public VoOp4(String[] attr){
		if(attr.length == 10){
			setId(Integer.parseInt(attr[0]));
			setNum1(Integer.parseInt(attr[1]));
			setOp1(Integer.parseInt(attr[2]));
			setNum2(Integer.parseInt(attr[3]));
			setOp2(Integer.parseInt(attr[4]));
			setNum3(Integer.parseInt(attr[5]));
			setOp3(Integer.parseInt(attr[6]));
			setNum4(Integer.parseInt(attr[7]));
			setOp4(Integer.parseInt(attr[8]));
			setNum5(Integer.parseInt(attr[9]));
		}
	}
	
	public VoOp4(int id, int num1, int op1, int num2, int op2, int num3, int op3, int num4, int op4, int num5){
		this.id = id;
		this.num1 = num1;
		this.op1 = op1;
		this.num2 = num2;
		this.op2 = op2;
		this.num3 = num3;
		this.op3 = op3;
		this.num4 = num4;
		this.op4 = op4;
		this.num5 = num5;
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


	public int getOp3() {
		return op3;
	}


	public void setOp3(int op3) {
		this.op3 = op3;
	}


	public int getNum4() {
		return num4;
	}


	public void setNum4(int num4) {
		this.num4 = num4;
	}


	public int getOp4() {
		return op4;
	}


	public void setOp4(int op4) {
		this.op4 = op4;
	}


	public int getNum5() {
		return num5;
	}


	public void setNum5(int num5) {
		this.num5 = num5;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.num1).append((this.op1 == 1) ? "+" : "-")
				.append(this.num2).append((this.op2 == 1) ? "+" : "-")
				.append(this.num3).append((this.op3 == 1) ? "+" : "-")
				.append(this.num4).append((this.op4 == 1) ? "+" : "-")
				.append(this.num5);
		
		return sb.toString();
	}
}
