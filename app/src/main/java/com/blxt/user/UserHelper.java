package com.blxt.user;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.blxt.user.bean.UserBase;

public class UserHelper {
    /** 指纹组件接收广播名 */
    public static final String MSG_USER_BROADCASTRECEIVER = "com.blxt.user.broadcast";
    /** 指纹组件接收广播名 */
    public static final String MSG_USER_BROADCASTRECEIVER_PROVIDER = "com.blxt.user.broadcast.provider";


    public static final String MSG_USER_KEY_APPLICANT = "user.applicant"; // 用户接受返回的意图名称
    public static final String MSG_USER_KEY_INTENT= "user.intent"; // 需要打开的意图
    public static final String MSG_USER_KEY_USER_INFO = "user.info"; // 请求当前用户
    public static final String MSG_USER_KEY_USER_ACTION = "user.action"; // 当前用户动作

    public static final int MODEL_REGISTER = 1;  // 注册
    public static final int MODEL_LOGIN = 2;  // 登陆
    public static final int MODEL_MANAGE = 3;  //管理
    public static final int MODEL_CHANGE_USER = 4;  // 修改用户

    static Context context = null;

    public static UserHelper newInstance(@NonNull Context context){
        if(context == null){
            throw new NullPointerException("没有初始化异常,Context不能为空");
        }
        if(UserHelper.context == null) {
            UserHelper.context = context;
        }

        return new UserHelper();
    }

    static UserBase userNow = null; // 当前用户

    private UserHelper(){

    }


    /**
     *  打开用户操作界面
     * @param intentReturn 返回的广播
     * @param activityId   需要打开的广播ID
     */
    public synchronized static void openUserActivity(String intentReturn, int activityId){
        Intent intent=new Intent(MSG_USER_BROADCASTRECEIVER_PROVIDER);
        intent.putExtra(MSG_USER_KEY_INTENT, activityId);
        intent.putExtra(MSG_USER_KEY_APPLICANT, intentReturn);
        sendData(intent);
    }

    /**
     * 请求当前系统用户信息
     * @param intentReturn  返回的广播接收器
     */
    public synchronized static void getUserInfo(String intentReturn){
        Intent intent=new Intent(MSG_USER_BROADCASTRECEIVER_PROVIDER);
        intent.putExtra(MSG_USER_KEY_USER_INFO, intentReturn);
        sendData(intent);
    }

    /**
     * 向指定广播 发送用户信息
     * @param user
     */
    public synchronized void notiUserInfo(String Intentname, String user, int userAction){
        Intent intent=new Intent(Intentname);
        intent.putExtra(MSG_USER_KEY_USER_INFO, user);
        intent.putExtra(MSG_USER_KEY_USER_ACTION, userAction);
        sendData(intent);
    }


    /**
     * 公布当前用户登陆信息
     */
    public void notiUserInfo(){
        notiUserInfo(MSG_USER_BROADCASTRECEIVER, getUserStr(), 1);
    }

    /**
     * 公布当前用户退出信息
     */
    public void notiUserLogout(){
        notiUserInfo(MSG_USER_BROADCASTRECEIVER, "#退出登陆#", 2);
    }



    public UserBase getUserNow() {
        return userNow;
    }

    public String getUserStr() {
        if(userNow == null){
            return null;
        }
        return userNow.toString();
    }

    public void setUserNow(UserBase userNow) {
        this.userNow = userNow;
    }

    /**
     * 发送数据
     * @param intent
     */
    private synchronized static void sendData(Intent intent){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        context.sendBroadcast(intent);
    }

}
