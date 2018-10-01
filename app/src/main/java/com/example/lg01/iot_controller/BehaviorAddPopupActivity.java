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

public class BehaviorAddPopupActivity extends Activity {

    protected String behavior_pname;
    protected String behavior_dname;
    protected String behavior_days;
    protected boolean behavior_switch;
    protected String behavior_time;

    private Handler handler = new Handler();
    //이하  http://ghj1001020.tistory.com/9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_popup_add);


    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    //이상  http://ghj1001020.tistory.com/9

    public void mOnClickAddTV(View v) {

        Intent intent = new Intent(BehaviorAddPopupActivity.this, BehaviorAddTVActivity.class);
        startActivity(intent);
        //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
        TVBehaviorlistActivity endActivity = (TVBehaviorlistActivity) TVBehaviorlistActivity.Behaviorlist;
        endActivity.finish();
        finish();
    }
    public void mOnClickAddBoiler(View v){

        Intent intent = new Intent(BehaviorAddPopupActivity.this,BehaviorAddBoilerActivity.class);
        startActivity(intent);
        //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
        BoilerBehaviorlistActivity endActivity=(BoilerBehaviorlistActivity)BoilerBehaviorlistActivity.Behaviorlist;
        endActivity.finish();
        finish();
    }
    public void mOnClickAddCoffee(View v){

        Intent intent = new Intent(BehaviorAddPopupActivity.this,BehaviorAddCoffeeActivity.class);
        startActivity(intent);
        //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
        CMBehaviorlistActivity endActivity=(CMBehaviorlistActivity)CMBehaviorlistActivity.Behaviorlist;
        endActivity.finish();
        finish();
    }



}