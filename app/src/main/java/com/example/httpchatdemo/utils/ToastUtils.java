package com.example.httpchatdemo.utils;

import android.widget.Toast;

import com.example.httpchatdemo.ApplicationVar;


public class ToastUtils {
    private static Toast toast;
    public static void showToast(String msg){
        if (toast==null){
            toast = Toast.makeText(ApplicationVar.getContext(),msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
    public static void showToast(int msg){
        if (toast==null){
            toast = Toast.makeText(ApplicationVar.getContext(),msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}
