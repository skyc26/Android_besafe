// This file was generated by PermissionsDispatcher. Do not modify!
package net.android.besafeapp.ui.report;

import androidx.core.app.ActivityCompat;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class ReportActivityPermissionsDispatcher {
  private static final int REQUEST_GETLOCATION = 0;

  private static final String[] PERMISSION_GETLOCATION = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.ACCESS_COARSE_LOCATION"};

  private static final int REQUEST_APPLYSUCCESS = 1;

  private static final String[] PERMISSION_APPLYSUCCESS = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.ACCESS_COARSE_LOCATION"};

  private ReportActivityPermissionsDispatcher() {
  }

  static void getLocationWithCheck(ReportActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_GETLOCATION)) {
      target.getLocation();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_GETLOCATION)) {
        target.showRationaleForMap(new GetLocationPermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_GETLOCATION, REQUEST_GETLOCATION);
      }
    }
  }

  static void ApplySuccessWithCheck(ReportActivity target) {
    if (PermissionUtils.hasSelfPermissions(target, PERMISSION_APPLYSUCCESS)) {
      target.ApplySuccess();
    } else {
      if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_APPLYSUCCESS)) {
        target.showRationaleForMap(new ApplySuccessPermissionRequest(target));
      } else {
        ActivityCompat.requestPermissions(target, PERMISSION_APPLYSUCCESS, REQUEST_APPLYSUCCESS);
      }
    }
  }

  static void onRequestPermissionsResult(ReportActivity target, int requestCode,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_GETLOCATION:
      if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_GETLOCATION)) {
        target.onMapDenied();
        return;
      }
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.getLocation();
      } else {
        if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_GETLOCATION)) {
          target.onMapNeverAskAgain();
        } else {
          target.onMapDenied();
        }
      }
      break;
      case REQUEST_APPLYSUCCESS:
      if (PermissionUtils.getTargetSdkVersion(target) < 23 && !PermissionUtils.hasSelfPermissions(target, PERMISSION_APPLYSUCCESS)) {
        target.onMapDenied();
        return;
      }
      if (PermissionUtils.verifyPermissions(grantResults)) {
        target.ApplySuccess();
      } else {
        if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_APPLYSUCCESS)) {
          target.onMapNeverAskAgain();
        } else {
          target.onMapDenied();
        }
      }
      break;
      default:
      break;
    }
  }

  private static final class GetLocationPermissionRequest implements PermissionRequest {
    private final WeakReference<ReportActivity> weakTarget;

    private GetLocationPermissionRequest(ReportActivity target) {
      this.weakTarget = new WeakReference<>(target);
    }

    @Override
    public void proceed() {
      ReportActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_GETLOCATION, REQUEST_GETLOCATION);
    }

    @Override
    public void cancel() {
      ReportActivity target = weakTarget.get();
      if (target == null) return;
      target.onMapDenied();
    }
  }

  private static final class ApplySuccessPermissionRequest implements PermissionRequest {
    private final WeakReference<ReportActivity> weakTarget;

    private ApplySuccessPermissionRequest(ReportActivity target) {
      this.weakTarget = new WeakReference<>(target);
    }

    @Override
    public void proceed() {
      ReportActivity target = weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSION_APPLYSUCCESS, REQUEST_APPLYSUCCESS);
    }

    @Override
    public void cancel() {
      ReportActivity target = weakTarget.get();
      if (target == null) return;
      target.onMapDenied();
    }
  }
}
