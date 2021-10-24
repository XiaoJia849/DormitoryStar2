package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

public class PlanChooseCreateActivity extends AppCompatActivity implements View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    RadioGroup radioGroup;
    int type;
    EditText editText,date_between;
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_choose_create);
        initView();
    }

    protected void initView(){
        radioGroup=findViewById(R.id.radioGroup);
        editText=findViewById(R.id.startDate);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(PlanChooseCreateActivity.this).setThemeCustom(R.style.MyCustomBetterPickersDialogs);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.plan1){
//                    选中计划1
                    type=1;
                }
                if(checkedId==R.id.plan2){
                    type=2;

                    String date_start=editText.getText().toString();
                    int date_bw_int=Integer.valueOf(date_between.getText().toString());
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.create_one){
//            创建一个新的计划

        }

        if(v.getId()==R.id.make_sure){
//            确认修改



//            将修改保存到本地
            ChangeSpInt("type",type);

//            将修改上传到服务器


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
        String month=monthOfYear<10?"0"+monthOfYear:""+monthOfYear;
        String day=dayOfMonth<10?"0"+dayOfMonth:""+dayOfMonth;
        editText.setText(""+year+"-"+month+"-"+day);
    }
}