package com.tina.mvpcore.app;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * @author yxc
 * @date 2018/11/19
 */
public class Configurator {

    private static final HashMap<Object, Object> CONFIGS = new HashMap<>();

    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();


    //单例
    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }

    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }

    private Configurator(){
        CONFIGS.put(ConfigKeys.CONFIG_READY.name(), false);

    }

    //获取配置信息
    final HashMap<Object, Object> getConfigs(){
        return CONFIGS;
    }


    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) CONFIGS.get(key);
    }


    //配置APIHOST
    public final Configurator withApiHost(String host){
        CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }

    //检查配置是否完成
    private void checkConfiguration(){
        final boolean isReady=(boolean)CONFIGS.get(ConfigKeys.CONFIG_READY.name());
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call configure()");
        }
    }

    //配置完成
    public final void configure(){
        CONFIGS.put(ConfigKeys.CONFIG_READY.name(),true);
    }

}
