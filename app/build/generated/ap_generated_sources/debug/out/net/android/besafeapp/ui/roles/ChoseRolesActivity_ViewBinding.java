// Generated code from Butter Knife. Do not modify!
package net.android.besafeapp.ui.roles;

import android.view.View;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.youth.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Override;
import net.android.besafeapp.R;

public class ChoseRolesActivity_ViewBinding implements Unbinder {
  private ChoseRolesActivity target;

  private View view7f080059;

  private View view7f08005a;

  @UiThread
  public ChoseRolesActivity_ViewBinding(ChoseRolesActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChoseRolesActivity_ViewBinding(final ChoseRolesActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_login, "field 'btnLogin' and method 'onBtnLoginClicked'");
    target.btnLogin = Utils.castView(view, R.id.btn_login, "field 'btnLogin'", Button.class);
    view7f080059 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBtnLoginClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_manager, "field 'btnManager' and method 'onBtnManagerClicked'");
    target.btnManager = Utils.castView(view, R.id.btn_manager, "field 'btnManager'", Button.class);
    view7f08005a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBtnManagerClicked();
      }
    });
    target.banner = Utils.findRequiredViewAsType(source, R.id.banner, "field 'banner'", Banner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChoseRolesActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnLogin = null;
    target.btnManager = null;
    target.banner = null;

    view7f080059.setOnClickListener(null);
    view7f080059 = null;
    view7f08005a.setOnClickListener(null);
    view7f08005a = null;
  }
}
