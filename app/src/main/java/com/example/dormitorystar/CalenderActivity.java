package com.example.dormitorystar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dormitorystar.obj.Done;
import com.example.dormitorystar.obj.User;
import com.example.dormitorystar.task.GetDataDoneOneDayTask;
import com.example.dormitorystar.task.GetDataDoneTask;
import com.example.dormitorystar.task.SendDataDoneTask;
import com.example.dormitorystar.utilcalendarview.CompactCalendarView;
import com.example.dormitorystar.utilcalendarview.domain.Event;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


//根据type进行变动
public class CalenderActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG="CalenderActivity";
    public static final int GET_DATA_DONE=0;
    public static final int GET_DATA_DORM=1;
    public static final int SEND_DATA_DONE=2;
    public static final int UPDATE_START_DATE=6;

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    NavigationTabStrip navigationTabStrip ;
    Handler handler;

    TextView zeroIcon,threeIcon,fourIcon,fiveIcon;
    TextView zeroIconText,threeIconText,fourIconText,fiveIconText;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    String startDate;
    Gson gson=new Gson();

//    室友信息
    List<Done> dones;
    List<User> users;

//    记录寝室人数
    int num;


    int type=0;

    protected void initNavi(){
        navigationTabStrip= findViewById(R.id.navi_calendar);
        Log.d(TAG, "initNavi: hh");
        navigationTabStrip.setTitles(getResources().getString(R.string.calendar), getResources().getString(R.string.user));
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setTitleSize(100);
        navigationTabStrip.setStripColor(getResources().getColor(R.color.royalblue));
        navigationTabStrip.setStripWeight(6);
        navigationTabStrip.setStripFactor(2);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        navigationTabStrip.setTypeface("iconfont2.ttf");
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(getResources().getColor(R.color.cornflowerblue));
//        navigationTabStrip.setOnPageChangeListener(...);

        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                Log.d(TAG, "onStartTabSelected: ");
            }

            @Override
            public void onEndTabSelected(String title, int index) {
                if(index==1){
//                    跳转到user页面
                    Intent intent=new Intent(CalenderActivity.this,UserInfoEditActivity.class);
                    startActivity(intent);
//                    把这个页面的资源释放了,之后再说


                }

            }
        });

    }

    protected void initCalendar(){
        compactCalendarView=findViewById(R.id.calendar);
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        compactCalendarView.setDayColumnNames(new String[]{"Mon","Tue","Wen","Thu","Fri","Sat","Sun"});


    }

    protected void initView(){
        zeroIcon=findViewById(R.id.zeroIcon);
        threeIcon=findViewById(R.id.threeIcon);
        fourIcon=findViewById(R.id.fourIcon);
        fiveIcon=findViewById(R.id.fiveIcon);

        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");

        zeroIcon.setTypeface(font);
        zeroIcon.setText(getResources().getString(R.string.zero));

        threeIcon.setTypeface(font);
        threeIcon.setText(getResources().getString(R.string.three));

        fourIcon.setTypeface(font);
        fourIcon.setText(getResources().getString(R.string.four));

        fiveIcon.setTypeface(font);
        fiveIcon.setText(getResources().getString(R.string.five));




        for(User user:users){
            switch (user.getBed_id()){
                case 1:
                    zeroIconText.setText(user.getNickname());
                    break;
                case 2:
                    threeIconText.setText(user.getNickname());
                    break;
                case 3:
                    fourIconText.setText(user.getNickname());
                    break;
                case 4:
                    fiveIconText.setText(user.getNickname());
                    break;
            }
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case GET_DATA_DONE:
//                        保存done信息到数据库
                        String strJSON= (String) msg.obj;
                        Log.d(TAG, "handleMessage: JSON"+strJSON);
                        List<Done> dones1= gson.fromJson(strJSON,new TypeToken<List<Done>>(){}.getType());
                        for(Done done:dones1){
                            done.save();
                        }

                        num--;
                        if(num==0){
                            //        根据Done绘制events
                            Log.d(TAG, "handleMessage: 根据Done 绘制图");
                            try {
                                createDoneEvents();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case SEND_DATA_DONE:
                        break;

                    case UPDATE_START_DATE:
                        startDate= (String) msg.obj;
                }


            }
        };



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

//        获取室友个人信息
        users=LitePal.findAll(User.class);
        initNavi();
        initView();
        initCalendar();

//        获取室友+自己done 历史所有信息
        getDataFromUrl();


        SharedPreferences sharedPreferences=getSharedPreferences("User",Activity.MODE_PRIVATE);
        type=sharedPreferences.getInt("type",1);
        if(type==1){
            Plan1();
        }
        if(type==2){
            try {
                Plan2("2021-10-02",2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        compactCalendarView.invalidate();




    }


//    4个人 设置开始年月日 和间隔时间
    protected void Plan2(String dateStart,int dateBwteen) throws ParseException {
//        获取那一天到今天的距离
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date dateS=sdf.parse(dateStart);

        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        Date dateF=sdf.parse(sdf.format(firstDayOfMonth));
        long day=(dateF.getTime()-dateS.getTime())/24/60/60/1000;
        int netday= (int) (day%(dateBwteen*4));

        int max_day_of_month = currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH);	//max_day_of_month=31

        for (int i = 0; i < max_day_of_month; i++) {
            currentCalender.setTime(firstDayOfMonth);
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();
            List<Event> events=new ArrayList<>();

            if(netday==0){
//                一号
                Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "1");
                event.setBed_id(1);
                event.setHasDone(false);
                events=Arrays.asList(event);
            }
            if(netday==dateBwteen){
                Event event1=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                event1.setBed_id(2);
                event1.setHasDone(false);
                events=Arrays.asList(event1);
            }
            if(netday==2*dateBwteen){
                Event event2=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                event2.setBed_id(3);
                event2.setHasDone(false);
                events=Arrays.asList(event2);
            }
            if(netday==3*dateBwteen){
                Event event3=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                event3.setBed_id(4);
                event3.setHasDone(false);
                events=Arrays.asList(event3);
            }
            netday=(netday+1)%(dateBwteen*4);

            compactCalendarView.addEvents(events);
        }


    }



//    四个人 1：1   3：2     5：3     7：4
    protected void Plan1(){
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        int max_day_of_month = currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH);	//max_day_of_month=31

        for (int i = 0; i < max_day_of_month; i++) {
            currentCalender.setTime(firstDayOfMonth);
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();
            List<Event> events=new ArrayList<>();
//          判断这一天星期几
            switch (currentCalender.get(Calendar.DAY_OF_WEEK)){
                case Calendar.MONDAY:
                    Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "1");
                    event.setBed_id(1);
                    event.setHasDone(false);
                    events=Arrays.asList(event);
                    break;
                case Calendar.WEDNESDAY:
                    Event event1=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                    event1.setBed_id(2);
                    event1.setHasDone(false);
                    events=Arrays.asList(event1);
                    break;
                case Calendar.FRIDAY:
                    Event event2=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                    event2.setBed_id(3);
                    event2.setHasDone(false);
                    events=Arrays.asList(event2);
                    break;
                case Calendar.SUNDAY:
                    Event event3=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                    event3.setBed_id(4);
                    event3.setHasDone(false);
                    events=Arrays.asList(event3);
                    break;
                default:
                    break;
            }

            compactCalendarView.addEvents(events);
        }


    }




    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }



