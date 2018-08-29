package com.example.lg01.iot_controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity{
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw = (EditText) findViewById(R.id.new_pw);
        editTextCode = (EditText) findViewById(R.id.certification_code);

    }
    public void insert(View view) {
        String Id = editTextId.getText().toString();
        String Pw = editTextPw.getText().toString();
        String InputCode=editTextCode.getText().toString();
        String RealCode="realstrong";
        if(InputCode.equals(RealCode)==false){
            Toast.makeText(SignupActivity.this,"Code is Wrong",Toast.LENGTH_SHORT).show();
        }
        else if(Id.length()<4){
            Toast.makeText(SignupActivity.this,"ID should be longer than 4",Toast.LENGTH_SHORT).show();
        }
        else if(Pw.length()<4){
            Toast.makeText(SignupActivity.this,"PW should be longer than 4",Toast.LENGTH_SHORT).show();
        }
        else{
            insertoToDatabase(Id, Pw);
            finish();
        }
    }
    //이하 http://jake217.tistory.com/17
    private void insertoToDatabase(String Id, String Pw) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignupActivity.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];

                    //php접속주소/.php
                    //String signup_url = "http://222.110.210.122/signup.php";
                    String signup_url=getString(R.string.db_url);
                    signup_url+="signup.php";
                    String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");

                    URL url = new URL(signup_url);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, Pw);
    }
}
    //이상  http://jake217.tistory.com/17