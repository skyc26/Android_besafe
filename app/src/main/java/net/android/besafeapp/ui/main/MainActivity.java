package net.android.besafeapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import net.android.besafeapp.R;
import net.android.besafeapp.adapter.UserAdapter;
import net.android.besafeapp.base.BaseActivity;
import net.android.besafeapp.ui.details.InfoDetailsActivity;
import net.android.besafeapp.util.MyLoader;
import net.android.besafeapp.view.ConfirmPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    MainViewModel mainViewModel;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.btn_add_city)
    TextView btnAddCity;

    private UserAdapter adapter;
    private int identity;

    private ConfirmPopWindow popWindow;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //列表
        mainViewModel.getAllInfo().observe(this, information -> {
            if (information.size() > 0) {
                adapter = new UserAdapter(MainActivity.this, information);
                recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recycler.setAdapter(adapter);
                adapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent();
                    intent.putExtra("objectId", information.get(position).getObjectId());
                    intent.setClass(MainActivity.this, InfoDetailsActivity.class);
                    startActivity(intent);
                });
            }
        });
        btnAddCity.setOnClickListener(v->{
            popWindow = new ConfirmPopWindow(MainActivity.this);
            popWindow.showAtBottom(btnAddCity);
            popWindow.setOnitemClckListener(new ConfirmPopWindow.OnItemClickListener() {
                @Override
                public void itemClickListener(int position) {
                    switch (position){
                        case 1:
                            //列表
                            mainViewModel.getInfo("举报").observe(MainActivity.this, information -> {
                                if (information!=null && information.size()>0) {
                                    adapter.setNewData(information);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case 2:
                            mainViewModel.getInfo("居民消防检查").observe(MainActivity.this, information -> {
                                if (information!=null && information.size()>0){
                                    adapter.setNewData(information);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case 3:
                            mainViewModel.getInfo("商户消防检查").observe(MainActivity.this, information -> {
                                if (information!=null && information.size()>0){
                                    adapter.setNewData(information);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                    }
                }
            });
        });
    }



}
