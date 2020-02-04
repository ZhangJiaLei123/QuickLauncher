package com.heneng.launcher.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.blxt.quicklog.QLog;
import com.heneng.launcher.R;
import com.heneng.quickfile.QFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.heneng.launcher.model.MConstant.APP_NAME_BACKGROUND;

public class ImageTools {
    //File APP_FILE_BACKGROUND = new File();

    public static BitmapDrawable getBackgroundImage(Context context){
        File imageFile = new File(QLog.PATH.getAppFilesPath(context) + "/" + APP_NAME_BACKGROUND);

        if(!imageFile.exists())
        {
            QLog.e(ImageTools.class, "背景图片不存在", imageFile.getPath());
            return null;
        }

        BitmapDrawable drawable = null;
        try {
            drawable = ImageTools.getImageDrawable(imageFile);
        } catch (IOException e) {
            QLog.e(ImageTools.class, "更新背景出错,文件读取异常", imageFile.getPath());
            e.printStackTrace();
        }

        return drawable;
    }

    /**
     * 初始化背景图片
     * @param context
     */
    public static void initToBackgroundImageFile(Context context){
        BitmapDrawable  drawable = (BitmapDrawable )context.getResources().getDrawable(R.drawable.appbackground);
        File imageFile = new File(QLog.PATH.getAppFilesPath(context) + "/" + APP_NAME_BACKGROUND);

        if (imageFile.exists())
            return;

        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

         Bitmap img = drawable.getBitmap();

        try{
            OutputStream os = new FileOutputStream(imageFile);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        }catch(Exception e){
            QLog.e(ImageTools.class,  "复制图片出错");
        }

    }

    /**
     * 设置新的背景图片,将新图片的文件复制替换掉原来的背景文件
     * @param context
     * @param newImageFile  新背景图片的位置
     */
    public static void copyToBackgroundImageFile(Context context, File newImageFile){

        File imageFile = new File(QLog.PATH.getAppFilesPath(context) + "/" + APP_NAME_BACKGROUND);

        if (imageFile.exists())
            imageFile.delete();

        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        QFile.MFile.copyFile(newImageFile, imageFile);

    }


    /**
     * 将文件生成位图
     * @param file
     * @return
     * @throws IOException
     */
    public static BitmapDrawable getImageDrawable(File file)
            throws IOException
    {
        //打开文件
        if(!file.exists())
        {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[1024];

        //得到文件的输入流
        InputStream in = new FileInputStream(file);

        //将文件读出到输出流中
        int readLength = in.read(bt);
        while (readLength != -1) {
            outStream.write(bt, 0, readLength);
            readLength = in.read(bt);
        }

        //转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
        BitmapDrawable bd = new BitmapDrawable(bitmap);

        return bd;
    }
}
