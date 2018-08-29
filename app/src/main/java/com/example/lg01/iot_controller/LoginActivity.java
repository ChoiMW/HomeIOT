package com.example.lg01.iot_controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextId;
    private EditText editTextPw;
    String success="Success";
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextId = (EditText) findViewById(R.id.login_id);
        editTextPw = (EditText) findViewById(R.id.login_password);

        Button login_button = (Button)findViewById(R.id.button_login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThread myThread = new mThread();
                if(editTextId.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Please Input ID",Toast.LENGTH_LONG).show();
                }
                else if(editTextPw.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Please Input PW",Toast.LENGTH_LONG).show();
                }
                else{
                    myThread.setDaemon(true);//액티비티종료시 같이 종료
                    myThread.start();
                }
            }
        });
        Button button_signup = (Button)findViewById(R.id.button_signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

    }
    String loginResult(InputStream is){
        String data="";
        Scanner s= new Scanner(is);
        while(s.hasNext())
            data+=s.nextLine();
        s.close();
        return data;
    }

    class mThread extends Thread {
        String ID=editTextId.getText().toString();
        String PW=editTextPw.getText().toString();
        @Override
        public void run(){
            try {
                String login_url=getString(R.string.db_url);
                login_url+="login.php";
                URL url = new URL(login_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control", "no-cache");
                String postData = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8");
                postData += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(PW, "UTF-8");

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
                final String result = loginResult(inputStream);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("Success")) {
                            Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else if(result.equals("pw")){
                            Toast.makeText(LoginActivity.this,"Password is wrong!",Toast.LENGTH_LONG).show();
                        }
                        else if(result.equals("id")){
                            Toast.makeText(LoginActivity.this, "No Such ID",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
    };

    private long pressedTime;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(pressedTime==0){
            Toast.makeText(LoginActivity.this, "If you press fast again, it will be end.", Toast.LENGTH_SHORT).show();
            pressedTime=System.currentTimeMillis();
        }
        else{
            int seconds=(int) (System.currentTimeMillis()-pressedTime);
            if(seconds<1500){
                //연속된 뒤로가기 버튼입력
                finish();
            }
            else{
                //초기화
                pressedTime=0;
            }
        }
    }
}
