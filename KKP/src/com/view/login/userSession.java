package com.view.login;

public class userSession {
    private static String kd;
    
    public static void setUserLogin(String kode){
        kd = kode;
    }
    public static String getUserLogin(){
        return kd;
    }
}
