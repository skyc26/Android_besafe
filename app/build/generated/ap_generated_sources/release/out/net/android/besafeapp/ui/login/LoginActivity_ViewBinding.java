// Generated code from Butter Knife. Do not modify!
package net.android.besafeapp.ui.login;

import android.view.View;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import net.android.besafeapp.R;
import net.android.besafeapp.view.FrameEditText;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.frameUsername = Utils.findRequiredViewAsType(source, R.id.frame_username, "field 'frameUsername'", FrameEditText.class);
    target.framePassword = Utils.findRequiredViewAsType(source, R.id.frame_password, "field 'framePassword'", FrameEditText.class);
    target.btnLogin = Utils.findRequiredViewAsType(source, R.id.btn_login, "field 'btnLogin'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.frameUsername = null;
    target.framePassword = null;
    target.btnLogin = null;
  }
}
