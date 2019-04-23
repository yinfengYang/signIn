package com.yyf.util;

public class MainClass {
    public static void main(String[] args){
        String uuid = null;
        for(int i=0;i<1000;i++){
            System.out.println(RandomCodeUtil.randomCode());
        }


    }
}
