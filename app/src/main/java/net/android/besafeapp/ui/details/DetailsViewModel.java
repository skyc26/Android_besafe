package net.android.besafeapp.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.android.besafeapp.db.Information;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class DetailsViewModel extends ViewModel {
    private MutableLiveData<Information> infoData;

    public DetailsViewModel() {
        infoData = new MutableLiveData<>();
    }

    public LiveData<Information> getInfo(String objectId){
        //查找Person表里面id为6b6c11c537的数据
        BmobQuery<Information> bmobQuery = new BmobQuery<Information>();
        bmobQuery.getObject(objectId, new QueryListener<Information>() {
            @Override
            public void done(Information info, BmobException e) {
                if(e==null){
                    infoData.postValue(info);
                }
            }
        });
        return infoData;
    }
}
