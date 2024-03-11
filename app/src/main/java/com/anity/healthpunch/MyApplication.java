package com.anity.healthpunch;

import android.app.Application;

import com.anity.healthpunch.entity.Course;

import java.util.List;

public class MyApplication extends Application {

    private static MyApplication mApp;

    //配置开学第一天时间
    public final static int year = 2024;
    public final static int month = 3;
    public final static int dayOfMonth = 4;

    public String token;

    public volatile List<Course> list;


    public static MyApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = this;
    }

}
