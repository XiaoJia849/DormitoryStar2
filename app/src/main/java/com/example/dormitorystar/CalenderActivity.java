package com.example.dormitorystar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dormitorystar.obj.Done;
import com.example.dormitorystar.obj.StartDate;
import com.example.dormitorystar.obj.User;
import com.example.dormitorystar.task.GetDataDoneOneDayTask;
import com.example.dormitorystar.task.GetDataDoneTask;
import com.example.dormitorystar.task.GetStartDateTask;
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

    public static final String TAG = "CalenderActivity";
    public static final int GET_DATA_DONE = 0;
    public static final int GET_DATA_DORM = 1;
    public static final int SEND_DATA_DONE = 2;
    public static final int GET_DATA_DONE_ONE_DAY = 5;
    public static final int GET_SATRT_DATE=7;

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    NavigationTabStrip navigationTabStrip;
    Handler handler;

    TextView zeroIcon, threeIcon, fourIcon, fiveIcon;
    TextView zeroIconText, threeIconText, fourIconText, fiveIconText;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    String startDate;
    Gson gson = new Gson();

    //    室友信息
    List<Done> dones;
    List<User> users;

    //    记录寝室人数
    int num;
//    记录手机主人的某些信息
    String user_id,dormitory_id,date;
    int bed_id;

    String dateStart;
    int dateBwt;


    public static final int RED=Color.argb(128,255,0,0);
    public static final int YELLOW=Color.argb(128,255,215,0);
    public static final int BLUE=Color.argb(128,0,0,255);
    public static final int GREEN=Color.argb(128,0,128,0);



    int type;

    protected void initNavi() {
        navigationTabStrip = findViewById(R.id.navi_calendar);
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
                if (index == 1) {
//                    跳转到user页面
                    Intent intent = new Intent(CalenderActivity.this, UserInfoEditActivity.class);
                    startActivity(intent);
                    finish();
                    //                    把这个页面的资源释放了,之后再说


                }

            }
        });

    }

    protected void initCalendar() {
        compactCalendarView = findViewById(R.id.calendar);
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        String[] week=new String[]{getResources().getString(R.string.Mon),
        getResources().getString(R.string.Tue),
                getResources().getString(R.string.Wen),
                getResources().getString(R.string.Thu),
                getResources().getString(R.string.Fri),
                getResources().getString(R.string.Sat),
                getResources().getString(R.string.Sun)
        };
        compactCalendarView.setDayColumnNames(week);
    }

    protected void initView() {

        SharedPreferences sharedPreferences = getSharedPreferences("User", Activity.MODE_PRIVATE);
        bed_id = sharedPreferences.getInt("bed_id", 0);
        user_id = sharedPreferences.getString("user_id", "");
        dormitory_id=sharedPreferences.getString("dormitory_id","");
        type = sharedPreferences.getInt("type", 1);

        date=sdf.format(new Date());


        zeroIcon = findViewById(R.id.zeroIcon);
        threeIcon = findViewById(R.id.threeIcon);
        fourIcon = findViewById(R.id.fourIcon);
        fiveIcon = findViewById(R.id.fiveIcon);
        zeroIconText=findViewById(R.id.zeroIconText);
        threeIconText=findViewById(R.id.threeIconText);
        fourIconText=findViewById(R.id.fourIconText);
        fiveIconText=findViewById(R.id.fiveIconText);



        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        zeroIcon.setTypeface(font);
        zeroIcon.setText(getResources().getString(R.string.zero));

        threeIcon.setTypeface(font);
        threeIcon.setText(getResources().getString(R.string.three));

        fourIcon.setTypeface(font);
        fourIcon.setText(getResources().getString(R.string.four_fill));

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


    protected void aboutHandler(){
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case GET_SATRT_DATE:
                        String s= (String) msg.obj;
                        StartDate startDate= gson.fromJson(s,new TypeToken<StartDate>(){}.getType());
                        dateStart=startDate.getStart_date();
                        dateBwt=startDate.getBetweenDate();

                        try {
                            Plan2(dateStart, dateBwt);
                            draw(getBed_idNeedDonePlan2(dateStart,dateBwt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;

                    case GET_DATA_DONE:
                        String strJSON = (String) msg.obj;
                        Log.d(TAG, "获取某个室友的历史Done" + strJSON);
                        List<Done> dones1 = gson.fromJson(strJSON, new TypeToken<List<Done>>() {}.getType());
                        try {
                            createEventByDones(dones1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SEND_DATA_DONE:
                        Log.d(TAG, "handleMessage: 发送今天xx值日信息");
                        break;

                    case GET_DATA_DONE_ONE_DAY:
                        Log.d(TAG, "handleMessage: 获取今天谁做了值日，发小红花");
                        Done done= (Done) msg.obj;
                        if(done.isDone()){
                            Log.d(TAG, "handleMessage: "+done.getBed_id()+"做了值日");
                            Typeface font = Typeface.createFromAsset(getAssets(), "iconfont4.ttf");
                            TextView textView;
                            switch (done.getBed_id()){
                                case 1:
                                    textView=findViewById(R.id.flowerzero);
                                    textView.setTypeface(font);
                                    textView.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    textView=findViewById(R.id.flowerthree);
                                    textView.setTypeface(font);
                                    textView.setVisibility(View.VISIBLE);
                                    break;

                                case 3:
                                    textView=findViewById(R.id.flowerfour);
                                    textView.setTypeface(font);
                                    textView.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    textView=findViewById(R.id.flowerfive);
                                    textView.setTypeface(font);
                                    textView.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                }


            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        aboutHandler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

//        获取室友个人信息
        users = LitePal.findAll(User.class);
        initNavi();
        initView();
        initCalendar();

//        获取室友+自己done 历史所有信息
        getDataFromUrl();

//        及时刷新获取done信息
        getDataTody();

        if (type == 1) {
            Plan1();
            draw(getBed_idNeedDonePlan1());
        }
        if (type == 2) {

            getStartDateBwt();

        }


        compactCalendarView.invalidate();


    }

    //    获取今天是那个bed_id值日 如果是0那就没有人需要值日了
    protected int getBed_idNeedDonePlan2(String dateStart, int dateBetween) throws ParseException {
        Date dateS = sdf.parse(dateStart);
        Date dateF = new Date();
        long day = (dateF.getTime() - dateS.getTime()) / 24 / 60 / 60 / 1000;
        int netday = (int) (day % (dateBetween * 4));
        if(netday==0){
            return 1;
        }else if(netday==dateBetween){
            return 2;
        }else if (netday==2*dateBetween){
            return 3;
        }else if (netday==3*dateBetween){
            return 4;
        }else {
            return 0;
        }

    }
    protected int getBed_idNeedDonePlan1(){
        Date date=new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String weekday=dateFm.format(date);
        switch (weekday){
            case "星期一":
                return 1;
            case "星期三":
                return 2;
            case "星期五":
                return 3;
            case "星期日":
                return 4;
            default:
                return 0;
        }

    }


//    设置实心图案
    protected void draw(int bed_id){
        switch (bed_id){
            case 1:
                zeroIcon.setText(getResources().getString(R.string.zero_fill));
                return;
            case 2:
                threeIcon.setText(getResources().getString(R.string.three_fill));
                return;
            case 3:
                fourIcon.setText(getResources().getString(R.string.four_fill));
                return;
            case 4:
                fiveIcon.setText(getResources().getString(R.string.five_fill));
                return;
            default:
                return;
        }


    }




    //    4个人 设置开始年月日 和间隔时间
    protected void Plan2(String dateStart, int dateBwteen) throws ParseException {
//        获取那一天到今天的距离
        Date dateS = sdf.parse(dateStart);
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        Date dateF = sdf.parse(sdf.format(firstDayOfMonth));
        long day = (dateF.getTime() - dateS.getTime()) / 24 / 60 / 60 / 1000;
        int netday = (int) (day % (dateBwteen * 4));
        int max_day_of_month = currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH);    //max_day_of_month=31

        for (int i = 0; i < max_day_of_month; i++) {
            currentCalender.setTime(firstDayOfMonth);
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();
            List<Event> events = new ArrayList<>();

            if (netday == 0) {
//                一号
                Event event = new Event(RED, timeInMillis, "1");
                event.setBed_id(1);
                event.setHasDone(false);
                events = Arrays.asList(event);
            }
            if (netday == dateBwteen) {
                Event event1 = new Event(YELLOW, timeInMillis, "2");
                event1.setBed_id(2);
                event1.setHasDone(false);
                events = Arrays.asList(event1);
            }
            if (netday == 2 * dateBwteen) {
                Event event2 = new Event(BLUE, timeInMillis, "2");
                event2.setBed_id(3);
                event2.setHasDone(false);
                events = Arrays.asList(event2);
            }
            if (netday == 3 * dateBwteen) {
                Event event3 = new Event(GREEN, timeInMillis, "2");
                event3.setBed_id(4);
                event3.setHasDone(false);
                events = Arrays.asList(event3);
            }
            netday = (netday + 1) % (dateBwteen * 4);

            compactCalendarView.addEvents(events);
        }


    }


    //    四个人 1：1   3：2     5：3     7：4
    protected void Plan1() {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        int max_day_of_month = currentCalender.getActualMaximum(Calendar.DAY_OF_MONTH);    //max_day_of_month=31

        for (int i = 0; i < max_day_of_month; i++) {
            currentCalender.setTime(firstDayOfMonth);
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();
            List<Event> events = new ArrayList<>();
//          判断这一天星期几
            switch (currentCalender.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    Event event = new Event(RED, timeInMillis, "1");
                    event.setBed_id(1);
                    event.setHasDone(false);
                    events = Arrays.asList(event);
                    break;
                case Calendar.WEDNESDAY:
                    Event event1 = new Event(YELLOW, timeInMillis, "2");
                    event1.setBed_id(2);
                    event1.setHasDone(false);
                    events = Arrays.asList(event1);
                    break;
                case Calendar.FRIDAY:
                    Event event2 = new Event(BLUE, timeInMillis, "2");
                    event2.setBed_id(3);
                    event2.setHasDone(false);
                    events = Arrays.asList(event2);
                    break;
                case Calendar.SUNDAY:
                    Event event3 = new Event(GREEN, timeInMillis, "2");
                    event3.setBed_id(4);
                    event3.setHasDone(false);
                    events = Arrays.asList(event3);
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



//    获取今天这4个人有没有做值日的信息
    protected void getDataTody() {
        String date = sdf.format(new Date());
        for (User user : users) {
            GetDataDoneOneDayTask getDataDoneOneDayTask = new GetDataDoneOneDayTask(user.getUser_id(), date, user.getBed_id());
            getDataDoneOneDayTask.setHandler(handler);
            Thread thread = new Thread(getDataDoneOneDayTask);
            thread.start();
        }

        
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
////                向服务器发送请求获取今天的数据
//                for (User user : users) {
//                    GetDataDoneOneDayTask getDataDoneOneDayTask = new GetDataDoneOneDayTask(user.getUser_id(), date, user.getBed_id());
//                    getDataDoneOneDayTask.setHandler(handler);
//                    Thread thread = new Thread(getDataDoneOneDayTask);
//                    thread.start();
//                }
//
//            }
//        }, 0, 300000);

    }


    //    从服务器获取 一个寝室所有同学的 json Str 关于是否做了值日的历史数据。是所有的历史数据
    protected void getDataFromUrl() {
        num = 2;
        for(User user:users){
            GetDataDoneTask getDataDoneTask=new GetDataDoneTask(user.getUser_id());
            getDataDoneTask.setHandler(handler);
            Thread thread=new Thread(getDataDoneTask);
            thread.start();
        }


//        做个测试
//        String[] userS = {"000001", "000002"};
//        for (String s : userS) {
//            GetDataDoneTask getDataDoneTask = new GetDataDoneTask(s);
//            getDataDoneTask.setHandler(handler);
//            Thread thread = new Thread(getDataDoneTask);
//            thread.start();
//        }


    }

//    获取这个寝室的StartDate
    protected void getStartDateBwt(){
        GetStartDateTask getStartDateTask=new GetStartDateTask(dormitory_id);
        getStartDateTask.setHandler(handler);
        Thread thread=new Thread(getStartDateTask);
        thread.start();

    }




    protected void createEventByDones(List<Done> dones) throws ParseException {
        if(dones==null){
            return;
        }
        List<Event> events = new ArrayList<>();
        for (Done done : dones) {
            currentCalender.setTime(sdf.parse(done.getDate()));
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();
            Event event = new Event(RED, timeInMillis, "没啥可以说的");
            event.setBed_id(done.getBed_id());
            event.setHasDone(done.isDone());

            switch (done.getBed_id()){
                case 1:
                    event.setColor(RED);
                    break;
                case 2:
                    event.setColor(YELLOW);
                    break;
                case 3:
                    event.setColor(BLUE);
                    break;
                case 4:
                    event.setColor(GREEN);
                    break;
                default:
                    break;
            }
            events.add(event);

        }
        compactCalendarView.addEvents(events);

    }

    //    做了今天的值日
    @Override
    public void onClick(View v) {

//        给今天一朵小红花
        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont4.ttf");
        TextView textView;
        switch (bed_id){
            case 1:
                textView=findViewById(R.id.flowerzero);
                textView.setTypeface(font);
                textView.setVisibility(View.VISIBLE);
                break;
            case 2:
                textView=findViewById(R.id.flowerthree);
                textView.setTypeface(font);
                textView.setVisibility(View.VISIBLE);
                break;

            case 3:
                textView=findViewById(R.id.flowerfour);
                textView.setTypeface(font);
                textView.setVisibility(View.VISIBLE);
                break;
            case 4:
                textView=findViewById(R.id.flowerfive);
                textView.setTypeface(font);
                textView.setVisibility(View.VISIBLE);
                break;
        }


//        把本地数据库修改
        Done done = new Done(user_id, date, bed_id, true);
        done.updateAll("user_id = ? and date =?", user_id, date);


//        把服务器数据修改，并且提交今天的日期
        Log.d(TAG, "onClick: 需要发送的数据"+date+"user_id"+user_id);
        SendDataDoneTask sendDataDoneTask = new SendDataDoneTask(user_id, date, bed_id, true);
        sendDataDoneTask.setHandler(handler);
        Thread thread = new Thread(sendDataDoneTask);
        thread.start();


    }


}