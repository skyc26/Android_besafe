package net.android.besafeapp.ui.report;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.squareup.picasso.Picasso;

import net.android.besafeapp.R;
import net.android.besafeapp.base.BaseActivity;
import net.android.besafeapp.db.Information;
import net.android.besafeapp.ui.ChoseLocationActivity;
import net.android.besafeapp.util.LocationUtils;
import net.android.besafeapp.util.ToastUtils;
import net.android.besafeapp.view.FrameEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import sakura.bottommenulibrary.bottompopfragmentmenu.BottomMenuFragment;
import sakura.bottommenulibrary.bottompopfragmentmenu.MenuItem;

@RuntimePermissions
public class ReportActivity extends BaseActivity {
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
    EditText edDescription;
    @BindView(R.id.item_photo)
    ImageView itemPhoto;
    @BindView(R.id.btn_issue)
    Button btnIssue;


    private LocationUtils utils;
    ReportViewModel reportViewModel;
    private List<LocalMedia> selectList = new ArrayList<>();

    char numberChars[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView() {

    //    frameUsername.setKeyListener(numberChars);
        frPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        liBack.setOnClickListener(v -> finish());
        liTitle.setText("举报");
    }


    @Override
    protected void initModel() {
        tvTime.setOnClickListener(v -> choseTime());
        tvSex.setOnClickListener(v -> choseSex());
        tvLocation.setOnClickListener(v -> {
            //申请单个权限
            Intent intent = new Intent(ReportActivity.this, ChoseLocationActivity.class);
            startActivityForResult(intent, 1);
        });
        utils = new LocationUtils();

        //申请单个权限
        ReportActivityPermissionsDispatcher.getLocationWithCheck(this);

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        tvCategory.setOnClickListener(v -> choseCategory());
    }

    public void choseTime() {
        Calendar calendar = Calendar.getInstance();
        final DatePicker picker = new DatePicker(this);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setCancelText("取消");
        picker.setSubmitText("确定");
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> tvTime.setText(year + " - " + month + " - " + day));
        picker.show();
    }

    public void choseSex() {
        String[] arr = new String[]{"男", "女"};

        SinglePicker<String> picker = new SinglePicker<>(this, arr);
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("角色选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(13);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(14);
        picker.setCancelText("取消");
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(14);
        picker.setSubmitText("确定");
        picker.setSelectedTextColor(0xFFEE0000);
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(0xFFE1E1E1);
        picker.setSelectedIndex(7);
        picker.setOnItemPickListener((index, item) -> {
            tvSex.setText(item);
        });
        picker.show();
    }

    private void choseCategory() {
        String[] arr = new String[]{"举报", "居民消防检查", "商户消防检查"};

        SinglePicker<String> picker = new SinglePicker<>(this, arr);
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("角色选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(13);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(14);
        picker.setCancelText("取消");
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(14);
        picker.setSubmitText("确定");
        picker.setSelectedTextColor(0xFFEE0000);
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(0xFFE1E1E1);
        picker.setSelectedIndex(7);
        picker.setOnItemPickListener((index, item) -> {
            tvCategory.setText(item);
        });
        picker.show();
    }

    @OnClick(R.id.btn_issue)
    public void Issue() {
        String name = frameUsername.getString();
        String phone = frPhone.getString();
        String sex = tvSex.getText().toString();
        String time = tvTime.getText().toString();
        String location = tvLocation.getText().toString();
        String Category = tvCategory.getText().toString();
        String description = edDescription.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show(ReportActivity.this, "用户名不能为空");
        }else if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(ReportActivity.this, "联系电话不能为空");
        } else if (TextUtils.isEmpty(sex)) {
            ToastUtils.show(ReportActivity.this, "性别不能为空");
        } else if (TextUtils.isEmpty(time)) {
            ToastUtils.show(ReportActivity.this, "时间不能为空");
        } else if (TextUtils.isEmpty(location)) {
            ToastUtils.show(ReportActivity.this, "位置不能为空");
        }else if (TextUtils.isEmpty(Category)) {
            ToastUtils.show(ReportActivity.this, "类别不能为空");
        } else if (TextUtils.isEmpty(description)) {
            ToastUtils.show(ReportActivity.this, "描述不能为空");
        } else {
            Information info = new Information();
            info.setName(name);
            info.setPhone(phone);
            info.setSex(sex);
            info.setTime(time);
            info.setDescription(description);
            info.setAddress(location);
            info.setCategory(Category);
            info.setStatus(0);

            if (selectList.size() > 0) {
                info.setFile(selectList.get(0).getCutPath());
            }
            info.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        ToastUtils.show(ReportActivity.this, "添加成功");
                        finish();
                    } else {
                        ToastUtils.show(ReportActivity.this, "添加失败");
                    }
                }
            });
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getLocation() {
        utils.setOnlocationListener(address -> {
            if (!TextUtils.isEmpty(address)) {
                tvLocation.setText(address);
            }
        });
    }

    @OnClick(R.id.item_photo)
    public void choseImage() {
        new BottomMenuFragment(ReportActivity.this)
                .addMenuItems(new MenuItem("从相册选择"))
                .addMenuItems(new MenuItem("拍照"))
                .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(TextView menu_item, int position) {
                        switch (position) {
                            case 0:
                                ChosePhone();
                                break;
                            case 1:
                                openCamera();
                                break;
                        }
                    }
                })
                .show();

    }

    public void ChosePhone() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void openCamera() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    List<File> photoList = new ArrayList<>();
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getCutPath());
                        photoList.add(new File(media.getCutPath()));
                    }
                    Picasso.with(ReportActivity.this)
                            .load("file://" + selectList.get(0).getCutPath())
                            //加载过程中的图片显示
                            .placeholder(R.mipmap.icon_add)
                            //加载失败中的图片显示
                            //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                            .into(itemPhoto);

                    break;
                case 1:
                    tvLocation.setText(data.getStringExtra("location"));
                    break;
            }
        }

    }

    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/ai/image/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(ReportActivity.this, "文件夹创建成功", Toast.LENGTH_LONG).show();
        }
        return path;
    }

    private void initLister() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(ReportActivity.this);
                } else {
                    Toast.makeText(ReportActivity.this, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ReportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功时
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION})
    void ApplySuccess() {
        getLocation();
    }


    /**
     * 申请权限告诉用户原因时
     *
     * @param request
     */
    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForMap(PermissionRequest request) {
        showRationaleDialog("使用此功能需要打开定位的权限", request);
    }

    /**
     * 申请权限被拒绝时
     */
    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION})
    void onMapDenied() {
        Toast.makeText(this, "你拒绝了权限，该功能不可用", Toast.LENGTH_LONG).show();
    }

    /**
     * 申请权限被拒绝并勾选不再提醒时
     */
    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION})
    void onMapNeverAskAgain() {
        AskForPermission();
    }

    /**
     * 告知用户具体需要权限的原因
     *
     * @param messageResId
     * @param request
     */
    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();//请求权限
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前应用缺少定位权限,请去设置界面打开\n打开之后按两次返回键可回到该应用哦");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + ReportActivity.this.getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

}
