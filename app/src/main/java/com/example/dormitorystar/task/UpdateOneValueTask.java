package com.example.dormitorystar.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//what=4  更新user_id的一项数据
public class UpdateOneValueTask implements Runnable{
    Handler handler;
    String url="http://118.195.165.40:8080/JSONUpdate/dataChange.jsp";
    String name;
    String value;
    String type;
    String user_id;
    public static final String TAG="UpdateOneValueTask";

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public UpdateOneValueTask(String name, String value, String type,String user_id) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.user_id=user_id;
    }

    @Override
    public void run() {
        OkHttpClient client=new OkHttpClient();
        String url1=url+"?name="+name+"&value="+value+"&type="+type+"&user_id="+user_id;
        Request request=new Request.Builder().get().url(url1).addHeader("Content-Type", "application/json; charset=gb2312").build();
        String user_id="";
        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                user_id = new String(response.body().bytes(), "gb2312");
                Log.d(TAG, "run: 成功了");
            }
            Message message=handler.obtainMessage(4);
            message.obj="更新"+name+"成功了";
            handler.sendMessage(message);

        } catch (IOException e) {
            Log.d(TAG, "run: 更新失败信息");
            e.printStackTrace();
        }

    }
}