//    把这个方法做成定时器。每隔5分钟就向服务器发送请求，只要求获取今天的数据
//    5*60*1000
    protected void getDataTody(){
        String date=sdf.format(new Date());

        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                向服务器发送请求获取今天的数据
            for(User user:users){
                GetDataDoneOneDayTask getDataDoneOneDayTask =new GetDataDoneOneDayTask(user.getUser_id(),date);
                getDataDoneOneDayTask.setHandler(handler);
                Thread thread=new Thread(getDataDoneOneDayTask);
                thread.start();
            }

            }
        },0,300000);

    }


//    从服务器获取 一个寝室所有同学的 json Str 关于是否做了值日的历史数据。是所有的历史数据
    protected void getDataFromUrl(){
        num=2;
//        for(User user:users){
//            GetDataDoneTask getDataDoneTask=new GetDataDoneTask(user.getUser_id());
//            getDataDoneTask.setHandler(handler);
//            Thread thread=new Thread(getDataDoneTask);
//            thread.start();
//        }
//        做个测试
        String []userS={"000001","000002"};
        for(String s:userS){
            GetDataDoneTask getDataDoneTask=new GetDataDoneTask(s);
            getDataDoneTask.setHandler(handler);
            Thread thread=new Thread(getDataDoneTask);
            thread.start();
        }


    }

//  从数据库找到日期在这个月第一天之后的数据。
    protected List<Done> findDonesAfterFirstDayOfMonth(){
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();

        List<Done> dones=LitePal.where("date between ? and ?",sdf.format(firstDayOfMonth),sdf.format(new Date())).find(Done.class);

        return dones;
    }


//    遍历这些数据制作Events
    protected void createDoneEvents() throws ParseException {
        List<Done> dones= findDonesAfterFirstDayOfMonth();
        List<Event> events=new ArrayList<>();
        for(Done done:dones){
            currentCalender.setTime(sdf.parse(done.getDate()));
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();

            Log.d(TAG, "createDoneEvents: "+done.getDate()+""+done.isDone());
            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "没啥可以说的");
            event.setBed_id(done.getBed_id());
            event.setHasDone(done.isDone());
            events.add(event);
        }
        compactCalendarView.addEvents(events);
    }


//    做了今天的值日
    @Override
    public void onClick(View v) {
//        获取用户信息
        SharedPreferences sharedPreferences=getSharedPreferences("User", Activity.MODE_PRIVATE);
        int bed_id=sharedPreferences.getInt("bed_id",0);
        String user_id=sharedPreferences.getString("user_id","");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date= sdf.format(new Date());


//        把今天的图标颜色改变
        currentCalender.setTime(new Date());
        setToMidnight(currentCalender);
        long timeInMillis = currentCalender.getTimeInMillis();
        List<Event> events=new ArrayList<>();
        Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "1");
        event.setBed_id(bed_id);
        event.setHasDone(true);
        events=Arrays.asList(event);
        compactCalendarView.addEvents(events);


//        把本地数据库修改
        Done done=new Done(user_id,date,bed_id,true);
        done.updateAll("user_id = ? and date =?",user_id,date);


//        把服务器数据修改，并且提交今天的日期
        Log.d(TAG, "onClick: "+date);
        SendDataDoneTask sendDataDoneTask=new SendDataDoneTask(user_id,date,bed_id,true);
        sendDataDoneTask.setHandler(handler);
        Thread thread=new Thread(sendDataDoneTask);
        thread.start();


    }




}