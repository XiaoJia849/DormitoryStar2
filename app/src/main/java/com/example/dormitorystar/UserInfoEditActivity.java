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
import com.eminayar.panter.enums.Animation;
import com.eminayar.panter.interfaces.OnTextInputConfirmListener;
import com.example.dormitorystar.obj.User;
import com.example.dormitorystar.task.UpdateOneValueTask;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.json.JSONObject;
import org.litepal.LitePal;

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
//    记录今天轮到哪位值日
    int turn_id=0;

    User user=new User();

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
                    finish();
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
//                        Toast.makeText(UserInfoEditActivity.this,str,Toast.LENGTH_LONG).show();

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
        String Str_dormitory_id=sharedPreferences.getString("dormitory_id","");
        int int_bed_id=sharedPreferences.getInt("bed_id",1);

        user.setDormitory_id(Str_dormitory_id);
        user.setUser_id(Str_user_id);
        user.setUser_pic("");
        user.setBirthday(Str_birthday);
        user.setGender(bool_gender);
        user.setLeader(bool_leader);
        user.setType(int_type);
        user.setNickname(Str_nickname);
        user.setSchool(Str_school);
        user.setBed_id(int_bed_id);



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
        if(bool_gender){
            initIntem(user_gender,getResources().getString(R.string.MPD_female));
        }else {
            initIntem(user_gender,getResources().getString(R.string.MPD_male));

        }
        if(bool_leader){
            initIntem(user_leader,getResources().getString(R.string.yes));
        }else {
            initIntem(user_leader,getResources().getString(R.string.no));
        }
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
                                            user.setNickname(text);
                                            user.updateAll("user_id = ?",user.getUser_id());
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
                        initIntem(user_gender,getResources().getString(R.string.MPD_female));
                        ChangeSpBool("gender",true);
                        user.setGender(true);
                        user.updateAll("user_id = ?",user.getUser_id());
                        sendUpdateOneValue("gender","true","Boolean",Str_user_id);

                    }else {
                        initIntem(user_gender,getResources().getString(R.string.MPD_male));
                        ChangeSpBool("gender",false);
                        user.setGender(false);
                        user.updateAll("user_id = ?",user.getUser_id());
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
                    .isCancelable(true)
                    .input(getResources().getString(R.string.SchoolEditInfo),
                            getResources().getString(R.string.SchoolEditError), new
                                    OnTextInputConfirmListener() {
                                        @Override
                                        public void onTextInputConfirmed(String text) {
                                            TextView textView= user_school.getContent();
                                            textView.setText(text);
                                            user_school.setContent(textView);

                                            ChangeSpString("school",text);
                                            user.setSchool(text);
                                            user.updateAll("user_id = ?",user.getUser_id());
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
                            initIntem(user_leader,getResources().getString(R.string.yes));
                            user.setLeader(true);
                            user.updateAll("user_id = ?",user.getUser_id());
                            sendUpdateOneValue("leader","true","Boolean",Str_user_id);

                        }
                    })// You can pass also View.OnClickListener as second param
                    .setNegative(getResources().getString(R.string.no), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initIntem(user_leader,getResources().getString(R.string.no));
                            ChangeSpBool("leader",false);
                            user.setLeader(false);
                            user.updateAll("user_id = ?",user.getUser_id());
                            sendUpdateOneValue("leader","true","Boolean",Str_user_id);
                        }
                    })
                    .withAnimation(Animation.POP)
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

//        Toast.makeText(UserInfoEditActivity.this,getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth),Toast.LENGTH_LONG).show();
        monthOfYear+=1;
        String month=monthOfYear<10?"0"+monthOfYear:""+monthOfYear;
        String day=dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth;

        String bbb=""+year+"-"+month+"-"+day;
        initIntem(user_birthday,bbb);

        user.setBirthday(bbb);
        user.updateAll("user_id = ?",user.getUser_id());

        ChangeSpString("birthday",bbb);
        sendUpdateOneValue("birthday",bbb,"String",Str_user_id);

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