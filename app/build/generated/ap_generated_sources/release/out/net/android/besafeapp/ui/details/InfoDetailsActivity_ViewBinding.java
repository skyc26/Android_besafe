// Generated code from Butter Knife. Do not modify!
package net.android.besafeapp.ui.details;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import net.android.besafeapp.R;
import net.android.besafeapp.view.FrameEditText;

public class InfoDetailsActivity_ViewBinding implements Unbinder {
  private InfoDetailsActivity target;

  @UiThread
  public InfoDetailsActivity_ViewBinding(InfoDetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public InfoDetailsActivity_ViewBinding(InfoDetailsActivity target, View source) {
    this.target = target;

    target.liBack = Utils.findRequiredViewAsType(source, R.id.li_back, "field 'liBack'", LinearLayout.class);
    target.liTitle = Utils.findRequiredViewAsType(source, R.id.li_title, "field 'liTitle'", TextView.class);
    target.tvRight = Utils.findRequiredViewAsType(source, R.id.tv_right, "field 'tvRight'", TextView.class);
    target.frameUsername = Utils.findRequiredViewAsType(source, R.id.frame_username, "field 'frameUsername'", FrameEditText.class);
    target.frPhone = Utils.findRequiredViewAsType(source, R.id.frame_phone, "field 'frPhone'", FrameEditText.class);
    target.tvSex = Utils.findRequiredViewAsType(source, R.id.tv_sex, "field 'tvSex'", TextView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    target.tvLocation = Utils.findRequiredViewAsType(source, R.id.tv_location, "field 'tvLocation'", TextView.class);
    target.tvCategory = Utils.findRequiredViewAsType(source, R.id.tv_category, "field 'tvCategory'", TextView.class);
    target.edDescription = Utils.findRequiredViewAsType(source, R.id.ed_description, "field 'edDescription'", TextView.class);
    target.itemPhoto = Utils.findRequiredViewAsType(source, R.id.item_photo, "field 'itemPhoto'", ImageView.class);
    target.btnIssue = Utils.findRequiredViewAsType(source, R.id.btn_issue, "field 'btnIssue'", Button.class);
    target.ivState = Utils.findRequiredViewAsType(source, R.id.iv_state, "field 'ivState'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InfoDetailsActivity target = this.target;
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
    target.ivState = null;
  }
}
