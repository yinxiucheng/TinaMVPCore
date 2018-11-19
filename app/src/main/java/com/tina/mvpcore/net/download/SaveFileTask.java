package com.tina.mvpcore.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.tina.mvpcore.app.ProjectInit;
import com.tina.mvpcore.net.callback.IRequest;
import com.tina.mvpcore.net.callback.ISuccess;
import com.tina.mvpcore.util.file.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author yxc
 * @date 2018/11/19
 */
public class SaveFileTask extends AsyncTask<Object, Void, File>{

    private final IRequest REQUEST;

    private final ISuccess SUCCESS;

    public SaveFileTask(IRequest REQUEST, ISuccess SUCCESS) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
    }

    @Override
    protected File doInBackground(Object... params) {

        String downloadDir = (String)params[0];
        String extension = (String)params[1];
        ResponseBody body = (ResponseBody)params[2];
        String name = (String) params[3];
        InputStream is = body.byteStream();

        if (null == downloadDir || downloadDir.equals("")){
            downloadDir = "down_loads";
        }

        if (extension == null){
            extension = "";
        }

        if (name == null){
            return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
        }else {
            return FileUtil.writeToDisk(is, downloadDir, name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (SUCCESS != null){
            SUCCESS.onSuccess(file.getPath());
        }

        if (REQUEST != null){
            REQUEST.onRequestEnd();
        }
        //下到了APK直接安装
        autoInstallApk(file);
    }

    private void autoInstallApk(File file) {
        if (FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            ProjectInit.getApplicationContext().startActivity(install);
        }
    }


}
