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
    com.example.dormitorystar.UserItemGroup user_school,user_leader,user_type,user_id;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    public static final int UPDATE_ONE_VALUE=4;

    NavigationTabStrip navigationTabStrip ;
    Handler handler;
    String Str_user_id="";


    protected void initNavi(){
        navigationTabStrip= findViewById(R.id.navi_user);

        navigationTabStrip.setTitles(getResources().getString(R.string.calendar), getResources().getString(R.string.user));
        navigationTabStrip.setTabIndex(1, true);
        navigationTabStrip.setTitleSize(100);
//        横线的颜色
        navigationTabStrip.setStripColor(getResources().getColor(R.color.royalblue));
        navigationTabStrip.setStripWeight(6);
        navigationTabStrip.setStripFactor(2);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
//        到assets去找,但是肯定还有其他的设置，我现在没看到效果
        navigationTabStrip.setTypeface("iconfont2.ttf");
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(getResources().getColor(R.color.cornflowerblue));
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
                switch (msg.what){
                    case UPDATE_ONE_VALUE:
                        String str= (String) msg.obj;
                        Toast.makeText(UserInfoEditActivity.this,str,Toast.LENGTH_LONG).show();

                        break;


                }

            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        initView();

        initNavi();

    }

//    初始化参数
    protected void initView(){
        SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
        String Str_birthday=sharedPreferences.getString("birthday","");
        String Str_school=sharedPreferences.getString("school","");
        String Str_nickname=sharedPreferences.getString("nickname","");
        Str_user_id=sharedPreferences.getString("user_id","");
        Boolean bool_leader=sharedPreferences.getBoolean("leader",false);
        Boolean bool_gender=sharedPreferences.getBoolean("gender",true);
        int int_type=sharedPreferences.getInt("type",1);



        user_birthday=findViewById(R.id.user_birthday);
        user_school=findViewById(R.id.user_school);
        user_nick_name=findViewById(R.id.user_nick_name);
        user_id=findViewById(R.id.user_user_id);

        user_gender=findViewById(R.id.user_gender);
        user_leader=findViewById(R.id.user_leader);

        user_type=findViewById(R.id.user_type);

        initIntem(user_birthday,Str_birthday);
        initIntem(user_school,Str_school);
        initIntem(user_nick_name,Str_nickname);
        initIntem(user_id,Str_user_id);
        initIntem(user_gender,bool_gender);
        initIntem(user_leader,bool_leader);
        initIntem(user_type,int_type);
    }


    protected void initIntem(com.example.dormitorystar.UserItemGroup itemGroup,Object obj){
        TextView textView=itemGroup.getContent();
        textView.setText(""+obj);
        itemGroup.setContent(textView);
    }


    protected void sendUpdateOneValue(String name,String value,String type,String user_id){
        UpdateOneValueTask updateOneValueTask=new UpdateOneValueTask(name,value,type,user_id);
        updateOneValueTask.setHandler(handler);
        Thread thread=new Thread(updateOneValueTask);
        thread.start();
    }



//    需要补充点击图片更换头像



//    点击展示dialog进行选择
    public void onClick(View view) {
        if(view.getId()==R.id.user_nick_name){
            new PanterDialog(this)
                    .setHeaderBackground(R.drawable.pattern_bg_blue)
                    .setDialogType(DialogType.INPUT)
                    .setTitle(getResources().getString(R.string.nicknameEdit))
                    .isCancelable(true)
                    .setPositive(getResources().getString(R.string.ok))
                    .input(getResources().getString(R.string.nicknameEditInfo),
                            getResources().getString(R.string.nicknameEditError), new
                                    OnTextInputConfirmListener() {
                                        @Override
                                        public void onTextInputConfirmed(String text) {
                                            TextView textView= user_nick_name.getContent();
                                            textView.setText(text);
                                            user_nick_name.setContent(textView);

                                            ChangeSpString("nickname",text);

                                            sendUpdateOneValue("nickname",text,"String",Str_user_id);

                                        }
                                    })
                    .show();


        }
        if(view.getId()==R.id.user_gender){
            GenderPickerDialog dialog=new GenderPickerDialog(UserInfoEditActivity.this);
            dialog.setOnSelectingGender(new GenderPickerDialog.OnGenderSelectListener() {
                @Override
                public void onSelectingGender(String value) {
                    if(!value.equals(getResources().getString(R.string.MPD_male))){
                        TextView textView=user_gender.getContent();
                        textView.setText(getResources().getString(R.string.MPD_female));
                        user_gender.setContent(textView);

                        ChangeSpBool("gender",true);

                        sendUpdateOneValue("gender","true","Boolean",Str_user_id);

                    }else {
                        ChangeSpBool("gender",false);

                        sendUpdateOneValue("gender","false","Boolean",Str_user_id);
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
                    .setHeaderBackground(R.drawable.pattern_bg_blue)
                    .setDialogType(DialogType.INPUT)
                    .setTitle(getResources().getString(R.string.SchoolEdit))
                    .isCancelable(false)
                    .input(getResources().getString(R.string.SchoolEditInfo),
                            getResources().getString(R.string.SchoolEditError), new
                                    OnTextInputConfirmListener() {
                                        @Override
                                        public void onTextInputConfirmed(String text) {
                                            TextView textView= user_school.getContent();
                                            textView.setText(text);
                                            user_school.setContent(textView);

                                            ChangeSpString("school",text);

                                            sendUpdateOneValue("school",text,"String",Str_user_id);

                                        }
                                    })
                    .show();

        }
        if(view.getId()==R.id.user_leader){
            new PanterDialog(this)
                    .setHeaderBackground(R.drawable.pattern_bg_blue)
                    .setPositive(getResources().getString(R.string.yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ChangeSpBool("leader",true);
                            sendUpdateOneValue("leader","true","Boolean",Str_user_id);

                        }
                    })// You can pass also View.OnClickListener as second param
                    .setNegative(getResources().getString(R.string.no), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ChangeSpBool("leader",false);
                            sendUpdateOneValue("leader","true","Boolean",Str_user_id);

                        }
                    })
                    .setMessage(getResources().getString(R.string.LeaderInfo))
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
        sendUpdateOneValue("birthday",""+year+"-"+month+"-"+day,"String",Str_user_id);

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