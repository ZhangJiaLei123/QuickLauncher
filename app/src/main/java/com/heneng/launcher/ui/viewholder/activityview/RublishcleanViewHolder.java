package com.heneng.launcher.ui.viewholder.activityview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blxt.quickactivity.AbstractViewHolder;
import com.blxt.quicklog.QLog;
import com.blxt.view.ArcProgress;
import com.heneng.launcher.R;
import com.blxt.safety.bean.CacheListItem;
import com.blxt.safety.bean.StorageSize;
import com.heneng.launcher.ui.adapter.RublishMemoryAdapter;
import com.blxt.safety.util.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.heneng.launcher.model.MConstant.MSGID_ACTIVITY_FINISH;
import static com.heneng.launcher.model.MConstant.MSGID_RUBLISHCLEAN_ONCLEAR;
import static com.heneng.launcher.model.MConstant.MSGID_RUBLISHCLEAN_ONSCANCOMPLETED;

public class RublishcleanViewHolder extends AbstractViewHolder {

    ListView mListView;
    ArcProgress rubbish_arc_store;
    View mProgressBar;
    TextView mProgressBarText;
    //中间信息
    private TextView rubbish_capacity;

    Button clearButton;
    Button rubbish_oneKeydo;

    RublishMemoryAdapter rublishMemoryAdapter;


    List<CacheListItem> mCacheListItem = new ArrayList<>();


    public RublishcleanViewHolder(@NonNull View view) {
        super(view);

        mListView = findViewById(R.id.rubbish_list);
        rubbish_arc_store = findViewById(R.id.rubbish_arc_store);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBarText = findViewById(R.id.progressBarText);
        rubbish_capacity = findViewById(R.id.rubbish_capacity);
        clearButton = findViewById(R.id.rubbish_oneKeydo);


        rublishMemoryAdapter = new RublishMemoryAdapter(getContext(), mCacheListItem);
        mListView.setAdapter(rublishMemoryAdapter);
        mListView.setOnItemClickListener(rublishMemoryAdapter);


        rubbish_oneKeydo = findViewById(R.id.rubbish_oneKeydo);//
        rubbish_oneKeydo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rubbish_oneKeydo.getText().equals("完成")) {
                    //finish();
                    callbck.sendEmptyMessage(MSGID_ACTIVITY_FINISH);
                } else {
                    onClickClear();
                    rubbish_oneKeydo.setText("完成");
                }
            }
        });

    }

    public void setHintT(String message) {
        rubbish_capacity.setText(message);
    }


    @Override
    public boolean upData(Object o) {
        return false;
    }

    public void onScanStarted(Context context) {
        mProgressBarText.setText(R.string.scanning);
        showProgressBar(true);
    }

    public void onScanProgressUpdated(Context context, int current, int max) {
        mProgressBarText.setText(getContext().getString(R.string.scanning_m_of_n, current, max));

    }

    public void onScanCompleted(Context context, List<CacheListItem> apps, long medMemory) {
        showProgressBar(false);
        mCacheListItem.clear();
        mCacheListItem.addAll(apps);
        rublishMemoryAdapter.notifyDataSetChanged();
        callbck.sendEmptyMessage(MSGID_RUBLISHCLEAN_ONSCANCOMPLETED);


        System.out.println(medMemory);
        float cachsize = 0;
        for (CacheListItem cacheListItem : apps) {
            cachsize += cachsize + cacheListItem.getCacheSize();
        }
        StorageSize mStorageSize = StorageUtil.convertStorageSize(medMemory);
        System.out.println("mStorageSize.value" + mStorageSize.value);
        if (mStorageSize.value == 0.0) {
            rubbish_arc_store.setSuffixText("");
            rubbish_arc_store.setBottomText("暂无垃圾");
            rubbish_capacity.setText("");
        } else {
            rubbish_arc_store.setProgress(mStorageSize.value);
            rubbish_arc_store.setSuffixText(mStorageSize.suffix);
        }

    }

    public void onCleanStarted(Context context) {
        if (isProgressBarVisible()) {
            showProgressBar(false);
        }

    }

    @SuppressLint("StringFormatInvalid")
    public void onCleanCompleted(Context context, long cacheSize) {
        Toast.makeText(context, context.getString(R.string.cleaned, Formatter.formatShortFileSize(
                getContext(), cacheSize)), Toast.LENGTH_LONG).show();
        mCacheListItem.clear();
        rublishMemoryAdapter.notifyDataSetChanged();
    }

    public void onClickClear() {
        callbck.sendEmptyMessage(MSGID_RUBLISHCLEAN_ONCLEAR);

        rubbish_arc_store.setProgress(0);
        rubbish_arc_store.setSuffixText("");
        rubbish_arc_store.setBottomText("已清理");
        QLog.d(this, "已清理");
    }


    private boolean isProgressBarVisible() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                    getContext(), android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
