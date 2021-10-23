package com.example.dormitorystar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.dormitorystar.utilcalendarview.CompactCalendarView;
import com.example.dormitorystar.utilcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalenderActivity extends AppCompatActivity {

    public static final String TAG="CalenderActivity";

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        TextView zeroIcon,threeIcon,fourIcon,fiveIcon;




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


        compactCalendarView.setDayColumnNames(new String[]{"Mon","Tue","Thr","Wen","Fri","Sat","Sun"});


        loadEvents();
        loadEventsForYear(2017);
        compactCalendarView.invalidate();

        logEventsByMonth(compactCalendarView);


    }

    private void addEvents(int month, int year) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        for (int i = 0; i < 6; i++) {
            currentCalender.setTime(firstDayOfMonth);
            if (month > -1) {
                currentCalender.set(Calendar.MONTH, month);
            }
            if (year > -1) {
                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
                currentCalender.set(Calendar.YEAR, year);
            }
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();

            List<Event> events = getEvents(timeInMillis, i);

            compactCalendarView.addEvents(events);
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 1) {
            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
            event.setBed_id(1);
            event.setHasDone(true);
            return Arrays.asList(event);
        }
//        else if ( day > 2 && day <= 4) {
//            return Arrays.asList(
//                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
//
//        }
        else if(day > 1 && day < 3){
            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
            event.setBed_id(2);
            event.setHasDone(false);
            return Arrays.asList(event);
//            return Arrays.asList(
//                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
//                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
//                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
        else if(day>2 && day <4) {
            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
            event.setBed_id(3);
            event.setHasDone(false);
            return Arrays.asList(event);
        }
        else {
            Event event=new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis));
            event.setBed_id(4);
            event.setHasDone(true);
            return Arrays.asList(event);
        }

    }

    private void loadEvents() {
        addEvents(-1, -1);
        addEvents(Calendar.DECEMBER, -1);
        addEvents(Calendar.AUGUST, -1);
    }

    private void loadEventsForYear(int year) {
        addEvents(Calendar.DECEMBER, year);
        addEvents(Calendar.AUGUST, year);
    }

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