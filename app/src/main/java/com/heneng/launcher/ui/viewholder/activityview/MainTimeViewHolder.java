package com.heneng.launcher.ui.viewholder.activityview;

import android.view.View;

import androidx.annotation.NonNull;

import com.blxt.quickactivity.AbstractViewHolder;
import com.blxt.quickview.time.NumberTimeBoard;
import com.blxt.quickview.time.WatcherBoard;
import com.heneng.launcher.R;

/**
 * 表盘选择
 */
public class MainTimeViewHolder extends AbstractViewHolder {
    int timeBoardIndex = 1; // 表盘选择
    private WatcherBoard shiZhong;
    private NumberTimeBoard shiZhong2;

    public MainTimeViewHolder(@NonNull View view) {
        super(view);
        shiZhong = findViewById(R.id.shi_zhong);
        shiZhong2 = findViewById(R.id.shi_zhong2);

        shiZhong2.getViewHolder().getTvTime().setTextSize(64);
        shiZhong2.getViewHolder().getTvDate().setTextSize(32);
    }


    @Override
    public boolean upData(Object o) {

        switch (timeBoardIndex){
            case 0:
                shiZhong.setVisibility(View.VISIBLE);
                shiZhong2.setVisibility(View.GONE);
                break;
            case 1:
                shiZhong.setVisibility(View.GONE);
                shiZhong2.setVisibility(View.VISIBLE);
                break;
        }
        return false;
    }

    public int getTimeBoardIndex() {
        return timeBoardIndex;
    }

    public void setTimeBoardIndex(int timeBoardIndex) {
        this.timeBoardIndex = timeBoardIndex;
    }
}
