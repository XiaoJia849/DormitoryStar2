package com.example.dormitorystar.task;


import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;

import java.io.IOException;

//what=6 修改，添加 startDate
//修改这个寝室的StartDate数据
public class UpdateStartDateTask implements Runnable{
    String dormitory_id;
    Handler handler;
    String start_date;
    int betweenDate;
    String url="http://118.195.165.40:8080/JSONUpdate/dataStartDateGet.jsp";


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public UpdateStartDateTask(String dormitory_id, String start_date, int betweenDate) {
        this.dormitory_id = dormitory_id;
        this.start_date = start_date;
        this.betweenDate = betweenDate;
    }

    @Override
    public void run() {
        String url1=url+"?dormitory_id="+dormitory_id+"&type=2"+"&start_date="+start_date+"&betweenDate="+betweenDate;
        String strJson = null;
        try {
            strJson = Jsoup.connect(url1)
                    .ignoreContentType(true)
                    .execute()
                    .body().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message=handler.obtainMessage(6);
        message.obj=strJson;
        handler.sendMessage(message);



    }
}
