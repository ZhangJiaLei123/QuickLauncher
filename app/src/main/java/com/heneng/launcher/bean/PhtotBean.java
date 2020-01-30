package com.heneng.launcher.bean;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.heneng.launcher.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PhtotBean extends BaseBean{

    protected int id;

    public PhtotBean(int id, String title, String content, Bitmap imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public PhtotBean(int id, String title, String content, File imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imageUrl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setImage(BitmapFactory.decodeStream(fis));
    }

    public Bitmap getImage(){
        return (Bitmap)image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<PhtotBean> getPhotoModels() {
        List<PhtotBean> mediaModels = new ArrayList<>();

        File filePicDir = new File(MainActivity.SD_PATH + "/Pictures/");
        File[] pics = filePicDir.listFiles();

        for (int i = 0; i < pics.length; i++) {
            if(pics[i].isDirectory()){
                continue;
            }
            long time = pics[i].lastModified();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cal.setTimeInMillis(time);

            PhtotBean mediaModel = new PhtotBean(0, pics[i].getName()
                    , formatter.format(cal.getTime()), pics[i]);
            mediaModels.add(mediaModel);
        }

        return mediaModels;
    }

    public static Drawable getDrawable( int resId){
        Resources resources = MainActivity.mContext.getResources();
        Drawable btnDrawable = resources.getDrawable(resId);
        return btnDrawable;
    }

}