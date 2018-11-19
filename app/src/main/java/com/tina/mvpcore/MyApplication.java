package com.tina.mvpcore;

import android.app.Application;

import com.tina.mvpcore.app.ProjectInit;

import java.util.ArrayList;

import okhttp3.Interceptor;

/**
 * @author yxc
 * @date 2018/11/19
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ProjectInit.init(this)
                .withApiHost("")
                .configure();


    }
}
