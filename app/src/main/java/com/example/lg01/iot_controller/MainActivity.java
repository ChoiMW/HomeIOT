package com.example.lg01.iot_controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button_controliot;
    Button button_behaviorlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_controliot=(Button)findViewById(R.id.button_controliot);
        button_behaviorlist=(Button)findViewById(R.id.button_behaviorlist);

        button_controliot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ControliotActivity.class);
                startActivity(intent);
            }
        });
        button_behaviorlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PopupDeviceActivity.class);
                startActivity(intent);
            }
        });

    }
    private long pressedTime;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(pressedTime==0){
            Toast.makeText(MainActivity.this, "If you press again, you will be logged out.", Toast.LENGTH_SHORT).show();
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
