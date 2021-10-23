package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class UserInfoEditActivity extends AppCompatActivity implements  View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
    }
//    需要补充点击图片更换头像


//    点击展示dialog进行选择
    public void onClick(View view) {
        if(view.getId()==R.id.user_nick_name){

        }
        if(view.getId()==R.id.user_user_id){

        }
        if(view.getId()==R.id.user_gender){

        }
        if(view.getId()==R.id.user_birthday){

        }
        if(view.getId()==R.id.user_school){

        }

    }
}