
package com.heneng.launcher.view.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

import com.blxt.power.PowerReceiver;
import com.blxt.power.PowerService;
import com.blxt.quickactivity.QPermissionActivity;
import com.blxt.quicklog.QLog;
import com.heneng.launcher.R;
import com.heneng.launcher.view.base.BaseActivity;
import com.heneng.launcher.view.viewholder.activityview.MainTimeViewHolder;
import com.heneng.launcher.view.viewholder.activityview.MainViewHolder;

import static com.heneng.launcher.model.MConstant.MSGID_APP_STARTACTIVITY;

public class MainActivity extends BaseActivity {
    final static String TAG = "MainActivity";

    public static final String  SD_PATH = Environment.getExternalStorageDirectory().getPath();
    
    public static Context mContext;

    MainViewHolder mainViewHolder;

    MainTimeViewHolder timeViewHolder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewHolder = new MainViewHolder(getWindow().getDecorView(), this);
        mainViewHolder.setCallbckHandler(handler);

        timeViewHolder = new MainTimeViewHolder(getWindow().getDecorView());

//        FAL_PERMISSIONS_CHAEK_WINDOWS = false;
//        FAL_PERMISSIONS_CHAEK_NOTIFICATION = false;
//        FAL_PERMISSIONS_CHAEK_SYSTEM = false;
//        FAL_PERMISSIONS_CHAEK_APP_AUTO_RUN = false;
//        FAL_PERMISSIONS_CHAEK_ADMIN = false;
//        permissions = null;
//        callBack = new MPermissionCallBack();

     //   checkDevicePolicyManager(getBaseContext(), PowerReceiver.class);

        mContext = this;

        Uri uri = Uri.parse("content://com.blxt.usermanage/query"); //路径和你定义的路径一样

        Cursor cursor1 = getContentResolver().query(uri, null, null,
                null, null
        );

        if (cursor1 != null && cursor1.getCount() > 0) {
            while (cursor1.moveToNext()) {
                String name = cursor1.getString(1);
                String phone = cursor1.getString(2);
                System.out.println("第二个应用:name==>" + name + "   phone==>" + phone);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewHolder.onResume();

    }

    @Override
    public void doMessage( Message message) {

        mainViewHolder.doMessage(message);

        switch (message.what){
            case MSGID_APP_STARTACTIVITY: // 启动Activity
                Class<?> cls = (Class)message.obj;
                QLog.d(this, "启动Activity" + cls);
                final Intent intent = new Intent(this, cls);
                startActivity(intent);
                break;
        }
    }

//    /**
//     * 权限检查结果回调
//     */
//    private class MPermissionCallBack implements QPermissionActivity.PermissionCallBack{
//
//        @Override
//        public void onCheckPermissionResult(boolean result) {
//            QLog.i(TAG,1, "onCheckPermissionResult",  result);
//        }
//
//        @Override
//        public void onNotificationResult(boolean result) {
//            QLog.i(TAG,2, "onNotificationResult",  result);
//        }
//
//        @Override
//        public void onWindowsCheckResult(boolean result) {
//            QLog.i(TAG,3, "onWindowsCheckResult",  result);
//        }
//
//        @Override
//        public void onSystemCheckResult(boolean result) {
//            QLog.i(TAG,4, "onSystemCheckResult",  result);
//        }
//
//        @Override
//        public void onDevicePolicyManager(boolean result) {
//            QLog.i(TAG,4, "onDevicePolicyManager",  result);
//            if(result){
//                PowerService.newInstance(getBaseContext());
//            }
//        }
//    }
}
