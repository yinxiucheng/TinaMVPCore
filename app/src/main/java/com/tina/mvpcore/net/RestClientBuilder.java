package com.tina.mvpcore.net;

import com.tina.mvpcore.net.callback.IError;
import com.tina.mvpcore.net.callback.IFailure;
import com.tina.mvpcore.net.callback.IRequest;
import com.tina.mvpcore.net.callback.ISuccess;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author yxc
 * @date 2018/11/19
 */
public class RestClientBuilder {

    private HashMap<String, Object> mParams;

    private String mUrl;

    private ISuccess mSuccess;

    private IRequest mRequest;

    private IFailure mFailure;

    private IError mError;

    private RequestBody mBody;

    RestClientBuilder() {

    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(HashMap<String, Object> params){
        this.mParams = params;
        return this;
    }

    public final RestClientBuilder success(ISuccess success){
        this.mSuccess = success;
        return this;
    }

    public final RestClientBuilder request(IRequest request){
        this.mRequest = request;
        return this;
    }

    public final RestClientBuilder failure(IFailure failure){
        this.mFailure = failure;
        return this;
    }

    public final RestClientBuilder error(IError error){
        this.mError = error;
        return this;
    }


    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }



}
