package com.blxt.finger;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class FingerHelp {
    ////////////////////////////////// 低功耗模块
    /** 广播名 */

    /////////////////////////////////// 指纹模块 - 接收
    /** 指纹组件接收广播名 */
    public static final String MSG_FINGER_BROADCASTRECEIVER = "com.blxt.finger.broadcast";
    /** 指纹组件接收广播名 */
    public static final String MSG_FINGER_BROADCASTRECEIVER_REPLY = "com.blxt.finger.broadcast.reply";
    public static final String MSG_FINGER_KEY_DO = "finger.data";

    /** 意图Key */
    public static final String MSG_FINGER_INTENT_KEY_CTRID = "FINGER.CTRID";
    public static final String MSG_FINGER_INTENT_KEY_USERID = "FINGER.USERID";

    public final static int MSG_FINGER_OPEN = 1; // 打开设备
    public final static int MSG_FINGER_CLKOSE = 2; // 关闭设备
    public final static int MSG_FINGER_ENROLL = 3; // 录入指纹
    public final static int MSG_FINGER_VERIFY = 4; // 验证指纹
    public final static int MSG_FINGER_IDENTIFY = 5; // 开始识别指纹
    public final static int MSG_FINGER_CAPTUREIMAGE = 6; // 捕捉指纹图像
    public final static int MSG_FINGER_GETUSERCOUNT = 7; // 获取指纹注册数
    public final static int MSG_FINGER_GETEMPTYID = 8; // 获取一个空ID
    public final static int MSG_FINGER_DELETEID = 9; // 删除一个指纹
    public final static int MSG_FINGER_DELETEALL = 10; // 删除所有指纹ID
    public final static int MSG_FINGER_CANCEL = 20; // 取消操作
    public final static int MSG_FINGER_INIT = 21;

    /////////////////////////////////// 指纹模块 - 反馈
    public static final String MSG_FINGER_OUTPUT_BROADCASTRECEIVER = "com.blxt.hn.finger.output";

    public static final String MSG_FINGER_INTENT_KEY_CTR_ID = "FINGER.CTRID";   // 操作反馈码

    public static final String MSG_FINGER_INTENT_KEY_USER_ID = "FINGER.UserID"; // 识别到的指纹ID

    public static final String MSG_FINGER_INTENT_KEY_ERROR = "FINGER.ERROR"; // 错误信息

    public static final String MSG_FINGER_INTENT_KEY_MSG = "FINGER.MSG"; // 携带消息


    public static final int MSG_FINGER_VALUE_OPEN_SUCCEE         = 0x0101; // 打开设备成功
    public static final int MSG_FINGER_VALUE_OPEN_FAILED         = 0x0102; // 打开设备失败
    public static final int MSG_FINGER_VALUE_CLKOSE_SUCCEE       = 0x0201; // 关闭设备成功
    public static final int MSG_FINGER_VALUE_CLKOSE_FAILED       = 0x0202; // 关闭设备失败

    public static final int MSG_FINGER_VALUE_ENROLL_SUCCEE       = 0x0301; // 录入成功
    public static final int MSG_FINGER_VALUE_ENROLL_FAILED       = 0x0302; // 录入失败
    public static final int MSG_FINGER_VALUE_ENROLL              = 0x0303; // 录入中
    public static final int MSG_FINGER_VALUE_VERIFY_SUCCEE       = 0x0401; // 验证成功
    public static final int MSG_FINGER_VALUE_VERIFY_FAILED       = 0x0402; // 验证失败
    public static final int MSG_FINGER_VALUE_VERIFY              = 0x0403; // 验证中

    public static final int MSG_FINGER_VALUE_IDENTIFY_SUCCEE     = 0x0501; // 开始识别指纹-捕捉到指纹成功
    public static final int MSG_FINGER_VALUE_IDENTIFY_FAILED     = 0x0502; // 开始识别指纹-捕捉到指纹失败
    public static final int MSG_FINGER_VALUE_IDENTIFY_STOP       = 0x0503; // 停止识别指纹
    public static final int MSG_FINGER_VALUE_IDENTIFY            = 0x0504; // 识别指纹中

    public static final int MSG_FINGER_VALUE_CAPTUREIMAGE_SUCCEE = 0x0601; // 捕捉指纹图像成功
    public static final int MSG_FINGER_VALUE_CAPTUREIMAGE_FAILED = 0x0602; // 捕捉指纹图像失败
    public static final int MSG_FINGER_VALUE_GETUSERCOUNT_SUCCEE = 0x0701; // 获取指纹注册数成功
    public static final int MSG_FINGER_VALUE_GETUSERCOUNT_FAILED = 0x0702; // 获取指纹注册数失败

    public static final int MSG_FINGER_VALUE_GETEMPTYID_SUCCEE   = 0x0801; // 获取一个空ID成功
    public static final int MSG_FINGER_VALUE_GETEMPTYID_FAILED   = 0x0802; // 获取一个空ID失败
    public static final int MSG_FINGER_VALUE_GETEMPTYID_FULL     = 0x0803; // 指纹注册容量已达到上限

    public static final int MSG_FINGER_VALUE_DELETEID_SUCCEE     = 0x0901; // 删除一个ID成功
    public static final int MSG_FINGER_VALUE_DELETEID_FAILED     = 0x0902; // 删除一个ID失败
    public static final int MSG_FINGER_VALUE_DELETEALL_SUCCEE    = 0x1001; // 删除全部ID成功
    public static final int MSG_FINGER_VALUE_DELETEALL_FAILED    = 0x1002; // 删除全部ID失败

    public static final int MSG_FINGER_VALUE_NOTI                = 0x2000; // 通知
    public static final int MSG_FINGER_VALUE_CANCEL_SUCCEE       = 0x2001; // 取消操作成功
    public static final int MSG_FINGER_VALUE_CANCEL_FAILED       = 0x2002; // 取消操作失败


    // 错误码

    public static final int ERROR_FINGER_SUCCEE                  = 0x3000; // 无错误
    public static final int ERROR_FINGER_NO_USB                  = 0x3001; // USB连接失败
    public static final int ERROR_FINGER_NO_OPEN                 = 0x3002; // 设备未打开

    public static final int ERROR_FINGER_UN_STATUS               = 0x3003; // 未知状态错误
    public static final int ERROR_FINGER_TEMPLATE_NOT_EMPTY      = 0x3004; // ID模板不为空（被使用）
    public static final int ERROR_FINGER_CONNECT                 = 0x3005; // 连接错误
    public static final int ERROR_FINGER_UPIMAGE                 = 0x3006; // 上传指纹图像到模块错误
    public static final int ERROR_FINGER_IMAGE_ROPY              = 0x3007; // 指纹图像质量差
    public static final int ERROR_FINGER_MERGE_TEMPLATE          = 0x3008; // 合并指纹图像失败
    public static final int ERROR_FINGER_DUPLICATION_ID          = 0x3009; // 重复ID
    public static final int ERROR_FINGER_ILL_ID                  = 0x3010; // 错误ID
    public static final int ERROR_FINGER_SAVE_ID                 = 0x3011; // 储存指纹
    public static final int ERROR_FINGER_CREATE_TEMPLATE         = 0x3012; // 创建模板
    public static final int ERROR_FINGER_EMPTY_TEMPLATE          = 0x3013; // 空模板

    static Context context = null;

    public static FingerHelp newInstance(@NonNull Context context){
        if(context == null){
            throw new NullPointerException("没有初始化异常,Context不能为空");
        }
        if(FingerHelp.context == null) {
            FingerHelp.context = context;
        }

        return new FingerHelp();
    }

    private FingerHelp(){

    }


    /**
     * 发送数据
     * @param data
     */
    public synchronized static void sendData(byte data[]){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        Intent intent=new Intent(MSG_FINGER_BROADCASTRECEIVER);
        intent.putExtra(MSG_FINGER_KEY_DO, data);
        context.sendBroadcast(intent);
    }

    /**
     * 回复数据
     * @param data
     */
    public synchronized static void replyData(byte data[]){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        Intent intent=new Intent(MSG_FINGER_BROADCASTRECEIVER_REPLY);
        intent.putExtra(MSG_FINGER_KEY_DO, data);
        context.sendBroadcast(intent);
    }

}
