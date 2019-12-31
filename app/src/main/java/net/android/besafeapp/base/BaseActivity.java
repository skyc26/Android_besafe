package net.android.besafeapp.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mBinder = ButterKnife.bind(this);
        initView();
        initModel();
        initListener();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initModel();

    public void initListener() {
    }

    public void StartForActivity(Class clz) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        startActivity(intent);
    }

    public void StartActivityAndFinsh(Class clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra("type",bundle);
        intent.setClass(this, clz);
        startActivity(intent);
        finish();
    }

}
