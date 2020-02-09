package com.blxt.user.bean;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 访客定义.
 * User: Zhang
 * Date: 2019/6/2
 * Time: 22:18 - 六月
 */
public class UserBase {

    HashMap<String, String> keyValue = new HashMap<String, String>();


    // 保存到数据库的
    /** 数据库主键, 自增*/

    Long _id;

    /** 登陆id */
    protected String uid = "";
    /** 用户名 */
    protected String name = "";
    /** 密码 */
    protected String pwd = "";
    /** 用户数据*/
    protected String dataStr;// 启用标记 权限 指纹标记 操作
    /** 联系方式*/
    protected String phone = "";

    /** 登陆时间*/
    protected Long time;
    /** 退出时间*/
    protected Long timeOut;
    /** 创建时间*/
    protected Long timeMake;
    private String pwdHint = ""; ///< 密码提示

    public UserBase() {
    }


    public UserBase(Long _id, String uid, String name, String pwd, String dataStr, String phone,
                    Long time, Long timeOut, Long timeMake, String pwdHint) {
        this._id = _id;
        this.uid = uid;
        this.name = name;
        this.pwd = pwd;
        this.dataStr = dataStr;
        this.phone = phone;
        this.time = time;
        this.timeOut = timeOut;
        this.timeMake = timeMake;
        this.pwdHint = pwdHint;

        keyValue = anakeyValue(dataStr);
    }



    /**
     * 解析键值对
     *
     * @param content
     * @return
     */
    public static HashMap<String, String> anakeyValue(String content) {
        HashMap<String, String> map = new HashMap<String, String>();

        if ((content == null) || (content.length() <= 1)) {
            return map;
        }

        int index = -1;
        while ((content != null) && (content.length() > 0) && ((index = content.indexOf("=")) > 0)) {
            String key = content.substring(0, index).trim();
            int indexL = content.indexOf("=", ++index);
            if (indexL < 0) {
                indexL = content.length();
            } else {
                indexL = content.lastIndexOf("\"", indexL);
                if ((indexL < 0) || (content.lastIndexOf("\"", indexL - 1) < 0)) {
                    indexL = content.length();
                } else {
                    indexL++;
                }
            }
            String vakue = content.substring(index, indexL).trim();

            vakue = vakue.endsWith("\"") ? vakue.substring(0, vakue.length() - 1) : vakue;
            vakue = vakue.startsWith("\"") ? vakue.substring(1, vakue.length()) : vakue;
            map.put(key, vakue);
            content = content.substring(indexL, content.length());
        }
        return map;
    }



    public boolean equals(UserBase obj) {
        if(obj.getUid().equals(obj.getUid())){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserBase{" +
                "_id=" + _id +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", time=" + time +
                ", phone='" + phone + '\'' +
                ", dataStr:{" + getDataStr() + '}' +
                '}';
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTimeOut() {
        return this.timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public Long getTimeMake() {
        return this.timeMake;
    }

    public void setTimeMake(Long timeMake) {
        this.timeMake = timeMake;
    }

    public String getDataStr() {
        dataStr = "";
        Set<Map.Entry<String, String>> set = keyValue.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            dataStr += entry.getKey() + "=\"" + entry.getValue() + "\" ";
        }
        dataStr = dataStr.trim();
        return this.dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public void setAbandon(boolean fal){
        keyValue.put("Abandon", fal + "");
    }

    public boolean isAbandon(){
        String res = keyValue.get("Abandon");
        if(res == null){
            return false;
        }
        return res.equals("true");
    }

    public String getPwdHint() {
        return this.pwdHint;
    }

    public void setPwdHint(String pwdHint) {
        this.pwdHint = pwdHint;
    }

    /**
     * 是否有管理员权限
     * @return
     */
    public boolean isAdmin(){
       return getGrade() >= GRADE.TOP_SECRET.grade ? true : false;
    }

    /**
     * 获取权限级别
     */
    public int getGrade(){
        String res = keyValue.get("Grade");
        if(res == null){
            return 0;
        }
        try {
            return Integer.parseInt(res);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 设置权限级别
     * @param grade
     */
    public void setGrade(int grade){
        keyValue.put("Grade", grade + "");
    }

    /**
     * 获取指纹ID
     */
    public int getFinger(){
        String res = keyValue.get("Finger");
        if(res == null){
            return -1;
        }
        try {
            return Integer.parseInt(res);
        }catch (Exception e){
            return -1;
        }
    }

    /**
     * 设置指纹ID
     * @param finger
     */
    public void setFinger(int finger){
        keyValue.put("Finger", finger + "");
    }

}
