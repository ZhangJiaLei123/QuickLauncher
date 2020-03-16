package com.blxt.safety.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.blxt.quickactivity.AbstractFullActivityQ;
import com.blxt.quicklog.QLog;
import com.heneng.launcher.R;
import com.blxt.safety.bean.AppProcessInfo;
import com.blxt.safety.bean.CacheListItem;
import com.blxt.safety.bean.StorageSize;
import com.blxt.safety.service.CleanerService;
import com.heneng.launcher.view.viewholder.activityview.RublishcleanViewHolder;
import com.blxt.safety.util.AppUtil;
import com.blxt.safety.util.StorageUtil;

import java.util.List;

import static com.heneng.launcher.model.MConstant.MSGID_ACTIVITY_FINISH;
import static com.heneng.launcher.model.MConstant.MSGID_RUBLISHCLEAN_ONCLEAR;
import static com.heneng.launcher.model.MConstant.MSGID_RUBLISHCLEAN_ONUPDATA;

/**
 * 扫描垃圾
 *
 * @author 文江
 */
public class RublishcleanActivity extends AbstractFullActivityQ implements CleanerService.OnActionListener {

    private CleanerService mCleanerService;

    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned = false;

    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doMessage(msg);
        }
    };


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCleanerService = ((CleanerService.CleanerServiceBinder) service).getService();
            mCleanerService.setOnActionListener(RublishcleanActivity.this);
            if (!mCleanerService.isScanning() && !mAlreadyScanned) {
                mCleanerService.scanCache();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
        }
    };

    RublishcleanViewHolder viewHolder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);

        viewHolder = new RublishcleanViewHolder(getWindow().getDecorView());
        viewHolder.setCallbckHandler(handler);


        Thread thread = new Thread(new Runnable() {
            //获取运行内存大小
            @Override
            public void run() {
                List<AppProcessInfo> list = AppUtil.getRunningAppProcesses(getApplicationContext());
                QLog.e(this, "运行进程=" + list.size());

                long alMemory = AppUtil.getTotalMemory(activity);
                long availMemory = AppUtil.getAvailMemory(activity);
                StorageSize alMemorySize = StorageUtil.convertStorageSize(alMemory);
                StorageSize availMemorySize = StorageUtil.convertStorageSize(availMemory);
                Message msMessage = Message.obtain();
                msMessage.what = MSGID_RUBLISHCLEAN_ONUPDATA;
                String value = (alMemorySize.value + "").substring(0, 4);
                String valueall = (availMemorySize.value + "").substring(0, 4);
                msMessage.obj = valueall + alMemorySize.suffix + "/"
                        + value + availMemorySize.suffix;

                handler.sendMessage(msMessage);
            }
        });
        thread.start();

        applyKitKatTranslucency();

        bindService(new Intent(this, CleanerService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScanStarted(Context context) {
        viewHolder.onScanStarted(context);
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max) {
        viewHolder.onScanProgressUpdated(context, current, max);
    }

    @Override
    public void onScanCompleted(Context context, List<CacheListItem> apps) {
        long medMemory = mCleanerService != null ? mCleanerService.getCacheSize() : 0;

        viewHolder.onScanCompleted(context, apps, medMemory);
    }

    @Override
    public void onCleanStarted(Context context) {
        viewHolder.onCleanStarted(context);
        if (!RublishcleanActivity.this.isFinishing()) {
            //showDialogLoading();
        }
    }


    @SuppressLint("StringFormatInvalid")
    @Override
    public void onCleanCompleted(Context context, long cacheSize) {
        viewHolder.onCleanCompleted(context, cacheSize);
        //  dismissDialogLoading();
        Toast.makeText(context, context.getString(R.string.cleaned, Formatter.formatShortFileSize(
                this, cacheSize)), Toast.LENGTH_SHORT).show();

    }


    private void applyKitKatTranslucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            setTranslucentStatus(true);
        }
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    public void doMessage(Message msg) {
        switch (msg.what) {
            case MSGID_RUBLISHCLEAN_ONUPDATA: // 更新Hint
                String message = (String) msg.obj;
                viewHolder.setHintT(message);
                break;
            case MSGID_ACTIVITY_FINISH: // 结束
                finish();
                break;
            case MSGID_RUBLISHCLEAN_ONCLEAR: // 开始清理
                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
                    mAlreadyCleaned = false;
                   // QLog.i(this, "开始清理");
                    mCleanerService.cleanCache();
                }
                break;

            default:
                break;
        }
    }


}
