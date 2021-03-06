package com.heneng.launcher.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import android.view.ContextThemeWrapper;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.heneng.launcher.R;
import com.heneng.launcher.model.MediaModel;

/**
 * ImageCard Presenter
 *
 * @author jacky
 * @version v1.0
 * @since 16/7/16
 */
public class ImgCardPresenter extends Presenter {

    private Context mContext;
    private int CARD_WIDTH = 313;
    private int CARD_HEIGHT = 176;
    private Drawable mDefaultCardImage = null;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.pic_default);
        ImageCardView cardView = new ImageCardView(new ContextThemeWrapper(parent.getContext(),
                R.style.CustomImageCardTheme)) {
            @Override
            public void setSelected(boolean selected) {
                int selected_background = mContext.getResources().getColor(R.color.detail_background);
                int default_background = mContext.getResources().getColor(R.color.default_background);
                int color = selected ? selected_background : default_background;
                findViewById(R.id.info_field).setBackgroundColor(color);
                super.setSelected(selected);
            }
        };
        cardView.setBadgeImage(getDrawable(mContext, R.mipmap.ic_launcher));
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        if (item instanceof MediaModel) {
            MediaModel mediaModel = (MediaModel) item;
            cardView.setTitleText(mediaModel.getTitle());
            cardView.setContentText(mediaModel.getContent());
            Glide.with(cardView.getMainImageView().getContext())
                    .load(mediaModel.getImageUrl())
                    .crossFade()
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    public static Drawable getDrawable(Context context, int resId){
        Resources resources = context.getResources();
        Drawable btnDrawable = resources.getDrawable(resId);
        return btnDrawable;
    }

}
