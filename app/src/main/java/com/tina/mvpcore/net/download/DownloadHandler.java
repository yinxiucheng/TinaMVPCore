package com.tina.mvpcore.net.download;

import android.os.AsyncTask;

import com.tina.mvpcore.net.RestCreator;
import com.tina.mvpcore.net.callback.IError;
import com.tina.mvpcore.net.callback.IFailure;
import com.tina.mvpcore.net.callback.IRequest;
import com.tina.mvpcore.net.callback.ISuccess;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yxc
 * @date 2018/11/19
 */
public class DownloadHandler {

    private final HashMap<String, Object> PARAMS;

    private final String URL;

    private final IRequest REQUEST;

    private final ISuccess SUCCESS;

    private final IFailure FAILURE;

    private final IError ERROR;

    private final String DOWNLOAD_DIR;

    private final String EXTENTSION;

    private final String FILENAME;

    public DownloadHandler(HashMap<String, Object> params,
                           String url,
                           IRequest request,
                           ISuccess success,
                           IFailure failure,
                           IError error,
                           String download_dir,
                           String extentsion,
                           String filename) {
        this.PARAMS = params;
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENTSION = extentsion;
        this.FILENAME = filename;
    }

    public final void handleDownload(){
        RestCreator.getRestService().download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            //开始保存文件,请求一个一部任务
                            SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DOWNLOAD_DIR, EXTENTSION, response.body(), FILENAME);

                            //如果下载完成
                            if (task.isCancelled()){
                                if (REQUEST != null){
                                    REQUEST.onRequestEnd();
                                }
                            }

                        }else{
                            if (ERROR != null){
                                ERROR.onError(response.code(), response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if(null != FAILURE){
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
