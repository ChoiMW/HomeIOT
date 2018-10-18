package com.example.lg01.iot_controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import java.util.Scanner;

public class PopupFindActivity extends Activity {

    private String time;
    private String channel;
    private String volume;
    private String temp;
    private String type;
    private String day;
    private String device;
    private String pattern_name;

    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_find);


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

    public void mOnClickFindOK(View v){
        Intent g_intent = getIntent();
        device=g_intent.getStringExtra("Device");


        if(device.equals("TV")){
            channel=g_intent.getStringExtra("Channel");
            volume=g_intent.getStringExtra("Volume");
        }
        else if(device.equals("Boiler")){
            temp=g_intent.getStringExtra("Temp");
        }
        else if(device.equals("CM")){
            type=g_intent.getStringExtra("Type");
        }


        time=g_intent.getStringExtra("Time");
        day=g_intent.getStringExtra("Day");


        float temp_time=Float.valueOf(time);
        int hour= (int)temp_time;
        int minutes=(int)((temp_time-hour)*60);
        time=String.valueOf(hour)+":"+String.valueOf(minutes)+":"+"00";



        EditText editText_name=(EditText)findViewById(R.id.editText_find);
        pattern_name=editText_name.getText().toString();


        mThread myThread = new mThread();
        myThread.start();



    }

    public void mOnClickFindCancel(View v){
        finish();
    }


    class mThread extends Thread {
        @Override
        public void run(){
            if(device.equals("TV")){
                try {
                    String behaviorAdd_url=getString(R.string.db_url);
                    behaviorAdd_url+="behaviorAddTV.php";
                    URL url = new URL(behaviorAdd_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    String postData = URLEncoder.encode("newpName", "UTF-8") + "=" + URLEncoder.encode(pattern_name, "UTF-8");
                    postData += "&" + URLEncoder.encode("newTime", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    postData += "&" + URLEncoder.encode("newDays", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8");
                    postData += "&" + URLEncoder.encode("power", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                    postData += "&" + URLEncoder.encode("channel", "UTF-8") + "=" + URLEncoder.encode(channel, "UTF-8");
                    postData += "&" + URLEncoder.encode("volume", "UTF-8") + "=" + URLEncoder.encode(volume, "UTF-8");
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PopupFindActivity.this,result,Toast.LENGTH_LONG).show();
                            if (result.equals("Add Success")) {
                                Intent intent = new Intent(PopupFindActivity.this,TVBehaviorlistActivity.class);
                                startActivity(intent);
                                finish();
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
            else if(device.equals("CM")){
                try {
                    String behaviorAdd_url=getString(R.string.db_url);
                    behaviorAdd_url+="behaviorAddCoffee.php";
                    URL url = new URL(behaviorAdd_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    String postData = URLEncoder.encode("newpName", "UTF-8") + "=" + URLEncoder.encode(pattern_name, "UTF-8");
                    postData += "&" + URLEncoder.encode("newTime", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    postData += "&" + URLEncoder.encode("newDays", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8");
                    postData += "&" + URLEncoder.encode("power", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                    postData += "&" + URLEncoder.encode("coffee", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PopupFindActivity.this,result,Toast.LENGTH_LONG).show();
                            if (result.equals("Add Success")) {
                                Intent intent = new Intent(PopupFindActivity.this,CMBehaviorlistActivity.class);
                                startActivity(intent);
                                finish();
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
            else if(device.equals("Boiler")){
                try {
                    String behaviorAdd_url=getString(R.string.db_url);
                    behaviorAdd_url+="behaviorAddBoiler.php";
                    URL url = new URL(behaviorAdd_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    String postData = URLEncoder.encode("newpName", "UTF-8") + "=" + URLEncoder.encode(pattern_name, "UTF-8");
                    postData += "&" + URLEncoder.encode("newTime", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    postData += "&" + URLEncoder.encode("newDays", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8");
                    postData += "&" + URLEncoder.encode("power", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                    postData += "&" + URLEncoder.encode("tmp", "UTF-8") + "=" + URLEncoder.encode(temp, "UTF-8");
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PopupFindActivity.this,result,Toast.LENGTH_LONG).show();
                            if (result.equals("Add Success")) {
                                Intent intent = new Intent(PopupFindActivity.this,BoilerBehaviorlistActivity.class);
                                startActivity(intent);
                                finish();
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
