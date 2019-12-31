package net.android.besafeapp.ui.roles;

import android.util.SparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.android.besafeapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChoseRolesViewModel extends ViewModel {
    MutableLiveData<SparseArray<List<String>>> BannerData;

    public ChoseRolesViewModel() {
        BannerData = new MutableLiveData<>();
    }

    public LiveData<SparseArray<List<String>>> getBanner() {
        //放图片地址的集合
        SparseArray array = new SparseArray();
        List list_path = new ArrayList<>();
        List titles = new ArrayList<>();

        list_path.add(R.mipmap.banner_one);
        list_path.add(R.mipmap.banner_two);
        list_path.add(R.mipmap.banner_three);
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        titles.add("");
        titles.add("");
        titles.add("");
//        titles.add("");
        array.put(1,list_path);
        array.put(2,titles);
        BannerData.postValue(array);
        return BannerData;
    }
}
