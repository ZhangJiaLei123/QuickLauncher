package com.heneng.launcher.presenter;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.heneng.launcher.model.MediaModel;

/**
 * @author jacky
 * @version v1.0
 * @since 16/8/28
 */
public class MediaDetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object itemData) {

        if(itemData instanceof MediaModel){
            MediaModel mediaModel = (MediaModel) itemData;
            viewHolder.getSubtitle().setText(mediaModel.getTitle());
            viewHolder.getBody().setText(mediaModel.getContent());
        }
    }
}