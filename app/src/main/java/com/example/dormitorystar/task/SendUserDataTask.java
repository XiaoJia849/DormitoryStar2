package com.example.dormitorystar.task;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//what=3  发送Users数据给服务器,返回用户id
public class SendUserDataTask implements Runnable{
    public static final String TAG="SendUserDataTask";

    Handler handler;
    String url="http://118.195.165.40:8080/JSONUpdate/dataRegister.jsp";
    String nickname,dormitory_id;
    int bed_id;
    boolean leader;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public SendUserDataTask(String nickname, String dormitory_id, int bed_id, boolean leader) {
        this.nickname = nickname;
        this.dormitory_id = dormitory_id;
        this.bed_id = bed_id;
        this.leader = leader;
    }

    @Override
    public void run() {

        String new_url=url+"?nickname="+nickname+"&dormitory_id="+dormitory_id+
                "&bed_id="+bed_id+"&leader="+leader;
        OkHttpClient client=new OkHttpClient();
        Log.d(TAG, "run: new_url"+new_url);
        Request request=new Request.Builder().get().url(new_url).addHeader("Content-Type", "application/json; charset=gb2312").build();
        String user_id="";
        try {
            Response response=client.newCall(request).execute();
            if(response.isSuccessful()){
                user_id = new String(response.body().bytes(), "gb2312");
                user_id=user_id.trim();
                Log.d(TAG, "run: 成功了");
            }
            Message message=handler.obtainMessage(3);
            message.obj=user_id;
            handler.sendMessage(message);

        } catch (IOException e) {
            Log.d(TAG, "run: 失败信息");
            e.printStackTrace();
        }


    }
}
