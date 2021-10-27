package com.example.dormitorystar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnTextInputConfirmListener;
import com.example.dormitorystar.task.UpdateOneValueTask;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.json.JSONObject;

import javax.net.ssl.SNIHostName;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;

public class UserInfoEditActivity extends AppCompatActivity implements  View.OnClickListener , CalendarDatePickerDialogFragment.OnDateSetListener {
    public static final String TAG="UserInfoEditActivity";
    com.example.dormitorystar.UserItemGroup user_nick_name,user_gender,user_birthday;
    com.example.dormitorystar.UserItemGroup user_school,user_leader,user_type;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    NavigationTabStrip navigationTabStrip ;
    Handler handler;


    protected void initNavi(){
        navigationTabStrip= findViewById(R.id.navi_user);
        Log.d(TAG, "initNavi: hh");
        navigationTabStrip.setTitles(getResources().getString(R.string.calendar), getResources().getString(R.string.user));
        navigationTabStrip.setTabIndex(1, true);
        navigationTabStrip.setTitleSize(100);
//        横线的颜色
        navigationTabStrip.setStripColor(Color.RED);
        navigationTabStrip.setStripWeight(6);
        navigationTabStrip.setStripFactor(2);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
//        到assets去找,但是肯定还有其他的设置，我现在没看到效果
        navigationTabStrip.setTypeface("iconfont2.ttf");
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(Color.CYAN);
//        navigationTabStrip.setOnPageChangeListener(...);

//        这个在点击tab才有用
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                Log.d(TAG, "onStartTabSelected: ");
            }

            @Override
            public void onEndTabSelected(String title, int index) {
//index 就是页码
                if(index==0){
//                    跳转到calendar页面
                    Intent intent=new Intent(UserInfoEditActivity.this,CalenderActivity.class);
                    startActivity(intent);


                }

            }
        });

    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {





            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        initView();

        initNavi();

    }
//    初始化参数
    protected void initView(){
        user_birthday=findViewById(R.id.user_birthday);
        user_gender=findViewById(R.id.user_gender);
        user_school=findViewById(R.id.user_school);
        user_leader=findViewById(R.id.user_leader);
        user_type=findViewById(R.id.user_type);
        user_nick_name=findViewById(R.id.user_nick_name);
    }




//    需要补充点击图片更换头像



//    点击展示dialog进行选择
    public void onClick(View view) {
        if(view.getId()==R.id.user_nick_name){
            new PanterDialog(this)
                    .setHeaderBackground(R.color.purple_500)
                    .setDialogType(DialogType.INPUT)
                    .setTitle("Nick Name Edit")
                    .isCancelable(false)
                    .input("input a new Nick name",
                            "ERROR MESSAGE IF USER PUT EMPTY INPUT", new
                                    OnTextInputConfirmListener() {
                                        @Override
                                        public void onTextInputConfirmed(String text) {
                                            TextView textView= user_nick_name.getContent();
                                            textView.setText(text);
                                            user_nick_name.setContent(textView);

                                            ChangeSpString("nickname",text);
                                            UpdateOneValueTask updateOneValueTask=new UpdateOneValueTask("nickname",text,"");

                                        }
                                    })
                    .show();


        }
        if(view.getId()==R.id.user_gender){
            GenderPickerDialog dialog=new GenderPickerDialog(UserInfoEditActivity.this);
            dialog.setOnSelectingGender(new GenderPickerDialog.OnGenderSelectListener() {
                @Override
                public void onSelectingGender(String value) {
                    if(!value.equals("Male")){
                        TextView textView=user_gender.getContent();
                        textView.setText("女");
                        user_gender.setContent(textView);

                        ChangeSpBool("gender",true);
                    }else {
                        ChangeSpBool("gender",false);
                    }
                }
            });
            dialog.show();

        }
        if(view.getId()==R.id.user_birthday){
//            选择生日
            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                    .setOnDateSetListener(UserInfoEditActivity.this).setThemeCustom(R.style.MyCustomBetterPickersDialogs);
            cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);

        }
        if(view.getId()==R.id.user_school){
            new PanterDialog(this)
                    .setHeaderBackground(R.color.purple_500)
                    .setDialogType(DialogType.INPUT)
                    .setTitle("School Edit")
                    .isCancelable(false)
                    .input("input School name",
                            "ERROR MESSAGE IF USER PUT EMPTY INPUT", new
                                    OnTextInputConfirmListener() {
                                        @Override
                                        public void onTextInputConfirmed(String text) {
                                            TextView textView= user_school.getContent();
                                            textView.setText(text);
                                            user_school.setContent(textView);

                                            ChangeSpString("school",text);
                                        }
                                    })
                    .show();

        }
        if(view.getId()==R.id.user_leader){
            new PanterDialog(this)
                    .setHeaderBackground(R.color.purple_500)
                    .setPositive("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ChangeSpBool("leader",true);
                        }
                    })// You can pass also View.OnClickListener as second param
                    .setNegative("No", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ChangeSpBool("leader",false);
                        }
                    })
                    .setMessage("Are you a leader?")
                    .isCancelable(true)
                    .show();
        }
        if(view.getId()==R.id.user_type){
//            检查用户是否为寝室长，是才跳转
            SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
            Boolean leader= sharedPreferences.getBoolean("leader",false);
            if(leader){
//            跳转到PlanChoose Activity
                Intent intent=new Intent(UserInfoEditActivity.this,PlanChooseCreateActivity.class);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

        Toast.makeText(UserInfoEditActivity.this,getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth),Toast.LENGTH_LONG).show();
        TextView textView=user_birthday.getContent();
        String month=monthOfYear<10?"0"+monthOfYear:""+monthOfYear;
        String day=dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth;
        textView.setText(""+year+"-"+month+"-"+day);
        user_birthday.setContent(textView);

        ChangeSpString("birthday",""+year+"-"+month+"-"+day);

    }


//    修改本地文件
    protected void  ChangeSpString(String name,String value){
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(name,value);
        editor.apply();
    }

    protected void ChangeSpBool(String name,Boolean value){
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(name,value);
        editor.apply();
    }





}