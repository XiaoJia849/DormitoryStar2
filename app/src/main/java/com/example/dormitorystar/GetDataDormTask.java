package com.example.dormitorystar;

import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;

import java.io.IOException;

//获取寝室数据User   并且对应的what=1
public class GetDataDormTask implements Runnable{
    Handler handler;
    String url="http://192.168.43.123:8081/JSONUpdate/dataGetDorm.jsp?dormitory_id=";
    String dormitory_id;


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setDormitory_id(String dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    @Override
    public void run() {
        String strJson = null;
        try {
            strJson = Jsoup.connect(url+dormitory_id)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message=handler.obtainMessage(1);
        message.obj=strJson;
        handler.sendMessage(message);

    }
}
