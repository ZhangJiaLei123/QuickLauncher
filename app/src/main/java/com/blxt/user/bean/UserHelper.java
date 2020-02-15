package com.blxt.user.bean;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class UserHelper {
    /** 指纹组件接收广播名 */
    public static final String MSG_USER_BROADCASTRECEIVER = "com.blxt.user.broadcast";
    /** 指纹组件接收广播名 */
    public static final String MSG_USER_BROADCASTRECEIVER_PROVIDER = "com.blxt.user.broadcast.provider";

    // 设置URI
    Uri uri_user = Uri.parse("content://com.blxt.usermanager/user");

    public static final String MSG_USER_KEY_APPLICANT = "user.applicant"; // 用户接受返回的意图名称
    public static final String MSG_USER_KEY_INTENT= "user.intent";        // 需要打开的意图
    public static final String MSG_USER_KEY_USER_INFO = "user.info";      // 请求当前用户
    public static final String MSG_USER_KEY_USER_ACTION = "user.action";  // 当前用户动作

    public static final int MODEL_REGISTER = 1;  // 注册
    public static final int MODEL_LOGIN = 2;  // 登陆
    public static final int MODEL_MANAGE = 3;  //管理
    public static final int MODEL_CHANGE_USER = 4;  // 用户信息&修改

    public static final int MODEL_LOGIN_IN = 21;  // 开始登陆
    public static final int MODEL_LOGIN_ED = 22;  // 结束登陆

    static Context context = null;
    public static HashMap<String, String> keyValue = new HashMap<String, String>();

    public static UserHelper newInstance(@NonNull Context context){
        if(context == null){
            throw new NullPointerException("没有初始化异常,Context不能为空");
        }
        if(UserHelper.context == null) {
            UserHelper.context = context;
        }

        return new UserHelper();
    }

    private UserHelper(){

    }


    /**
     * 获取登陆的用户信息
     */
    public synchronized String getSystemUser(){
        if(context == null){
            throw new NullPointerException("没有初始化异常,Context不能为空");
        }
        ContentResolver resolver =  context.getContentResolver();
        return resolver.getType(uri_user);
    }

    /**
     * 从用户信息的字符串中解析到用户信息
     * @param content
     * @param key
     * @return
     */
    public String findDataByUser(String content, String key){
        key +="=\"";
        int indexH = content.indexOf(key);
        if(indexH < 0){
            return null;
        }
        indexH += key.length() ;
        int indexL = content.indexOf("\"", indexH);
        return content.substring(indexH, indexL);
    }
}
