package com.example.dormitorystar;


import android.os.Message;

import org.jsoup.Jsoup;

import java.io.IOException;
import android.os.Handler;
import android.os.Message;

//what=5 获取某一天的user_id的done的数据
//返回数据bed_id不要获取
public class GetDataDoneOneDay implements Runnable{
    Handler handler;
    String user_id,date;
    public static final String TAG="GetDataDoneOneDay";
    String url="http://192.168.43.123:8081/JSONUpdate/dataDoneDate.jsp";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public GetDataDoneOneDay(String user_id, String date) {
        this.user_id = user_id;
        this.date = date;
    }

    @Override
    public void run() {
        String strJson = null;
        try {
            strJson = Jsoup.connect(url+"?user_id="+user_id+"&date="+date)
                    .ignoreContentType(true)
                    .execute()
                    .body().trim();
            boolean done=Boolean.valueOf(strJson);
            Message message=handler.obtainMessage(5);
            Done done1=new Done(user_id,date,0,done);
            message.obj=done1;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
