// Generated code from Butter Knife. Do not modify!
package net.android.besafeapp.ui.report;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import net.android.besafeapp.R;
import net.android.besafeapp.view.FrameEditText;

public class ReportActivity_ViewBinding implements Unbinder {
  private ReportActivity target;

  private View view7f0800b1;

  private View view7f080058;

  @UiThread
  public ReportActivity_ViewBinding(ReportActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReportActivity_ViewBinding(final ReportActivity target, View source) {
    this.target = target;

    View view;
    target.liBack = Utils.findRequiredViewAsType(source, R.id.li_back, "field 'liBack'", LinearLayout.class);
    target.liTitle = Utils.findRequiredViewAsType(source, R.id.li_title, "field 'liTitle'", TextView.class);
    target.tvRight = Utils.findRequiredViewAsType(source, R.id.tv_right, "field 'tvRight'", TextView.class);
    target.frameUsername = Utils.findRequiredViewAsType(source, R.id.frame_username, "field 'frameUsername'", FrameEditText.class);
    target.frPhone = Utils.findRequiredViewAsType(source, R.id.frame_phone, "field 'frPhone'", FrameEditText.class);
    target.tvSex = Utils.findRequiredViewAsType(source, R.id.tv_sex, "field 'tvSex'", TextView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    target.tvLocation = Utils.findRequiredViewAsType(source, R.id.tv_location, "field 'tvLocation'", TextView.class);
    target.tvCategory = Utils.findRequiredViewAsType(source, R.id.tv_category, "field 'tvCategory'", TextView.class);
    target.edDescription = Utils.findRequiredViewAsType(source, R.id.ed_description, "field 'edDescription'", EditText.class);
    view = Utils.findRequiredView(source, R.id.item_photo, "field 'itemPhoto' and method 'choseImage'");
    target.itemPhoto = Utils.castView(view, R.id.item_photo, "field 'itemPhoto'", ImageView.class);
    view7f0800b1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.choseImage();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_issue, "field 'btnIssue' and method 'Issue'");
    target.btnIssue = Utils.castView(view, R.id.btn_issue, "field 'btnIssue'", Button.class);
    view7f080058 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.Issue();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ReportActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.liBack = null;
    target.liTitle = null;
    target.tvRight = null;
    target.frameUsername = null;
    target.frPhone = null;
    target.tvSex = null;
    target.tvTime = null;
    target.tvLocation = null;
    target.tvCategory = null;
    target.edDescription = null;
    target.itemPhoto = null;
    target.btnIssue = null;

    view7f0800b1.setOnClickListener(null);
    view7f0800b1 = null;
    view7f080058.setOnClickListener(null);
    view7f080058 = null;
  }
}
