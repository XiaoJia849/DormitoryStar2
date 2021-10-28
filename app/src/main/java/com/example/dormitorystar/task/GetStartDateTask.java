package com.example.dormitorystar.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//what=7
//获取这个寝室的startDate数据
public class GetStartDateTask implements Runnable{
    String dormitory_id;
    Handler handler;
    String url="http://118.195.165.40:8080/JSONUpdate/dataStartDateGet.jsp";
    public static final String TAG="GetStartDateTask";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public GetStartDateTask(String dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    @Override
    public void run() {
        String url1=url+"?dormitory_id="+dormitory_id+"&type="+"1";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().get().url(url1).addHeader("Content-Type", "application/json; charset=gb2312").build();
        String strJson = null;
        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                    strJson = new String(response.body().bytes(), "gb2312");
                Log.d(TAG, "run: 获得的strJSON"+strJson);
            }
            Message message=handler.obtainMessage(7);
            message.obj=strJson;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
