package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.dormitorystar.utilcalendarview.CompactCalendarView;
import com.example.dormitorystar.utilcalendarview.domain.Event;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


//根据type进行变动
public class CalenderActivity extends AppCompatActivity {

    public static final String TAG="CalenderActivity";

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    NavigationTabStrip navigationTabStrip ;

    int type=0;

    protected void initNavi(){
        navigationTabStrip= findViewById(R.id.navi_calendar);
        Log.d(TAG, "initNavi: hh");
        navigationTabStrip.setTitles(getResources().getString(R.string.calendar), getResources().getString(R.string.user));
        navigationTabStrip.setTabIndex(0, true);
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
                if(index==1){
//                    跳转到user页面
                    Intent intent=new Intent(CalenderActivity.this,UserInfoEditActivity.class);
                    startActivity(intent);
//                    把这个页面的资源释放了,之后再说


                }

            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Log.d(TAG, "onCreate: 咋了");
        TextView zeroIcon,threeIcon,fourIcon,fiveIcon;

//初始化导航栏
        initNavi();

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




        compactCalendarView=findViewById(R.id.calendar);
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);


        compactCalendarView.setDayColumnNames(new String[]{"Mon","Tue","Wen","Thu","Fri","Sat","Sun"});


//        loadEvents();
//        loadEventsForYear(2017);

//        -1 -1表示本年，本月
//        addEvents(-1, -1);

//        addEvents(Calendar.DECEMBER, 2021);



        if(type==1){
//         这个月按照计划1进行安排
            Plan1();
        }
        if(type==2){
            //        这个月按照计划2安排
            try {
                Plan2("2021-10-02",2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }





        compactCalendarView.invalidate();

        logEventsByMonth(compactCalendarView);


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
                event.setHasDone(true);
                events=Arrays.asList(event);
            }
            if(netday==dateBwteen){
                Event event1=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                event1.setBed_id(2);
                event1.setHasDone(true);
                events=Arrays.asList(event1);
            }
            if(netday==2*dateBwteen){
                Event event2=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                event2.setBed_id(3);
                event2.setHasDone(true);
                events=Arrays.asList(event2);
            }
            if(netday==3*dateBwteen){
                Event event3=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                event3.setBed_id(4);
                event3.setHasDone(true);
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
                    event.setHasDone(true);
                    events=Arrays.asList(event);
                    break;
                case Calendar.WEDNESDAY:
                    Event event1=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                    event1.setBed_id(2);
                    event1.setHasDone(true);
                    events=Arrays.asList(event1);
                    break;
                case Calendar.FRIDAY:
                    Event event2=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                    event2.setBed_id(3);
                    event2.setHasDone(true);
                    events=Arrays.asList(event2);
                    break;
                case Calendar.SUNDAY:
                    Event event3=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "2");
                    event3.setBed_id(4);
                    event3.setHasDone(true);
                    events=Arrays.asList(event3);
                    break;
                default:
                    break;
            }

            compactCalendarView.addEvents(events);
        }



    }



//    private void addEvents(int month, int year) {
////        设置今天的日期
//        currentCalender.setTime(new Date());
////        Log.d(TAG, "addEvents: currentCalender"+currentCalender.getTime().toString());
////        设置这个月第一天的日期
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
////        Log.d(TAG, "addEvents: currentCalender"+currentCalender.getTime().toString());
//        Date firstDayOfMonth = currentCalender.getTime();
//        for (int i = 0; i < 6; i++) {
//            currentCalender.setTime(firstDayOfMonth);
//            if (month > -1) {
//                currentCalender.set(Calendar.MONTH, month);
//            }
//            if (year > -1) {
//                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
//                currentCalender.set(Calendar.YEAR, year);
//            }
//            currentCalender.add(Calendar.DATE, i);
//            setToMidnight(currentCalender);
//            long timeInMillis = currentCalender.getTimeInMillis();
//
////            给这个月第i天添加事件
//            List<Event> events = getEvents(timeInMillis, i);
//
//            compactCalendarView.addEvents(events);
//        }
//    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

//    private List<Event> getEvents(long timeInMillis, int day) {
//        if (day < 1) {
//            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
//            event.setBed_id(1);
//            event.setHasDone(true);
//            return Arrays.asList(event);
//        }
////        else if ( day > 2 && day <= 4) {
////            return Arrays.asList(
////                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
////                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
////
////        }
//        else if(day > 1 && day < 3){
//            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
//            event.setBed_id(2);
//            event.setHasDone(false);
//            return Arrays.asList(event);
////            return Arrays.asList(
////                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
////                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
////                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
//        }
//        else if(day>2 && day <4) {
//            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
//            event.setBed_id(3);
//            event.setHasDone(false);
//            return Arrays.asList(event);
//        }
//        else {
//            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
//            event.setBed_id(4);
//            event.setHasDone(true);
//            return Arrays.asList(event);
//        }
//
//    }

//    private void loadEvents() {
//        addEvents(-1, -1);
//        addEvents(Calendar.DECEMBER, -1);
//        addEvents(Calendar.AUGUST, -1);
//    }

//    private void loadEventsForYear(int year) {
//        addEvents(Calendar.DECEMBER, year);
//        addEvents(Calendar.AUGUST, year);
//    }

    private void logEventsByMonth(CompactCalendarView compactCalendarView) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        currentCalender.set(Calendar.MONTH, Calendar.AUGUST);
        List<String> dates = new ArrayList<>();
        Locale locale = Locale.FRANCE;
        dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", locale);
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        dateFormatForDisplaying.setTimeZone(timeZone);

        for (Event e : compactCalendarView.getEventsForMonth(new Date())) {
            dates.add(dateFormatForDisplaying.format(e.getTimeInMillis()));
        }
        Log.d(TAG, "Events for Aug with simple date formatter: " + dates);
        Log.d(TAG, "Events for Aug month using default local and timezone: " + compactCalendarView.getEventsForMonth(currentCalender.getTime()));
    }


}