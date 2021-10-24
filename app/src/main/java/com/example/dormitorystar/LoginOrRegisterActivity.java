package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nickname,dormitory_id,bed_id;
    CheckBox check_notification;
    Switch leader;
    TextView what_you_need_to_know;
    Button register;
    String Str_nickname,Str_dormitory_id,Str_user_id;
    int Int_bed_id;
    Boolean Bool_leader;
    // user_id是服务器分配的一个号码，当注册成功后，服务器返回user_id
    String url="http://192.168.43.123:8081/JSONUpdate/dataRegister.jsp";
    String url1="http://192.168.43.123:8081/JSONUpdate/dataGetDorm.jsp?dormitory_id=";
    public static final String TAG="LoginOrRegisterActivity";
    

//    获取同一个寝室的所有信息
    protected  String getDataFromUrl(String url1,String dormitory_id) throws IOException {
        String strJson = Jsoup.connect(url1+dormitory_id)
                .ignoreContentType(true)
                .execute()
                .body();
        return strJson;
    }

//    解析JSON,保存室友信息到数据库
    protected void parseJSONAndSaveData(String json){
        Gson gson=new Gson();
        List<User> users= gson.fromJson(json,new TypeToken<List<User>>(){}.getType());
        for(User user:users){
            if(user.getUser_id().equals(Str_user_id)){
                continue;
            }
            user.save();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        Str_user_id=sharedPreferences.getString("user_id","");
        if(!Str_user_id.equals("")){
//            登录,直接看卫生日历
            Str_dormitory_id=sharedPreferences.getString("dormitory_id","");

            Log.d(TAG, "onCreate: 登录");

//          获取室友信息,刷新数据库
            try {
                LitePal.deleteAll(User.class);
                String json=getDataFromUrl(url1,Str_dormitory_id);
                parseJSONAndSaveData(json);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Intent intent=new Intent(this,CalenderActivity.class);
            startActivity(intent);



        }else {
//            注册
            Log.d(TAG, "onCreate: 注册");
            setContentView(R.layout.activity_login_or_register);
            initView();

//            创建数据库
            LitePal.getDatabase();


        }

    }


//    将信息传给服务器获取user_id
    public static String sendMessage(String url, JSONObject jsonObject) throws IOException {
        String new_url=url+"?json="+jsonObject.toString();
        String user_id= Jsoup.connect(new_url).ignoreContentType(true).execute().body().trim();
        return user_id;
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

//            创建用户的json
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("nickname", Str_nickname);
                jsonObject.put("dormitory_id", Str_dormitory_id);
                jsonObject.put("bed_id",Int_bed_id);
                jsonObject.put("leader",Bool_leader);
                jsonObject.put("type",1);
                jsonObject.put("birthday","");
                jsonObject.put("user_pic","");
                jsonObject.put("school","");
                jsonObject.put("gender",true);


            } catch (JSONException e) {
                e.printStackTrace();
            }


//            将信息发送到服务器，获取返回user_id
            try {
                Str_user_id=sendMessage(url,jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }


//            将user_id,等等信息用sharePreference存储
            SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("user_id",Str_user_id);
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