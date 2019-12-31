package net.android.besafeapp.ui.main;

import android.util.Log;
import android.util.SparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import net.android.besafeapp.R;
import net.android.besafeapp.db.Information;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainViewModel extends ViewModel {
    MutableLiveData<SparseArray<List<String>>> BannerData;
    MutableLiveData<List<Information>> InfoData;

    public MainViewModel() {
        BannerData = new MutableLiveData<>();
        InfoData = new MutableLiveData<>();
    }

    public LiveData<SparseArray<List<String>>> getBanner() {
        //放图片地址的集合
        SparseArray array = new SparseArray();
        List list_path = new ArrayList<>();
        List titles = new ArrayList<>();

        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        titles.add("");
        titles.add("");
        titles.add("");
        titles.add("");
        array.put(1,list_path);
        array.put(2,titles);
        BannerData.postValue(array);
        return BannerData;
    }

    public LiveData<List<Information>> getInfo(String category) {
        BmobQuery<Information> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("category",category);
        categoryBmobQuery.findObjects(new FindListener<Information>() {
            @Override
            public void done(List<Information> list, BmobException e) {
                if (e == null) {
                    InfoData.postValue(list);
                }
            }
        });
        return InfoData;
    }

    public LiveData<List<Information>> getAllInfo() {
        BmobQuery<Information> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<Information>() {
            @Override
            public void done(List<Information> list, BmobException e) {
                if (e == null) {
                    InfoData.postValue(list);
                }
            }
        });
        return InfoData;
    }
}
