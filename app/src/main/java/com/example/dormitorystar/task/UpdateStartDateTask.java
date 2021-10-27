package com.example.dormitorystar.task;


import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;

import java.io.IOException;

//what=6 获取，修改，添加 startDate
public class UpdateStartDateTask implements Runnable{
    String dormitory_id,type,start_date;
    Handler handler;
    String url="http://118.195.165.40:8080/JSONUpdate/dataStartDateGet.jsp";


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public UpdateStartDateTask(String dormitory_id, String type, String start_date) {
        this.dormitory_id = dormitory_id;
        this.type = type;
        this.start_date = start_date;
    }

    @Override
    public void run() {
        String url1=url+"?dormitory_id="+dormitory_id+"&type="+type+"&start_date="+start_date;
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
