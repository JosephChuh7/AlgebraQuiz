package com.example.joseph.quiz;

//生成形如 Ax + b = 0 的方程

public class LinearEquation{

//首先需要两个随机数，[-99, 99]
private int A = 1;
private int B = 1;

//生成随机数的上下限
private int max = 99;
private int min = -99;

//每次生成一个随机线性方程后，就将解存放在这里
private String answer = null;

public LinearEquation(int min, int max){
	this.min = min;
	this.max = max;
}


//生成线性方程
public String generateEquation() {

	//将A, B都先设定成随机值
	setRandomAB();

	//将解赋给this.answer
	calcAnswer();

	//生成方程
	String str_A = null;
	String str_B = null;

	if (Math.abs(this.A) != 1) {
		str_A = this.A + "X";
	} else if (this.A == -1) {
		str_A = "-X";
	} else {
		str_A = "X";
	}

	if (this.B >= 0) {
		str_B = " + " + this.B; 
	} else {
		str_B = " - " + Math.abs(this.B);
	}
	//AX + B = 0
	return str_A + str_B + " = 0";

}

//判断是否正确
public boolean isCorrect(String ans) {
	if (QuizHelper.areEqual(ans, this.answer))
		return true;
	else return false;
}

public String getAnswer(){
	return this.answer;
}

//将A, B都设定成随机值
private void setRandomAB(){
	this.A = QuizHelper.getRandom(this.min, this.max);
	
	while (this.A == 0) {
		this.A = QuizHelper.getRandom(this.min, this.max);
	}

	this.B = QuizHelper.getRandom(this.min, this.max);

}

//求线性方程的解
// round to 2 decimal places
private void calcAnswer(){
	//如果能整除，则显示整数

	int dividend = (0 - this.B);  //被除数
	int divisor = A;	//除数

	//如果能够整除，就直接用 整数表示
	//不能整除，就保留两位小数	
	double ans = (double) dividend / (double) divisor;

	this.answer =  QuizHelper.formatSolution(ans);
	
}


}