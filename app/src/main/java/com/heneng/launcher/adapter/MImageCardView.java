package com.heneng.launcher.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.leanback.widget.ImageCardView;

public class MImageCardView extends ImageCardView {
    public MImageCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MImageCardView(Context context) {
        super(context);
        init();
    }

    public MImageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        setLayoutParams(new LinearLayout.LayoutParams(100,100));
    }
}
