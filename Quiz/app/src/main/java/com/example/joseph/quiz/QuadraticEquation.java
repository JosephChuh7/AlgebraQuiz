package com.example.joseph.quiz;/*
Bugs:
2. 	防止输入非数字字符（把String 转换成 double 若出错则提醒用户） Android里再改
*/



public class QuadraticEquation{

	//AX^2 + B + C = 0
	private int A = 1;
	private int B = 1;
	private int C = 1;

	private int delta = 0;

	//生成随机数的上下限
	private int max = 99;
	private int min = -99;


	//生成的方程的解就放这里好了
	//private ArrayList<String> answer = new ArrayList<String>();
	private String[] answer  = new String[2];


	public QuadraticEquation (int min, int max){
		this.min = min;
		this.max = max;
	}


	//生成一元二次方程
	public String generateEquation(){
		//随机生成ABC的值
		setRandomABC();
		//至此delta值已经计算

		//计算方程的解，保存至answer
		calcAnswer();

		//生成方程
		String str_A = null;
		String str_B = null;
		String str_C = null;

		if (Math.abs(this.A) != 1) {
			str_A = this.A + "X^2";
		} else if (this.A == -1) {
			str_A = "-X^2";
		} else {
			str_A = "X^2";
		}


		if (Math.abs(this.B) != 1) {
			if (this.B > 0)
				str_B = " + " + this.B + "X";
			else if(this.B < 0)
				str_B = " - " + Math.abs(this.B) + "X";
			else
				str_B = null;
			
		} else if (this.B == -1) {
			str_B = "- X";
		} else {
			str_B = " X";
		}


		if (this.C >= 0) {
			str_C = " + " + this.C; 
		} else {
			str_C = " - " + Math.abs(this.C);
		}
		//AX^2 + BX + C = 0
		return str_A + str_B + str_C + " = 0";
	}


	//判断是否正确
	//传入两个解，与答案相比较
	public boolean isCorrect(String r1, String r2){
		if ((QuizHelper.areEqual(r1, answer[0]) && QuizHelper.areEqual(r2, answer[1])) || (QuizHelper.areEqual(r1, answer[1]) && QuizHelper.areEqual(r2, answer[0]))){
			return true;
		} else return false;

	}


	//获取结果
	public String getAnswer(int index){
		if(index < 0 || index > 1)
			return null;
		return answer[index];
	}

	public int getDelta(){
		return this.delta;
	}

	//判断是否有解，有解返回true, 无解返回false
	private boolean hasRealRoots(){
		//delta必然为整数
		this.delta = this.B * this.B - 4 * this.A * this.C ;
		if (this.delta >= 0)
			return true;
		else return false;
	}

	private void setRandomABC(){
		this.A = QuizHelper.getRandom(this.min, this.max);
		this.B = QuizHelper.getRandom(this.min, this.max);
		this.C = QuizHelper.getRandom(this.min, this.max);

		//保证A != 0  && 方程有解
		while (this.A == 0 || !this.hasRealRoots()) {
		this.A = QuizHelper.getRandom(this.min, this.max);
		this.B = QuizHelper.getRandom(this.min, this.max);
		this.C = QuizHelper.getRandom(this.min, this.max);
		}
		
	}

	//只要不整除，就保留两位小数。
	private void calcAnswer(){
	
		//Math.sqrt()返回 double类型
		double sqrtDelta = Math.sqrt(this.delta);
		double r1 = ((0 - this.B) + sqrtDelta) / (2 * this.A); // int + double 最终结果是double
		double r2 = ((0 - this.B) - sqrtDelta) / (2 * this.A);

		answer[0] = QuizHelper.formatSolution(r1);
		answer[1] = QuizHelper.formatSolution(r2);

	}

}
