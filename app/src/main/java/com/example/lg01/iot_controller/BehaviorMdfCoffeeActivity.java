package com.example.lg01.iot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class BehaviorMdfCoffeeActivity extends AppCompatActivity{

    protected String behavior_current_pname;
    protected int behavior_current_days;
    protected boolean behavior_current_switch;
    protected String behavior_current_time;
    protected String behavior_current_type;

    protected String behavior_new_pname;
    protected int behavior_new_days;
    protected String behavior_new_time;
    protected String behavior_new_power;
    protected String behavior_new_coffee;

    protected Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behaviomdfcoffee);
        //이전 액티비티에서 인텐트로 데이터 가져오기

        Intent intent = getIntent();
        behavior_current_pname = intent.getStringExtra("Behavior_pname");
        behavior_current_days = intent.getIntExtra("Behavior_days",8);
        behavior_current_switch = intent.getBooleanExtra("Behavior_switch",false);
        behavior_current_time = intent.getStringExtra("Behavior_time");
        behavior_current_type = intent.getStringExtra("Behavior_type");
        CheckBox checkBox = null;
        ToggleButton toggle_power=(ToggleButton)findViewById(R.id.toggleButton_coffee);
        Spinner spinner=(Spinner)findViewById(R.id.spinner_coffee);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.coffee,android.R.layout.simple_spinner_item);

        if(behavior_current_type.equals("Americano")){
            spinner.setSelection(0);
        }
        else if(behavior_current_type.equals("Mocha")){
            spinner.setSelection(1);

        }
        else if(behavior_current_type.equals("Latte")){
            spinner.setSelection(2);

        }
        else if(behavior_current_type.equals("Espresso")){
            spinner.setSelection(3);

        }
        else if(behavior_current_type.equals("Cappuccino")){
            spinner.setSelection(4);

        }
        spinner.setAdapter(adapter);




        //현재 이름 올리기
        EditText editText_pname= (EditText)findViewById(R.id.editText_behavior_pname);
        editText_pname.setText(behavior_current_pname);

        //현재 시간 체크
        EditText editText_time= (EditText)findViewById(R.id.editText_TIme);
        editText_time.setText(behavior_current_time);


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
    public void mOnClickMdfComplete(View v){
        CheckBox checkBox;

        EditText editText_pname= (EditText)findViewById(R.id.editText_behavior_pname);
        EditText editText_time= (EditText)findViewById(R.id.editText_TIme);
        ToggleButton toggle_power=(ToggleButton)findViewById(R.id.toggleButton_coffee);
        Spinner spinner=(Spinner)findViewById(R.id.spinner_coffee);

        behavior_new_pname=editText_pname.getText().toString();
        behavior_new_time=editText_time.getText().toString();
        behavior_new_coffee=spinner.getSelectedItem().toString();

        if(toggle_power.isChecked()){
            //켜짐
            behavior_new_power="1";
        }
        else{
            //꺼짐
            behavior_new_power="0";

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



    //이하 http://webnautes.tistory.com/829

    class mThread extends Thread {
        @Override
        public void run(){
            try {

                String behaviorMdf_url=getString(R.string.db_url);
                behaviorMdf_url+="behaviorMdfCoffee.php";
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
                postData += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(behavior_new_coffee, "UTF-8");
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

                        Toast.makeText(BehaviorMdfCoffeeActivity.this,result,Toast.LENGTH_LONG).show();
                        if (result.equals("Modify Success")) {
                            //이전 BehaviorlistActivity를 종료하고
                            CMBehaviorlistActivity endActivity=(CMBehaviorlistActivity)CMBehaviorlistActivity.Behaviorlist;
                            endActivity.finish();
                            // 다시 실행한다.
                            Intent intent = new Intent(BehaviorMdfCoffeeActivity.this,CMBehaviorlistActivity.class);
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
