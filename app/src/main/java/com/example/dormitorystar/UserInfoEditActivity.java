package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnTextInputConfirmListener;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;

public class UserInfoEditActivity extends AppCompatActivity implements  View.OnClickListener , CalendarDatePickerDialogFragment.OnDateSetListener {
    public static final String TAG="UserInfoEditActivity";
    com.example.dormitorystar.UserItemGroup user_nick_name,user_gender,user_birthday;
    com.example.dormitorystar.UserItemGroup user_school,user_leader,user_type;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        initView();

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


//    还需要修改服务器数据


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
//            跳转到PlanChoose Activity
            Intent intent=new Intent(UserInfoEditActivity.this,PlanChooseCreateActivity.class);
            startActivity(intent);

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