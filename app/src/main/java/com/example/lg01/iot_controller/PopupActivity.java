package com.example.lg01.iot_controller;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Scanner;

public class PopupActivity extends Activity {

    protected String behavior_pname;
    protected String behavior_dname;
    protected int behavior_days;
    protected int behavior_power;
    protected boolean behavior_switch;
    protected String behavior_time;
    protected int behavior_temp;
    protected String behavior_type;
    protected String behavior_channel;
    protected String behavior_volume;


    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_popup);

        //이전 액티비티에서 인텐트로 데이터 가져오기
        Intent intent = getIntent();
        behavior_pname = intent.getStringExtra("Behavior_pname");
        behavior_dname = intent.getStringExtra("Behavior_dname");
        behavior_days = intent.getIntExtra("Behavior_days",8);
        behavior_power = intent.getIntExtra("Behavior_power",-1);
        behavior_switch = intent.getBooleanExtra("Behavior_switch",false);
        behavior_time = intent.getStringExtra("Behavior_time");
        if(behavior_dname.equals("TV")){
            behavior_channel = intent.getStringExtra("Behavior_channel");
            behavior_volume = intent.getStringExtra("Behavior_volume");
        }
        else if(behavior_dname.equals("Boiler")){
            behavior_temp = intent.getIntExtra("Behavior_temp",-1);

        }
        else if(behavior_dname.equals("CM")){
            behavior_type = intent.getStringExtra("Behavior_type");
        }
        else{
            //에어컨일경우
        }

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

    public void mOnClickModify(View v){
        Intent intent= new Intent();
        intent.putExtra("Behavior_pname", behavior_pname);
        intent.putExtra("Behavior_dname", behavior_dname);
        intent.putExtra("Behavior_days", behavior_days);
        intent.putExtra("Behavior_power", behavior_power);
        intent.putExtra("Behavior_switch", behavior_switch);
        intent.putExtra("Behavior_time", behavior_time);

        if(behavior_dname.equals("TV")){
            intent = new Intent(PopupActivity.this,BehaviorMdfTVActivity.class);
            intent.putExtra("Behavior_channel", behavior_channel);
            intent.putExtra("Behavior_volume", behavior_volume);

        }
        else if(behavior_dname.equals("Boiler")){
            intent = new Intent(PopupActivity.this,BehaviorMdfBoilerActivity.class);
            intent.putExtra("Behavior_temp", behavior_temp);

        }
        else if(behavior_dname.equals("CM")){
            intent = new Intent(PopupActivity.this,BehaviorMdfCoffeeActivity.class);
            intent.putExtra("Behavior_type", behavior_type);

        }
        else{
            //에어컨일경우
        }

        startActivity(intent);
        finish();

    }

    public void mOnClickDelete(View v){

        mThread myThread= new mThread();
        myThread.setDaemon(true);
        myThread.start();

    }


    class mThread extends Thread {
        @Override
        public void run(){
            try {

                String behaviorDlt_url=getString(R.string.db_url);
                if(behavior_dname.equals("TV")){
                    behaviorDlt_url+="TV_behaviorDlt.php";
                }
                else if(behavior_dname.equals("Boiler")){
                    behaviorDlt_url+="Boiler_behaviorDlt.php";
                }
                else if(behavior_dname.equals("CM")){
                    behaviorDlt_url+="CM_behaviorDlt.php";
                }
                else{
                    //에어컨일경우
                    //behaviorDlt_url+="behaviorDlt.php";
                }
                URL url = new URL(behaviorDlt_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control", "no-cache");
                String postData = URLEncoder.encode("Namea", "UTF-8") + "=" + URLEncoder.encode(behavior_pname, "UTF-8");
                postData += "&" + URLEncoder.encode("Day", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(behavior_days), "UTF-8");
                postData += "&" + URLEncoder.encode("Time", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(behavior_time), "UTF-8");
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();
                InputStream inputStream;
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }
                final String result = Result(inputStream);
                //conn.disconnect();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PopupActivity.this,"Delete "+result,Toast.LENGTH_LONG).show();
                        if (result.equals("Success")) {
                            // 다시 실행한다.
                            if(behavior_dname.equals("TV")){
                                Intent intent = new Intent(PopupActivity.this,TVBehaviorlistActivity.class);
                                startActivity(intent);
                                //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
                                TVBehaviorlistActivity endActivity=(TVBehaviorlistActivity)TVBehaviorlistActivity.Behaviorlist;
                                endActivity.finish();
                                finish();
                            }
                            else if(behavior_dname.equals("Boiler")){
                                Intent intent = new Intent(PopupActivity.this,BoilerBehaviorlistActivity.class);
                                startActivity(intent);
                                //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
                                BoilerBehaviorlistActivity endActivity=(BoilerBehaviorlistActivity)BoilerBehaviorlistActivity.Behaviorlist;
                                endActivity.finish();
                                finish();
                            }
                            else if(behavior_dname.equals("CM")){
                                Intent intent = new Intent(PopupActivity.this,CMBehaviorlistActivity.class);
                                startActivity(intent);
                                //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
                                CMBehaviorlistActivity endActivity=(CMBehaviorlistActivity)CMBehaviorlistActivity.Behaviorlist;
                                endActivity.finish();
                                finish();
                            }
                            else{
                                // 에어컨일경우
                                /*
                                Intent intent = new Intent(PopupActivity.this,BehaviorlistActivity.class);
                                startActivity(intent);
                                //DB 삭제가 끝나면 이전 BehaviorlistActivity를 종료하고
                                BehaviorlistActivity endActivity=(BehaviorlistActivity)BehaviorlistActivity.Behaviorlist;
                                endActivity.finish();
                                finish();
                                */
                            }
                        }

                    }
                });
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    String Result(InputStream is){
        String data="";
        Scanner s= new Scanner(is);
        while(s.hasNext())
            data+=s.nextLine();
        s.close();
        return data;
    }

}
