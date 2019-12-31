package net.android.besafeapp;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("fade7aaeb90b16ecc9b3291a5d536eee")//设置appkey
                .setConnectTimeout(15)//请求超时时间（单位为秒）：默认15s
                .setUploadBlockSize(1024 * 1024)//文件分片上传时每片的大小（单位字节），默认512*1024
                .setFileExpiration(2500)//文件的过期时间(单位为秒)：默认1800s
                .build();
        Bmob.initialize(config);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
    }
}
