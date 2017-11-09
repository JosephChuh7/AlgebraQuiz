package com.example.joseph.quiz;

import java.util.Random;
import java.math.BigDecimal;


public abstract class QuizHelper {

	//输入两个整数a, b 求它们的最大公约数
	public static int getGcd(int a, int b){
		//声明较大数与较小数
		int big, small;  
		//因为计算最大公约数的两个数必须为自然数
		//所以取绝对值
		a = Math.abs(a);
		b = Math.abs(b);

		if(a > b){
			big = a;
			small = b;
		} else {
			big = b;
			small = a;
		}

		//Algorithm: 辗转相除法
		/*
		*  始终用较大数除以较小数，然后用余数代替较大数，
		*  整除时的除数就是最大公约数
		*/
		int remainder = big % small;

		while (remainder != 0) {

			big = small;
			small = remainder;
			remainder = big % small;
		}

		//int gcd = small;
		return small;
}
	
	//输入：分子，分母，
	//输出：A/B 形式的字符串
	public static String getFraction(int numerator, int denominator) {
			
		//得到无符号的 A/B
		String unsignedFac = Math.abs(numerator) + "/" + Math.abs(denominator);
		//两数异号，就加上负号再返回
		if (isContrarySign(numerator, denominator)) {
			return "-" + unsignedFac;
		}
		return unsignedFac;
	}
	//判断是否异号
	//异号：true  非异号：false
	public static boolean isContrarySign(int a, int b) {

		//java 整型int占4个字节32位，两个数异或后移动31位判断结果，如果是1则异号，如果是0则同号
		int temp = (a ^ b) >>> 31;

		if (temp == 1) 
			return true;
		else return false;
	}


	//获取 [min, max]内的 随机数
	public static int getRandom(int min, int max){
		
		return new Random().nextInt(max - min + 1) + min;
	}

	//输入 double
	//保留两位小数
	public static String rnd2Decm(double d) {
		
		return String.format("%.2f", d);

	}

	//对
	//传入double，如果是小数点后为零，则取整，不为零则保留两位小数。
	public static String formatSolution(Double d) {
		//原理：用d取整后的值减去d，若为0 则原浮点数小数点后为零
		if (d.intValue() - d == 0) {
			return d.intValue() + "";
		} else {
			return rnd2Decm(d);
		}

	}

//传入两个可以转换成double的string, 如果两个string转换成double后差别很小，就认为两者一样。
	public static boolean areEqual(String s1, String s2){
		double d1 = Double.parseDouble(s1);
		double d2 = Double.parseDouble(s2);

		if (Math.abs(d1 - d2) < 1e-2) {
			return true;
		}
		return false;
	}


}