package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nickname,dormitory_id,bed_id;
    CheckBox check_notification;
    TextView what_you_need_to_know;
    Button register;
    String Str_nickname,Str_dormitory_id,Str_bed_id,Str_user_id;
// user_id是服务器分配的一个号码，当注册成功后，服务器返回user_id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        Str_user_id=sharedPreferences.getString("user_id","");
        if(!Str_user_id.equals("")){
//            登录
            Intent intent=new Intent(this,MemberLeaderElectActivity.class);
            startActivity(intent);


        }else {
//            注册
            setContentView(R.layout.activity_login_or_register);
            initView();

            Str_bed_id=bed_id.getText().toString();
            Str_nickname=nickname.getText().toString();
            Str_dormitory_id=nickname.getText().toString();

//            将信息 Str_dormitory_id,Str_bed_id发送到服务器，获取返回user_id
            Str_user_id=sendInfoToServer();


//            将user_id,等等信息用sharePreference存储
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("user_id",Str_user_id);
            editor.putString("nickname",Str_nickname);
            editor.putString("dormitory_id",Str_dormitory_id);
            editor.putString("bed_id",Str_bed_id);
            editor.commit();

//            跳转到下一个页面



        }

    }

    protected void initView(){
        nickname=findViewById(R.id.nickname);
        dormitory_id=findViewById(R.id.dormitory_id);
        bed_id=findViewById(R.id.bed_id);
        check_notification=findViewById(R.id.check_notification);
        what_you_need_to_know=findViewById(R.id.what_you_need_to_know);
        what_you_need_to_know.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.what_you_need_to_know){
//            dialog打开用户须知

        }
        if(v.getId()==R.id.register){
            Str_nickname=nickname.getText().toString();
            Str_dormitory_id=dormitory_id.getText().toString();
            Str_bed_id=bed_id.getText().toString();
        }



    }

//    将信息传给服务器，获取User_id
    public String sendInfoToServer(){
        return "";
    }
}