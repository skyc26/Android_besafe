package net.android.besafeapp.ui.details;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.adorkable.iosdialog.AlertDialog;
import com.squareup.picasso.Picasso;

import net.android.besafeapp.R;
import net.android.besafeapp.base.BaseActivity;
import net.android.besafeapp.db.Information;
import net.android.besafeapp.ui.login.LoginActivity;
import net.android.besafeapp.ui.main.MainActivity;
import net.android.besafeapp.ui.report.ReportActivity;
import net.android.besafeapp.util.ToastUtils;
import net.android.besafeapp.view.FrameEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class InfoDetailsActivity extends BaseActivity {
    @BindView(R.id.li_back)
    LinearLayout liBack;
    @BindView(R.id.li_title)
    TextView liTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.frame_username)
    FrameEditText frameUsername;
    @BindView(R.id.frame_phone)
    FrameEditText frPhone;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.ed_description)
    TextView edDescription;
    @BindView(R.id.item_photo)
    ImageView itemPhoto;
    @BindView(R.id.btn_issue)
    Button btnIssue;
    @BindView(R.id.iv_state)
    ImageView ivState;
    private String objectId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_info_details;
    }

    @Override
    protected void initView() {
        liBack.setOnClickListener(v -> finish());
        liTitle.setText("详情");
        objectId = getIntent().getStringExtra("objectId");
    }

    @Override
    protected void initModel() {
        DetailsViewModel detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        detailsViewModel.getInfo(objectId).observe(this, information -> {
            frameUsername.setText(information.getName());
            frPhone.setText(information.getPhone());
            tvSex.setText(information.getName());
            tvTime.setText(information.getTime());
            tvLocation.setText(information.getAddress());
            tvCategory.setText(information.getCategory());
            edDescription.setText(information.getDescription());
            switch (information.getStatus()) {
                case 0:
                    ivState.setBackgroundResource(R.mipmap.icon_one);
                    break;
                case 1:
                    ivState.setBackgroundResource(R.mipmap.icon_two);
                    break;
                case 2:
                    ivState.setBackgroundResource(R.mipmap.icon_three);
                    break;
            }
            if (information.getFile() != null) {
                Picasso.with(InfoDetailsActivity.this)
                        .load("file://" +information.getFile())
                        //加载过程中的图片显示
                        .placeholder(R.mipmap.icon_add)
                        //加载失败中的图片显示
                        //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                        .into(itemPhoto);
            }
            btnIssue.setOnClickListener(v -> {
                // 显示帐号在其他设备登录
                new AlertDialog(InfoDetailsActivity.this).builder().setTitle("提示")
                        .setMsg("是否通过审核")
                        .setPositiveButton("确定", v1 -> {
                            information.setStatus(1);
                            information.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ToastUtils.show(InfoDetailsActivity.this, "审核成功");
                                        finish();
                                    } else {
                                        ToastUtils.show(InfoDetailsActivity.this, "审核失败");
                                    }
                                }
                            });
                        })
                        .setNegativeButton("拒绝", v12 -> {
                            information.setStatus(2);
                            information.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ToastUtils.show(InfoDetailsActivity.this, "拒绝成功");
                                        finish();
                                    } else {
                                        ToastUtils.show(InfoDetailsActivity.this, "拒绝失败");
                                    }
                                }
                            });
                        }).show();
            });
        });
    }

}
