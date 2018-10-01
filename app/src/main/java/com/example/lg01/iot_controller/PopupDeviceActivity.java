package com.example.lg01.iot_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class PopupDeviceActivity extends Activity {



    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_popup_device);


    }
    //이하  http://ghj1001020.tistory.com/9
    //확인 버튼 클릭
    public void mOnClose(View v){
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    //이상  http://ghj1001020.tistory.com/9

    public void mOnClickTVBehavior(View v){
        Intent intent= new Intent();
        intent = new Intent(PopupDeviceActivity.this,TVBehaviorlistActivity.class);
        startActivity(intent);
        finish();

    }

    public void mOnClickCMBehavior(View v){
        Intent intent= new Intent();
        intent = new Intent(PopupDeviceActivity.this,CMBehaviorlistActivity.class);
        startActivity(intent);
        finish();
    }

    public void mOnClickBoilerBehavior(View v){
        Intent intent= new Intent();
        intent = new Intent(PopupDeviceActivity.this,BoilerBehaviorlistActivity.class);
        startActivity(intent);
        finish();

    }

}
