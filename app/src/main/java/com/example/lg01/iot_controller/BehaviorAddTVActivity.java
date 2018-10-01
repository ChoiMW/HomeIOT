package com.example.lg01.iot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class BehaviorAddTVActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private int behavior_new_days_add;
    private String behavior_new_pname_add;
    private String behavior_new_time_add;
    private String behavior_new_power_add;
    private String behavior_new_channel_add;
    private String behavior_new_volume_add;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_behaviomdftv);

    }

    public void mOnClickMdfComplete(View v){
        CheckBox checkBox;

        EditText editText_pname= (EditText)findViewById(R.id.editText_behavior_pname);
        EditText editText_time= (EditText)findViewById(R.id.editText_TIme);
        EditText editText_channel= (EditText)findViewById(R.id.editText_channel);
        EditText editText_volume= (EditText)findViewById(R.id.editText_volume);
        ToggleButton toggle_power=(ToggleButton)findViewById(R.id.toggleButton_tv);

        behavior_new_pname_add=editText_pname.getText().toString();
        behavior_new_time_add=editText_time.getText().toString();
        behavior_new_channel_add=editText_channel.getText().toString();
        behavior_new_volume_add=editText_volume.getText().toString();


        if(toggle_power.isChecked()){
            //켜짐
            behavior_new_power_add="1";
        }
        else{
            //꺼짐
            behavior_new_power_add="0";
            behavior_new_channel_add="0";
            behavior_new_volume_add="0";
        }





        mThread myThread = new mThread();

        checkBox=(CheckBox)findViewById(R.id.checkBox_Monday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 1;
            myThread.start();
        }

        checkBox=(CheckBox)findViewById(R.id.checkBox_Tuesday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 2;
            myThread.start();
        }

        checkBox=(CheckBox)findViewById(R.id.checkBox_Wednesday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 3;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Thursday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 4;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Friday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 5;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Saturday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 6;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Sunday);
        if(checkBox.isChecked()){
            behavior_new_days_add= 0;
            myThread.start();
        }
        if(behavior_new_days_add==8){
            Toast.makeText(this, "Please Select Days", Toast.LENGTH_SHORT).show();
        }
        else if(behavior_new_pname_add.equals("")){
            Toast.makeText(this, "Please Input Name", Toast.LENGTH_SHORT).show();
        }
        else if(behavior_new_time_add.equals("")){
            Toast.makeText(this, "Please Input Time", Toast.LENGTH_SHORT).show();
        }

    }



    public void mOnClickChannelUp(View v){
        EditText channel=(EditText)findViewById(R.id.editText_channel);
        String s=channel.getText().toString();
        int n=Integer.valueOf(s);
        n=n+1;
        String.valueOf(n);
        channel.setText(String.valueOf(n));
    }
    public void mOnClickChannelDown(View v){
        EditText channel=(EditText)findViewById(R.id.editText_channel);
        String s=channel.getText().toString();
        int n=Integer.valueOf(s);
        n=n-1;
        channel.setText(String.valueOf(n));
    }
    public void mOnClickVolumeUp(View v){
        EditText volume=(EditText)findViewById(R.id.editText_volume);
        String s=volume.getText().toString();
        int n=Integer.valueOf(s);
        n=n+1;
        volume.setText(String.valueOf(n));
    }
    public void mOnClickVolumeDown(View v){
        EditText volume=(EditText)findViewById(R.id.editText_volume);
        String s=volume.getText().toString();
        int n=Integer.valueOf(s);
        n=n-1;
        volume.setText(String.valueOf(n));
    }
    //이하 http://webnautes.tistory.com/829

    class mThread extends Thread {
        @Override
        public void run(){
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
                String postData = URLEncoder.encode("newpName", "UTF-8") + "=" + URLEncoder.encode(behavior_new_pname_add, "UTF-8");
                postData += "&" + URLEncoder.encode("newTime", "UTF-8") + "=" + URLEncoder.encode(behavior_new_time_add, "UTF-8");
                postData += "&" + URLEncoder.encode("newDays", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(behavior_new_days_add), "UTF-8");
                postData += "&" + URLEncoder.encode("power", "UTF-8") + "=" + URLEncoder.encode(behavior_new_power_add, "UTF-8");
                postData += "&" + URLEncoder.encode("channel", "UTF-8") + "=" + URLEncoder.encode(behavior_new_channel_add, "UTF-8");
                postData += "&" + URLEncoder.encode("volume", "UTF-8") + "=" + URLEncoder.encode(behavior_new_volume_add, "UTF-8");
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
                        /*
                        Toast.makeText(BehaviorAddTVActivity.this,result,Toast.LENGTH_LONG).show();
                        if (result.equals("Add Success")) {
                            //이전 BehaviorlistActivity를 종료하고
                            TVBehaviorlistActivity endActivity=(TVBehaviorlistActivity)TVBehaviorlistActivity.Behaviorlist;
                            endActivity.finish();
                            // 다시 실행한다.
                            Intent intent = new Intent(BehaviorAddTVActivity.this,TVBehaviorlistActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        */

                        if (result.equals("#!/usr/bin/phpAdd Success")) {
                            Toast.makeText(BehaviorAddTVActivity.this, "Success", Toast.LENGTH_LONG).show();
                            //이전 BehaviorlistActivity를 종료하고
                            TVBehaviorlistActivity endActivity = (TVBehaviorlistActivity) TVBehaviorlistActivity.Behaviorlist;
                            endActivity.finish();
                            // 다시 실행한다.
                            Intent intent = new Intent(BehaviorAddTVActivity.this, TVBehaviorlistActivity.class);
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
    String Result(InputStream is){
        String data="";
        Scanner s= new Scanner(is);
        while(s.hasNext())
            data+=s.nextLine();
        s.close();
        return data;
    }
    //이상 http://webnautes.tistory.com/829

}
