package com.example.dormitorystar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    int ResourceId;
    Context Context;
    public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        ResourceId=resource;
        Context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            convertView= LayoutInflater.from(getContext()).inflate(ResourceId,parent,false);
        }
        ImageView imageView=convertView.findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(Context, R.anim.rotate);
        imageView.startAnimation(animation);

        return convertView;
    }
}
