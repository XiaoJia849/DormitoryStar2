package com.example.dormitorystar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.dormitorystar.obj.StartDate;
import com.example.dormitorystar.task.GetStartDateTask;
import com.example.dormitorystar.task.UpdateStartDateTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlanChooseCreateActivity extends AppCompatActivity implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    RadioGroup radioGroup;
    RadioButton radio1,radio2;
    int type;
    EditText date_between;
    Button startDateBtn;
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    public static final int UPDATE_START_DATE=6;
    public static final int GET_SATRT_DATE=7;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static final String TAG="PlanChooseCreateActivity";
    Handler handler;
    String dormitory_id;
    String date_start;
    int date_bw_int;
    Gson gson = new Gson();


    protected void aboutHandler(){
        handler=new Handler(Looper.myLooper()){
            @SuppressLint("LongLogTag")
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UPDATE_START_DATE:
                        Log.d(TAG, "handleMessage: 发送设置startDate消息");
                        break;
                    case GET_SATRT_DATE:
                        String s= (String) msg.obj;
                        if(s!=null){
                            StartDate startDate1= gson.fromJson(s,new TypeToken<StartDate>(){}.getType());
                            date_start=startDate1.getStart_date();
                            date_bw_int=startDate1.getBetweenDate();
                            date_between.setText(""+date_bw_int);
                            startDateBtn.setText(date_start);
                        }else{
                            date_between.setText(""+2);
                            date_bw_int=2;

                            date_start=sdf.format(new Date());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            startDateBtn.setText(sdf.format(new Date()));


                        }

                        break;


                }
            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aboutHandler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_choose_create);
        initView();
    }

    protected void initView(){
        SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
        dormitory_id=sharedPreferences.getString("dormitory_id","");


        radioGroup=findViewById(R.id.radioGroup);
        startDateBtn=findViewById(R.id.startDate);
        radio1=findViewById(R.id.plan1);
        radio2=findViewById(R.id.plan2);
        date_between=findViewById(R.id.date_between);



        GetStartDateTask getStartDateTask=new GetStartDateTask(dormitory_id);
        getStartDateTask.setHandler(handler);
        Thread thread=new Thread(getStartDateTask);
        thread.start();


        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(PlanChooseCreateActivity.this).setThemeCustom(R.style.MyCustomBetterPickersDialogs);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);

            }
        });

        date_between.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String xx=date_between.getText().toString();
                if(!xx.equals("")){
                    date_bw_int=Integer.valueOf(xx);
                }

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==radio1.getId()){
//                    选中计划1
                    type=1;
                    Log.d(TAG, "onCheckedChanged: TYPE"+1);
                }
                if(checkedId==radio2.getId()){
                    type=2;
                    Log.d(TAG, "onCheckedChanged: TYPE="+2);

                }

            }
        });
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.create_one){
//            创建一个新的计划
            Toast.makeText(PlanChooseCreateActivity.this,"敬请期待",Toast.LENGTH_LONG).show();
        }

        if(v.getId()==R.id.make_sure){
//            确认修改

//            将修改保存到本地
            ChangeSpInt("type",type);

//            将修改上传到服务器
            UpdateStartDateTask updateStartDateTask=new UpdateStartDateTask(dormitory_id,date_start,date_bw_int);
            updateStartDateTask.setHandler(handler);
            Thread thread=new Thread(updateStartDateTask);
            thread.start();

//            跳转到日历
            Intent intent=new Intent(PlanChooseCreateActivity.this,CalenderActivity.class);
            startActivity(intent);
        }
    }

    protected void ChangeSpInt(String name,int value){
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(name,value);
        editor.apply();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(PlanChooseCreateActivity.this,getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth),Toast.LENGTH_LONG).show();
        monthOfYear+=1;
        String month=monthOfYear<10?"0"+monthOfYear:""+monthOfYear;
        String day=dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth;
        date_start=""+year+"-"+month+"-"+day;
        startDateBtn.setText(""+year+"-"+month+"-"+day);
    }



}