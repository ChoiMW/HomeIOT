package com.example.lg01.iot_controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

public class BoilerBehaviorlistActivity extends AppCompatActivity {

    private static String TAG ="iot_controller_BehaviorlistActivity";

    private static final String TAG_JSON="arr";
    private static final String TAG_PNAME ="Name";
    private static final String TAG_DAYS ="DayofWeek";
    private static final String TAG_POWER="Power";
    private static final String TAG_TIME="Time";
    private static final String TAG_TEMP="Temp";
    private static final String TAG_SWITCH="Switch";
    protected Handler handle=new Handler();

    public static Activity Behaviorlist;

    private TextView mTextViewResult;


    ArrayList<MyItem> behavior_list;
    ListView MyList;
    String mJsonString;

    //이하 http://recipes4dev.tistory.com/141
    //액션바 +표시
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true ;
    }
    //액션바 클릭시 수행
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(BoilerBehaviorlistActivity.this,BehaviorAddBoilerActivity.class);
                startActivity(intent);
                return true ;

            default :
                return false;
        }
    }

    //이상 http://recipes4dev.tistory.com/141

    //이하 http://webnautes.tistory.com/829

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Behaviorlist=BoilerBehaviorlistActivity.this;
        setContentView(R.layout.activity_newbehaviorlist);
        //리스트 목록
        behavior_list = new ArrayList<MyItem>();
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        MyList = (ListView) findViewById(R.id.list_behaviorlist);
        GetData task = new GetData();
        String getjson=getString(R.string.db_url);
        getjson+="Boiler_json.php";
        task.execute(getjson);
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(BoilerBehaviorlistActivity.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                //json 결과 디버그용
                mJsonString = result;
                showResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            //list_item add part
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String pname = item.getString(TAG_PNAME);
                int days = item.getInt(TAG_DAYS);
                int power = item.getInt(TAG_POWER);
                int Switch = item.getInt(TAG_SWITCH);
                String time = item.getString(TAG_TIME);
                String temp = item.getString(TAG_TEMP);

                MyItem m_i = new MyItem(pname,temp,days,power,time,Switch);
                behavior_list.add(m_i);

            }
            MyListAdapter MyAdapter = new MyListAdapter(this,R.layout.custom_behavior_list_view, behavior_list);
            MyList.setAdapter(MyAdapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }
    //이상 http://webnautes.tistory.com/829
    @Override
    public void onResume() {
        super.onResume();
        MyListAdapter MyAdapter = new MyListAdapter(this,R.layout.custom_behavior_list_view, behavior_list);
        MyAdapter.notifyDataSetChanged();

    }

    class MyItem{
        MyItem(String aPname,String aTemp,int aDays,int aPower,String aTime,int aSwitch){
            pName=aPname;
            Temp=aTemp;
            Days=aDays;
            behavior_switches=aSwitch;
            Power=aPower;
            Time=aTime;
        }
        String pName;
        String Temp;
        int Days;
        int Power;
        int behavior_switches;
        String Time;
    }

    // 어댑터 클래스
    class MyListAdapter extends BaseAdapter
    {
        Context maincon;
        LayoutInflater Inflater;
        ArrayList<MyItem> arSrc;
        int layout;

        public MyListAdapter(Context context, int alayout, ArrayList<MyItem> aarsrc) {
            maincon = context;
            Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arSrc = aarsrc;
            layout = alayout;
        }

        public int getCount() {
            return arSrc.size();
        }
        public String getItem(int position) {
            return arSrc.get(position).pName;
        }
        public long getItemId(int position) {
            return position;
        }

        // 각 항목의 뷰 생성
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null) {
                convertView = Inflater.inflate(layout, parent, false);
            }
            // 아이템 클릭시 실행할 동작
            View.OnClickListener mOnClickListener =new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(BoilerBehaviorlistActivity.this, PopupActivity.class);
                    intent.putExtra("Behavior_pname", arSrc.get(pos).pName);
                    intent.putExtra("Behavior_dname","Boiler");
                    intent.putExtra("Behavior_temp", arSrc.get(pos).Temp);
                    intent.putExtra("Behavior_days", arSrc.get(pos).Days);
                    intent.putExtra("Behavior_power", arSrc.get(pos).Power);
                    intent.putExtra("Behavior_switch", arSrc.get(pos).behavior_switches);
                    intent.putExtra("Behavior_time", arSrc.get(pos).Time);
                    startActivity(intent);

                }
            };


            TextView txt_pname = (TextView) convertView.findViewById(R.id.text_behavior_name);
            txt_pname.setText(arSrc.get(position).pName);
            txt_pname.setOnClickListener(mOnClickListener);

            TextView txt_day = (TextView) convertView.findViewById(R.id.text_days);
            txt_day.setText(String.valueOf(arSrc.get(position).Days));
            txt_day.setOnClickListener(mOnClickListener);

            final Switch switch_b = (Switch) convertView.findViewById(R.id.switch_behavior);

            TextView txt_time = (TextView) convertView.findViewById(R.id.text_Time);
            txt_time.setText(arSrc.get(position).Time);
            txt_time.setOnClickListener(mOnClickListener);

            if((arSrc.get(position).behavior_switches)==1){
                switch_b.setChecked(true);
            }
            else{
                switch_b.setChecked(false);
            }
            switch_b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Message msg = null;
                    switch_thread mythread;

                    if(isChecked)
                    {
                        //DB behavior_switch 1로 기록필요
                        mythread= new switch_thread(arSrc.get(pos).pName,1,arSrc.get(pos).Time,arSrc.get(pos).Days);
                        mythread.setDaemon(true);//액티비티종료시 같이 종료
                        mythread.start();
                        msg=handle.obtainMessage();
                        if(msg.what==0){
                            mythread.interrupt();
                        }
                    }
                    else {
                        //DB behavior_switch 0으로 기록필요
                        mythread= new switch_thread(arSrc.get(pos).pName,0,arSrc.get(pos).Time,arSrc.get(pos).Days);
                        mythread.setDaemon(true);//액티비티종료시 같이 종료
                        mythread.start();
                        msg=handle.obtainMessage();
                        if(msg.what==0){
                            mythread.interrupt();
                        }
                    }
                }
            });

            return convertView;
        }
    }


    class switch_thread extends Thread {
        private String behavior_name_thread;
        private int behavior_switch_thread;
        private String behavior_time_thread;
        private int behavior_days_thread;
        public switch_thread(String behavior_name_thread,int behavior_switch_thread,String behavior_time, int behavior_days){
            this.behavior_name_thread=behavior_name_thread;
            this.behavior_switch_thread=behavior_switch_thread;
            this.behavior_time_thread=behavior_time;
            this.behavior_days_thread=behavior_days;
        }
        @Override
        public void run(){
            try {
                String behaviorswitch_url=getString(R.string.db_url);
                behaviorswitch_url+="behaviorlist_switch.php";
                URL url = new URL(behaviorswitch_url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(20000);
                con.setReadTimeout(20000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Cache-Control", "no-cache");
                String post = URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(behavior_name_thread, "UTF-8");
                post += "&" + URLEncoder.encode("St", "UTF-8") + "=" + behavior_switch_thread;
                post += "&" + URLEncoder.encode("Time", "UTF-8") + "=" + behavior_time_thread;
                post += "&" + URLEncoder.encode("Days", "UTF-8") + "=" + behavior_days_thread;
                OutputStream outputStream = con.getOutputStream();
                outputStream.write(post.getBytes());
                outputStream.flush();
                outputStream.close();
                InputStream inputStream;
                if (con.getResponseCode() == con.HTTP_OK) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
                final String result = Result(inputStream);
                handle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("Success")) {
                            handle.sendEmptyMessage(0);
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
