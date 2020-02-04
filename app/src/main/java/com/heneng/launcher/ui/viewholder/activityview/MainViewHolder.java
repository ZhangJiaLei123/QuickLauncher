package com.heneng.launcher.ui.viewholder.activityview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.blxt.quickactivity.AbstractViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.material.navigation.NavigationView;
import com.heneng.launcher.R;
import com.heneng.launcher.bean.AppInfoBean;
import com.heneng.launcher.bean.PhtotBean;
import com.heneng.launcher.model.AppDataManage;
import com.heneng.launcher.model.FunctionModel;
import com.heneng.launcher.model.MConstant;
import com.heneng.launcher.model.MediaModel;
import com.heneng.launcher.presenter.AppCardPresenter;
import com.heneng.launcher.presenter.FunctionCardPresenter;
import com.heneng.launcher.presenter.ImgCardPresenter;
import com.heneng.launcher.presenter.PhotoPresenter;
import com.blxt.safety.activity.RublishcleanActivity;
import com.heneng.launcher.util.ImageTools;
import com.heneng.quicknoti.TipToast;

import java.util.ArrayList;
import java.util.List;

import static com.heneng.launcher.model.MConstant.MSGID_APP_STARTACTIVITY;

public class MainViewHolder extends AbstractViewHolder {

    private Activity activity = null;
    TipToast tipToast = TipToast.getInstance();


    protected BrowseFragment mBrowseFragment;
    private ArrayObjectAdapter rowsAdapter;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;

    private NavigationView naviView;
    private DrawerLayout mDrawer = null;

    public MainViewHolder(@NonNull View view, Activity activity ) {
        super(view);
        this.activity = activity;

        mBrowseFragment = (BrowseFragment) activity.getFragmentManager().findFragmentById(R.id.browse_fragment);
        mBrowseFragment.setHeadersState(BrowseFragment.HEADERS_DISABLED);
        //  mBrowseFragment.setTitle(getString(R.string.app_name));
        naviView = view.findViewById(R.id.nav_view);
        mDrawer = view.findViewById(R.id.drawer_layout);


        upBackground();

        prepareBackgroundManager();
        buildRowsAdapter();

        addListener();
    }

    @Override
    public boolean upData(Object o) {
        if(o instanceof Message){
            Message message = (Message)o;
            switch (message.what){
                case MConstant.MSGID_APP_UP_BACKGROUND: // 更新背景
                    upBackground();
                    break;
            }
        }

        return false;
    }

    public void addListener(){
        //菜单的点击事件
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int index = 0;
                //进行判断
                switch (menuItem.getItemId()) {
                    case R.id.nav_clear:// 清理垃圾
                        //@TODO 清理垃圾
                       // tipToast.showToast("提示", "清理垃圾完成");
                        Message message = new Message();
                        message.what = MSGID_APP_STARTACTIVITY;
                        message.obj = RublishcleanActivity.class;
                        callbck.sendMessage(message);

                        break;
                    case R.id.nav_background:// 设置壁纸
                        //@TODO 设置壁纸
                        tipToast.showToast("提示", "设置壁纸");
                        break;
                    case R.id.nav_up_app:// 检查更新
                        //@TODO 检查更新
                        tipToast.showToast("提示", "无版本更新");
                        break;

                    default:
                        break;
                }


                mDrawer.closeDrawers();
                return false;
            }
        });
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity);
        mBackgroundManager.attach(activity.getWindow());
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void buildRowsAdapter() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        //  addPhotoRow();
        //  addVideoRow();
        addAppRow();
        addOtherRow(); // 其他活动

        mBrowseFragment.setAdapter(rowsAdapter);
        mBrowseFragment.setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof MediaModel) {
//                    MediaModel mediaModel = (MediaModel) item;
//                    Intent intent = new Intent(getContext(), MediaDetailsActivity.class);
//                    intent.putExtra(MediaDetailsActivity.MEDIA, mediaModel);
//
//                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            (Activity) getContext(),
//                            ((ImageCardView) itemViewHolder.view).getMainImageView(),
//                            MediaDetailsActivity.SHARED_ELEMENT_NAME).toBundle();
//                    getContext().startActivity(intent, bundle);
                } else if (item instanceof AppInfoBean) {
                    AppInfoBean appBean = (AppInfoBean) item;
                    Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(
                            appBean.getPackageName());
                    if (launchIntent != null) {
                        getContext().startActivity(launchIntent);
                    }
                } else if (item instanceof FunctionModel) {
                    FunctionModel functionModel = (FunctionModel) item;
                    Intent intent = functionModel.getIntent();
                    if (intent != null) {
                        getContext().startActivity(intent);
                    }
                }
            }
        });
        mBrowseFragment.setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof MediaModel) {

                    MediaModel mediaModel = (MediaModel) item;
                    int width = mMetrics.widthPixels;
                    int height = mMetrics.heightPixels;

                    Glide.with(getContext())
                            .load(mediaModel.getImageUrl())
                            .asBitmap()
                            .centerCrop()
                            .into(new SimpleTarget<Bitmap>(width, height) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    mBackgroundManager.setBitmap(resource);
                                }
                            });
                } else {
                    mBackgroundManager.setBitmap(null);
                }
            }
        });

        mBrowseFragment.setSelectedPosition(0);
    }

    private void addPhotoRow() {
        String headerName = getContext().getResources().getString(R.string.app_header_photo_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new PhotoPresenter());

        for (PhtotBean mediaModel : PhtotBean.getPhotoModels()) {
            listRowAdapter.add(mediaModel);
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addVideoRow() {
        String headerName = getContext().getResources().getString(R.string.app_header_video_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ImgCardPresenter());
        for (MediaModel mediaModel : MediaModel.getVideoModels()) {
            listRowAdapter.add(mediaModel);
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addAppRow() {
        String headerName = getContext().getResources().getString(R.string.app_header_app_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new AppCardPresenter());

        ArrayList<AppInfoBean> appDataList = new AppDataManage(getContext()).getLaunchAppList();
        int cardCount = appDataList.size();

        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add(appDataList.get(i));
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addOtherRow() {
        String headerName = getContext().getResources().getString(R.string.app_header_function_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new FunctionCardPresenter());
        List<FunctionModel> functionModels = FunctionModel.getFunctionList(getContext());
        int cardCount = functionModels.size();
        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add(functionModels.get(i));
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    /**
     * 更新背景图片
     */
    private void upBackground(){
        BitmapDrawable drawable = ImageTools.getBackgroundImage(getContext());
        if(drawable != null){
            mDrawer.setBackground(drawable);
        }
        else{
            tipToast.showToast("提示", "背景图片不存在");
        }
    }
}
