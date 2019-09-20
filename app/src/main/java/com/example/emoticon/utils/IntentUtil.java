package com.example.emoticon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//Intent跳转封装
public class IntentUtil {
    public static final String OPEN_ACTIVITY_KEY = "open_activity";//intent跳转传递参数
    private static IntentUtil manager;
    private static Intent intent;

    private IntentUtil(){

    }

    public static IntentUtil get(){
        if(manager == null){
            synchronized (IntentUtil.class){
                if(manager == null){
                    manager = new IntentUtil();
                }
            }
        }
        intent = new Intent();
        return manager;
    }

    //启动一个Activity
    public void goActivity(Context _this, Class<? extends Activity> _class){
        intent.setClass(_this,_class);
        _this.startActivity(intent);
        _this = null;
    }

    //启动Activity后kill前一个
    public void goActivityKill(Context _this, Class<? extends Activity> _class){
        intent.setClass(_this,_class);
        _this.startActivity(intent);
        ((Activity) _this).finish();
        _this = null;
    }

//    //获取上一个界面传递过来的参数
//    public <T extends Parcelable> T getParcelableExtra(Activity activity) {
//        Parcelable parcelable = activity.getIntent().getParcelableExtra(OPEN_ACTIVITY_KEY);
//        activity = null;
//        return (T) parcelable;
//    }

    //向下个Activity传递数据
    public void goActivityPassing(Context _this,Class<? extends Activity> _class,String title){
        intent.setClass(_this,_class);
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        intent.putExtras(bundle);
        _this.startActivity(intent);
        //((Activity) _this).finish();
        _this = null;
    }

    //接收上个Activity传递来的数据
    public String receiveActivityPassing(Activity activity){
        Bundle bundle = activity.getIntent().getExtras();
        String title = bundle.getString("title");
        //((Activity)_this).finish();
        return title;
    }
}
