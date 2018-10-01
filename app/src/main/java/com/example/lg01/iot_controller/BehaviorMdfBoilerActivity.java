package com.example.lg01.iot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.StringTokenizer;

public class BehaviorMdfBoilerActivity extends AppCompatActivity{

    protected String behavior_current_pname;
    protected int behavior_current_days;
    protected boolean behavior_current_power;
    protected String behavior_current_time;
    protected int behavior_current_temp;

    protected String behavior_new_pname;
    protected int behavior_new_days=8;
    protected String behavior_new_time;
    protected String behavior_new_power;
    protected String behavior_new_temperature;

    protected Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behaviomdfboiler);
        //이전 액티비티에서 인텐트로 데이터 가져오기
        Intent intent = getIntent();
        CheckBox checkBox = null;
        ToggleButton toggle_power=(ToggleButton)findViewById(R.id.toggleButton_boiler);

        /*
        toggle_power.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ImageButton chanenel_up=(ImageButton)findViewById(R.id.button_channel_up);
                ImageButton chanenel_down=(ImageButton)findViewById(R.id.button_channel_up);
                EditText channel=(EditText)findViewById(R.id.editText_channel);

                ImageButton volume_up=(ImageButton)findViewById(R.id.button_volume_up);
                ImageButton volume_down=(ImageButton)findViewById(R.id.button_volume_down);
                EditText volume=(EditText)findViewById(R.id.editText_volume);

                if(isChecked){
                    //켜지는변화감지
                    chanenel_up.setClickable(true);
                    chanenel_down.setClickable(true);
                    channel.setClickable(true);

                    volume_up.setClickable(true);
                    volume_down.setClickable(true);
                    volume.setClickable(true);
                }
                else{
                    //꺼지는 변화 감지
                    channel.setText("0");
                    chanenel_up.setClickable(false);
                    chanenel_down.setClickable(false);
                    channel.setClickable(false);

                    volume.setText("0");
                    volume_up.setClickable(false);
                    volume_down.setClickable(false);
                    volume.setClickable(false);

                }
            }
        });
        */

        behavior_current_pname = intent.getStringExtra("Behavior_pname");
        behavior_current_days = intent.getIntExtra("Behavior_days",8);
        behavior_current_power = intent.getBooleanExtra("Behavior_power",false);
        behavior_current_time = intent.getStringExtra("Behavior_time");
        behavior_current_temp = intent.getIntExtra("Behavior_temp",-1);


        //현재 이름 올리기
        EditText editText_pname= (EditText)findViewById(R.id.editText_behavior_pname);
        editText_pname.setText(behavior_current_pname);

        //현재 시간 체크
        EditText editText_time= (EditText)findViewById(R.id.editText_TIme);
        editText_time.setText(behavior_current_time);

        //현재 온도 체크
        EditText editText_temp= (EditText)findViewById(R.id.editText_temperature);
        editText_temp.setText(behavior_current_temp);

        //현재 파워 체크
        ToggleButton button_power= (ToggleButton)findViewById(R.id.toggleButton_boiler);
        if(behavior_current_power){
            button_power.setChecked(true);
        }
        else{
            button_power.setChecked(false);
        }


        //현재 요일 체크하기
            if(behavior_current_days==1){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Monday);
            }
            else if(behavior_current_days==2){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Tuesday);
            }
            else if(behavior_current_days==3){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Wednesday);
            }
            else if(behavior_current_days==4){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Thursday);
            }
            else if(behavior_current_days==5){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Friday);
            }
            else if(behavior_current_days==6){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Saturday);
            }
            else if(behavior_current_days==0){
                checkBox=(CheckBox)findViewById(R.id.checkBox_Sunday);
            }
            checkBox.setChecked(true);
        }




    //사용자가 어떠한 작업을해서 완료 버튼을 누른다.
    public void mOnClickMdfComplete(View v) throws InterruptedException {
        CheckBox checkBox;

        EditText editText_pname= (EditText)findViewById(R.id.editText_behavior_pname);
        EditText editText_time= (EditText)findViewById(R.id.editText_TIme);
        ToggleButton toggle_power=(ToggleButton)findViewById(R.id.toggleButton_boiler);
        EditText editText_temperature= (EditText)findViewById(R.id.editText_temperature);

        behavior_new_pname=editText_pname.getText().toString();
        behavior_new_time=editText_time.getText().toString();
        behavior_new_temperature=editText_temperature.getText().toString();


        if(toggle_power.isChecked()){
            //켜짐
            behavior_new_power="1";
        }
        else{
            //꺼짐
            behavior_new_power="0";
            behavior_new_temperature="0";
        }



        mThread myThread = new mThread();

        checkBox=(CheckBox)findViewById(R.id.checkBox_Monday);
        if(checkBox.isChecked()){
            behavior_new_days= 1;
            myThread.start();
        }

        checkBox=(CheckBox)findViewById(R.id.checkBox_Tuesday);
        if(checkBox.isChecked()){
            behavior_new_days= 2;
            myThread.start();
        }

        checkBox=(CheckBox)findViewById(R.id.checkBox_Wednesday);
        if(checkBox.isChecked()){
            behavior_new_days= 3;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Thursday);
        if(checkBox.isChecked()){
            behavior_new_days= 4;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Friday);
        if(checkBox.isChecked()){
            behavior_new_days= 5;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Saturday);
        if(checkBox.isChecked()){
            behavior_new_days= 6;
            myThread.start();
        }
        checkBox=(CheckBox)findViewById(R.id.checkBox_Sunday);
        if(checkBox.isChecked()){
            behavior_new_days= 0;
            myThread.start();
        }
        if(behavior_new_days==8){
            Toast.makeText(this, "Please Select Days", Toast.LENGTH_SHORT).show();
        }
        else if(behavior_new_pname.equals("")){
            Toast.makeText(this, "Please Input Name", Toast.LENGTH_SHORT).show();
        }
        else if(behavior_new_time.equals("")){
            Toast.makeText(this, "Please Input Time", Toast.LENGTH_SHORT).show();
        }

    }



    public void mOnClickTemperaturelUp(View v){
        EditText channel=(EditText)findViewById(R.id.editText_temperature);
        String s=channel.getText().toString();
        int n=Integer.valueOf(s);
        n=n+1;
        String.valueOf(n);
        channel.setText(String.valueOf(n));
    }
    public void mOnClickTemperatureDown(View v){
        EditText channel=(EditText)findViewById(R.id.editText_temperature);
        String s=channel.getText().toString();
        int n=Integer.valueOf(s);
        n=n-1;
        channel.setText(String.valueOf(n));
    }


    //이하 http://webnautes.tistory.com/829

    class mThread extends Thread {
        @Override
        public void run(){
            try {

                String behaviorMdf_url=getString(R.string.db_url);
                behaviorMdf_url+="behaviorMdfBoiler.php";
                //URL url = new URL("http://222.110.210.122/login.php");
                URL url = new URL(behaviorMdf_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control", "no-cache");
                String postData = URLEncoder.encode("pName", "UTF-8") + "=" + URLEncoder.encode(behavior_current_pname, "UTF-8");
                postData += "&" + URLEncoder.encode("oldTime", "UTF-8") + "=" + URLEncoder.encode(behavior_current_time, "UTF-8");
                postData += "&" + URLEncoder.encode("oldDays", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(behavior_current_days), "UTF-8");
                postData += "&" + URLEncoder.encode("newpName", "UTF-8") + "=" + URLEncoder.encode(behavior_new_pname, "UTF-8");
                postData += "&" + URLEncoder.encode("newTime", "UTF-8") + "=" + URLEncoder.encode(behavior_new_time, "UTF-8");
                postData += "&" + URLEncoder.encode("newDays", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(behavior_new_days), "UTF-8");
                postData += "&" + URLEncoder.encode("power", "UTF-8") + "=" + URLEncoder.encode(behavior_new_power, "UTF-8");
                postData += "&" + URLEncoder.encode("temperature", "UTF-8") + "=" + URLEncoder.encode(behavior_new_temperature, "UTF-8");
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

                        Toast.makeText(BehaviorMdfBoilerActivity.this,result,Toast.LENGTH_LONG).show();
                        if (result.equals("Modify Success")) {
                            //이전 BehaviorlistActivity를 종료하고
                            BoilerBehaviorlistActivity endActivity=(BoilerBehaviorlistActivity)BoilerBehaviorlistActivity.Behaviorlist;
                            endActivity.finish();
                            // 다시 실행한다.
                            Intent intent = new Intent(BehaviorMdfBoilerActivity.this,BoilerBehaviorlistActivity.class);
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
