package net.android.besafeapp.ui.login;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import net.android.besafeapp.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> mUserInfoMutableData;
    private Context mContext;

    private final int SUCCESS = 0;
    private final int FAILURE = 1;


    public LoginViewModel() {
        mUserInfoMutableData = new MutableLiveData<>();
    }


    public LiveData<String> getUserInfo(Context context, String userName, String passWord) {
        this.mContext = context;
        queryData(context, userName, passWord);
        return mUserInfoMutableData;
    }

    /**
     * 查询数据
     */
    private void queryData(Context context, String userName, String passWord) {
        if (userName.equals("admin") && passWord.equals("admin")){
            mUserInfoMutableData.postValue("登录成功");
        }else
            mUserInfoMutableData.postValue("用户名或密码错误");

    }


}
