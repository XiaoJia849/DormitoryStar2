package com.example.dormitorystar.task;


import android.os.Message;

import org.jsoup.Jsoup;

import java.io.IOException;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.dormitorystar.obj.Done;

//what=5 获取某一天的user_id的done的数据
//返回数据bed_id不要获取
public class GetDataDoneOneDayTask implements Runnable{
    Handler handler;
    String user_id,date;
    int bed_id;
    public static final String TAG="GetDataDoneOneDayTask";
    String url="http://118.195.165.40:8080/JSONUpdate/dataDoneDateGet.jsp";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public GetDataDoneOneDayTask(String user_id, String date,int bed_id) {
        this.user_id = user_id;
        this.date = date;
        this.bed_id=bed_id;
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
            Log.d(TAG, "run: 获取的单个人的"+done);
            Message message=handler.obtainMessage(5);
            Done done1=new Done(user_id,date,bed_id,done);
            message.obj=done1;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
