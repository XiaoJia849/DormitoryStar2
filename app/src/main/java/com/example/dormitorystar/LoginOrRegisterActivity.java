package com.example.dormitorystar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.jsoup.Jsoup;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nickname,dormitory_id,bed_id;
    CheckBox check_notification;
    Switch leader;
    TextView what_you_need_to_know;
    Button register;
    String Str_nickname,Str_dormitory_id,Str_user_id;
    int Int_bed_id;
    Boolean Bool_leader;
    public static final String TAG="LoginOrRegisterActivity";
    Handler handler;
    public static final int GET_DATA_DORM=1;
    public static final int SEND_USER_DATA=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==GET_DATA_DORM){
                    String jsonStr= (String) msg.obj;
                    parseJSONAndSaveData(jsonStr);
                    Log.d(TAG, "handleMessage: 室友信息存储到数据库");

                    Intent intent=new Intent(LoginOrRegisterActivity.this,CalenderActivity.class);
                    startActivity(intent);


                }
                if(msg.what==SEND_USER_DATA){
                    Str_user_id= (String) msg.obj;
                    SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("user_id",Str_user_id);
                    Log.d(TAG, "handleMessage: 获取新用户的用户ID"+Str_user_id);
                }

            }
        };



        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        Str_user_id=sharedPreferences.getString("user_id","");
        if(!Str_user_id.equals("")){
//            登录,直接看卫生日历
            Str_dormitory_id=sharedPreferences.getString("dormitory_id","");

            GetDataDormTask task=new GetDataDormTask();
            task.setHandler(handler);
            task.setDormitory_id(Str_dormitory_id);
            Thread thread1=new Thread(task);
            thread1.start();

        }else {
//            注册
//            创建数据库
            LitePal.getDatabase();

            setContentView(R.layout.activity_login_or_register);
            initView();



        }

    }


    //    解析JSON,保存室友信息到数据库,将之前的用户数据删除
    protected void parseJSONAndSaveData(String json){
        Gson gson=new Gson();
        LitePal.deleteAll(User.class);
        List<User> users= gson.fromJson(json,new TypeToken<List<User>>(){}.getType());
        for(User user:users){
            if(user.getUser_id().equals(Str_user_id)){
                continue;
            }
            user.save();
        }
    }


    protected void initView(){
        nickname=findViewById(R.id.nickname);
        dormitory_id=findViewById(R.id.dormitory_id);
        bed_id=findViewById(R.id.bed_id);
        check_notification=findViewById(R.id.check_notification);
        what_you_need_to_know=findViewById(R.id.what_you_need_to_know);
        what_you_need_to_know.setOnClickListener(this);
        leader=findViewById(R.id.leader);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.what_you_need_to_know){
//            dialog打开用户须知

        }
        if(v.getId()==R.id.register){
//            注册
            Int_bed_id=Integer.valueOf(bed_id.getText().toString());
            Str_nickname=nickname.getText().toString();
            Str_dormitory_id=dormitory_id.getText().toString();
            Bool_leader=leader.isChecked();


            SendUserDataTask task=new SendUserDataTask(Str_nickname,Str_dormitory_id,Int_bed_id,Bool_leader
            );
            task.setHandler(handler);
            Thread thread1=new Thread(task);
            thread1.start();


            GetDataDormTask task1=new GetDataDormTask();
            task1.setHandler(handler);
            task1.setDormitory_id(Str_dormitory_id);
            Thread thread2=new Thread(task);
            thread2.start();



//            将user_id,等等信息用sharePreference存储
            SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("nickname",Str_nickname);
            editor.putString("dormitory_id",Str_dormitory_id);
            editor.putInt("bed_id",Int_bed_id);
            editor.putInt("type",1);
            editor.putBoolean("leader",Bool_leader);
            editor.commit();

//            跳转到下一个页面
            Intent intent=new Intent(LoginOrRegisterActivity.this,CalenderActivity.class);
            startActivity(intent);

        }



    }


}