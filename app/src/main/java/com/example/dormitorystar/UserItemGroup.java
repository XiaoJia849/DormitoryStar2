package com.example.dormitorystar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

public class UserItemGroup extends FrameLayout {
    private LinearLayout itemGroupLayout;
    private TextView title;
    private TextView content;
    private ImageView rightV;
    private Context context;


    public UserItemGroup(@NonNull Context context) {
        super(context);
    }

    public UserItemGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        this.context=context;
        initAttrs(context,attrs);
    }

    public UserItemGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        this.context=context;
        initAttrs(context,attrs);
    }

    public UserItemGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        this.context=context;
        initAttrs(context,attrs);
    }

    private void initView(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.item_group_layout,null);
        itemGroupLayout = (LinearLayout) view.findViewById(R.id.item_group);
        title=view.findViewById(R.id.title);
        content=view.findViewById(R.id.content);
        rightV=view.findViewById(R.id.right);
        addView(view);
    }


    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getContent() {
        return content;
    }

    public void setContent(TextView content) {
        this.content = content;
    }

    /**
     * 初始化相关属性，引入相关属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
    //标题的默认字体颜色
        int defaultTitleColor = context.getResources().getColor(R.color.gray);
        //输入框的默认字体颜色
        int defaultEdtColor = context.getResources().getColor(R.color.black);
        //输入框的默认的提示内容的字体颜色
        int defaultHintColor = context.getResources().getColor(R.color.gray);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemGroup);
        String title = typedArray.getString(R.styleable.ItemGroup_title);
        float paddingLeft = typedArray.getDimension(R.styleable.ItemGroup_paddingLeft, 15);
        float paddingRight = typedArray.getDimension(R.styleable.ItemGroup_paddingRight, 15);
        float paddingTop = typedArray.getDimension(R.styleable.ItemGroup_paddingTop, 5);
        float paddingBottom = typedArray.getDimension(R.styleable.ItemGroup_paddingBottom, 5);
        float titleSize = typedArray.getDimension(R.styleable.ItemGroup_title_size, 15);
        int titleColor = typedArray.getColor(R.styleable.ItemGroup_title_color, defaultTitleColor);
        String content = typedArray.getString(R.styleable.ItemGroup_edt_content);
        float contentSize = typedArray.getDimension(R.styleable.ItemGroup_edt_text_size, 13);
        int contentColor = typedArray.getColor(R.styleable.ItemGroup_edt_text_color, defaultEdtColor);
        String hintContent = typedArray.getString(R.styleable.ItemGroup_edt_hint_content);
        int hintColor = typedArray.getColor(R.styleable.ItemGroup_edt_hint_text_color, defaultHintColor);
        //默认输入框可以编辑
        boolean isEditable = typedArray.getBoolean(R.styleable.ItemGroup_isEditable, true);
        //向右的箭头图标是否可见，默认可见
        boolean showJtIcon = typedArray.getBoolean(R.styleable.ItemGroup_jt_visible, true);
        typedArray.recycle();

        //设置数据
        //设置item的内边距
        this.title.setText(title);
        this.title.setTextSize(titleSize);
        this.title.setTextColor(titleColor);

        this.content.setText(content);
        this.content.setTextSize(contentSize);
        this.content.setTextColor(contentColor);
        this.content.setHint(hintContent);
        this.content.setHintTextColor(hintColor);
//        contentEdt.setFocusable(isEditable); //设置输入框是否可以编辑
//        contentEdt.setClickable(true);
//        contentEdt.setKeyListener(null);
        this.rightV.setVisibility(showJtIcon ? View.VISIBLE : View.GONE);  //设置向右的箭头图标是否可见


    }


}
