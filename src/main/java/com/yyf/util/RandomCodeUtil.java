package com.yyf.util;

import java.util.Random;

/**
 * 生成4位数的签到码
 * */
public class RandomCodeUtil {
	
	public static String randomCode(){
		String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb=new StringBuilder(6);
		for(int i=0;i<6;i++)
		{
			char ch=str.charAt(new Random().nextInt(str.length()));
			sb.append(ch);
		}
		
		return sb.toString();
	}
}
	
