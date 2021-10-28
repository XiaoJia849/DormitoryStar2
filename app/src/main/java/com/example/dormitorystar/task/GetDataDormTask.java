package com.example.dormitorystar.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//获取寝室数据User   并且对应的what=1
public class GetDataDormTask implements Runnable{
    Handler handler;
    String url="http://118.195.165.40:8080/JSONUpdate/dataGetDorm.jsp?dormitory_id=";
    String dormitory_id;
    public static final String TAG="GetDataDormTask";


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setDormitory_id(String dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    @Override
    public void run() {


        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().get().url(url+dormitory_id).addHeader("Content-Type", "application/json; charset=gb2312").build();
        String strJson = null;
        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                strJson = new String(response.body().bytes(), "gb2312");
            }
            Message message=handler.obtainMessage(1);
            message.obj=strJson;
            handler.sendMessage(message);

        } catch (IOException e) {
            Log.d(TAG, "run: 失败信息");
            e.printStackTrace();
        }




    }
}
