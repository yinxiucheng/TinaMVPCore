package com.tina.mvpcore.net.callback;

/**
 * @author yxc
 * @date 2018/11/19
 */
public interface IError {

    void onError(int code, String message);
}
