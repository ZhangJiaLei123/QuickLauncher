package com.heneng.launcher.ui.activity;

import android.os.Bundle;

import com.heneng.launcher.R;
import com.heneng.launcher.ui.activity.application.BaseActivity;

/**
 * @author jacky
 * @version v1.0
 * @since 16/8/28
 */
public class MediaDetailsActivity extends BaseActivity {

    public static final String MEDIA = "Media";
    public static final String SHARED_ELEMENT_NAME = "hero";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

}