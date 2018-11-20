package com.tina.mvpcore;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tina.mvpcore.net.RestClient;
import com.tina.mvpcore.net.rx.RxRestClient;
import com.tina.mvpcore.net.callback.IError;
import com.tina.mvpcore.net.callback.IFailure;
import com.tina.mvpcore.net.callback.ISuccess;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View view) {
//        HashMap params=new HashMap();
//        params.put("username","jett");
//        params.put("password","123");
//        RestClient.create()
//                .url("/login/info")
//                .params(params)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String responce) {
//                        Toast.makeText(MainActivity.this, responce.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }).build()
//                .get();
        //上传
        //http://dengpaoedu.com:8080/fileuploadanddownload/uploadServlet.

        HashMap params = new HashMap();
        params.put("file", "abcd.txt");
        RestClient.create()
                .params(params)
                .url("/fileuploadanddownload/uploadServlet")
                .file(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.txt")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String responce) {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                }).failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    }
                 }).error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(MainActivity.this, code + "", Toast.LENGTH_SHORT).show();
                    }
                 })
                 .build().upload();

        //测试下载  http://dengpaoedu.com:8080/examples/test.zip
//        RestClient.create()
//                .params(params)
//                .url("/examples/test.zip")
//                .dir("/sdcard")
//                .extension("zip")
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String responce) {
//                        Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
//                    }
//                }).build().download();
    }


    public void rxInvoke(){
        HashMap params = new HashMap();
        String url="/login/info";
        params.put("username","jett");
        params.put("password","123");

        RxRestClient.create()
                .url(url)
                .params(params)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
