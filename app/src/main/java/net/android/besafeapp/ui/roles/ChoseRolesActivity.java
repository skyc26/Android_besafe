package net.android.besafeapp.ui.roles;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import net.android.besafeapp.R;
import net.android.besafeapp.base.BaseActivity;
import net.android.besafeapp.ui.login.LoginActivity;
import net.android.besafeapp.ui.main.MainActivity;
import net.android.besafeapp.ui.report.ReportActivity;
import net.android.besafeapp.util.MyLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoseRolesActivity extends BaseActivity {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_manager)
    Button btnManager;
    @BindView(R.id.banner)
    Banner banner;

    ChoseRolesViewModel viewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chose_roles;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initModel() {
        viewModel = ViewModelProviders.of(this).get(ChoseRolesViewModel.class);
        //轮播图
        viewModel.getBanner().observe(this, listSparseArray -> {
            //设置内置样式，共有六种可以点入方法内逐一体验使用。
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图片加载器，图片加载器在下方
            banner.setImageLoader(new MyLoader());
            //设置图片网址或地址的集合
            banner.setImages(listSparseArray.get(1));
            //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
            banner.setBannerAnimation(Transformer.Default);
            //设置轮播图的标题集合
            banner.setBannerTitles(listSparseArray.get(2));
            //设置轮播间隔时间
            banner.setDelayTime(3000);
            //设置是否为自动轮播，默认是“是”。
            banner.isAutoPlay(true);
            //设置指示器的位置，小点点，左中右。
            banner.setIndicatorGravity(BannerConfig.CENTER)
                    //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                    .start();
        });
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {
        StartForActivity(ReportActivity.class);
    }

    @OnClick(R.id.btn_manager)
    public void onBtnManagerClicked() {
        StartForActivity(LoginActivity.class);
    }

}
