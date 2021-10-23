package com.example.dormitorystar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

public class MemberLeaderElectActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView imageView1;
    ImageView imageView2;
    public static final String TAG="MemberLeaderElectActivity";
    Display display;
//    默认为normal,各个小球绕着中心旋转，elect时所有小球停下来，可以被点击认为选择作为寝室长

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_leader_elect);
        imageView=findViewById(R.id.imageView);
        imageView.setX(0);
        imageView.setY(0);


        // arcTo() and PathInterpolator only available on API 21+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();

//            前4个变量确定一个椭圆， 以正右为正轴，顺时针为正旋转，这个路径就是Path。起始位置在270f的位置，逆时针旋转18
//            旋转一周必须写359否则不转的
            path.arcTo(0f, 0f, 1000f, 1000f, 270f, -359f, true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, View.X, View.Y, path);
            animator.setDuration(2000);
            animator.setRepeatCount(-1);
            animator.start();
            Log.d(TAG, "onCreate: hhhh");

            path.arcTo(0f, 0f, 1000f, 1000f, 270f, -359f, false);
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView1, View.X, View.Y, path);
            animator1.setDuration(2000);
            animator1.setRepeatCount(-1);
            animator1.start();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.normal){


        }

        if(item.getItemId()==R.id.elect){


        }
        return super.onOptionsItemSelected(item);
    }
}