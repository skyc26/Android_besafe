package net.android.besafeapp.ui.login;

import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.blankj.utilcode.util.SPUtils;

import net.android.besafeapp.ui.main.MainActivity;
import net.android.besafeapp.R;
import net.android.besafeapp.base.BaseActivity;

import net.android.besafeapp.util.ToastUtils;
import net.android.besafeapp.view.FrameEditText;

import butterknife.BindView;


/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.frame_username)
    FrameEditText frameUsername;
    @BindView(R.id.frame_password)
    FrameEditText framePassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private String userName;
    private String password;

    private LoginViewModel viewModel;

    char numberChars[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};


    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        frameUsername.setKeyListener(numberChars);
        frameUsername.setInputType(InputType.TYPE_CLASS_TEXT);
        framePassword.setInputType(0x81);
    }

    @Override
    protected void initModel() {


    }


    @Override
    public void initListener() {
        super.initListener();
        btnLogin.setOnClickListener(v -> {
            userName = frameUsername.getString();
            password = framePassword.getString();
            if (userName == null || password == null) {
                ToastUtils.show(this, "用户名或密码不能为空");
                return;
            }
            viewModel.getUserInfo(this, userName, password).observe(LoginActivity.this, new Observer<String>() {
                @Override
                public void onChanged(String message) {
                    if (message.equals("登录成功")) {
                        //消防员
                        StartForActivity(MainActivity.class);
                        ToastUtils.show(LoginActivity.this, "登录成功");
                        finish();
                    }else {
                        ToastUtils.show(LoginActivity.this, message);
                    }
                }
            });
        });

    }
}
