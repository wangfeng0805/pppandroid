package com.ylfcf.ppp.Permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ylfcf.ppp.ui.BaseActivity;

/**
 * ��̬����Ȩ��base��
 * Created by Administrator on 2017/5/10.
 */

public class BasePermissionActivity extends BaseActivity implements EasyPermission.PermissionCallback{
    private int mRequestCode;
    private String[] mPermissions;
    private PermissionCallBackM mPermissionCallBack;

    //rationale: ������Ȩ����
    protected void requestPermission(int requestCode, String[] permissions, String rationale,
                                     PermissionCallBackM permissionCallback) {
        this.mRequestCode = requestCode;
        this.mPermissionCallBack = permissionCallback;
        this.mPermissions = permissions;

        EasyPermission.with(this)
                .addRequestCode(requestCode)
                .permissions(permissions)
                //.nagativeButtonText(android.R.string.ok)
                //.positveButtonText(android.R.string.cancel)
                .rationale(rationale)
                .request();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
            ��Settings������ת��������׼���룬����ôд
        */
        if (requestCode == EasyPermission.SETTINGS_REQ_CODE) {
            if (EasyPermission.hasPermissions(this, mPermissions)) {
                //����Ȩ������ҵ���߼�
                onEasyPermissionGranted(mRequestCode, mPermissions);
            } else {
                onEasyPermissionDenied(mRequestCode, mPermissions);
            }
        }
    }

    @Override public void onEasyPermissionGranted(int requestCode, String... perms) {
        if (mPermissionCallBack != null) {
            mPermissionCallBack.onPermissionGrantedM(requestCode, perms);
        }
    }

    @Override public void onEasyPermissionDenied(final int requestCode, final String... perms) {
        //rationale: Never Ask Again�����ʾ��Ϣ
        if (EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "ֻ�������Ȩ�޲��ܽ��д˲�����ȥ����ҳ�������Ȩ�ޣ�", android.R.string.ok,
                android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog,
                                                  int which) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onPermissionDeniedM(
                                    requestCode, perms);
                        }
                    }
                }, perms)) {
            return;
        }

        if (mPermissionCallBack != null) {
            mPermissionCallBack.onPermissionDeniedM(requestCode, perms);
        }
    }
}
