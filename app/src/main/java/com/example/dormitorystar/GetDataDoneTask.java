package com.example.dormitorystar;

import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;

import java.io.IOException;

//what=0  获取一个User_id  所有历史Done数据
public class GetDataDoneTask implements Runnable{
    Handler handler;
    String user_id;
    public static final String TAG="GetDataDoneTask";
    String url="http://192.168.43.123:8081/JSONUpdate/dataDoneGet.jsp";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public GetDataDoneTask(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public void run() {
        String strJson = null;
        try {
            strJson = Jsoup.connect(url+"?user_id="+user_id)
                    .ignoreContentType(true)
                    .execute()
                    .body().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message=handler.obtainMessage(0);
        message.obj=strJson;
        handler.sendMessage(message);



    }
}
