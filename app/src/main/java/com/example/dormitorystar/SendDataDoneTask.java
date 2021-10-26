package com.example.dormitorystar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//what=2  发送一条done信息到数据库
public class SendDataDoneTask implements Runnable{
    Handler handler;
    String user_id,date;
    int bed_id;
    boolean done;
    String url="http://192.168.43.123:8081/JSONUpdate/dataDoneCreate.jsp";
    public static final String TAG="SendDataDoneTask";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public SendDataDoneTask(String user_id, String date, int bed_id, boolean done) {
        this.user_id = user_id;
        this.date = date;
        this.bed_id = bed_id;
        this.done = done;
    }

    @Override
    public void run() {
        String new_url=url+"?user_id="+user_id+"&date="+date+
                "&bed_id="+bed_id+"&done="+done;
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().get().url(new_url).addHeader("Content-Type", "application/json; charset=gb2312").build();
        String user_id="";
        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                Message message=handler.obtainMessage(2);
                message.obj=user_id;
                handler.sendMessage(message);
            }
        } catch (IOException e) {
            Log.d(TAG, "run: 失败信息");
            e.printStackTrace();
        }


    }
}
