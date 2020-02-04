package com.blxt.safety.bean;

import android.graphics.drawable.Drawable;



public class AppProcessInfo implements Comparable<AppProcessInfo> {

    /**
     * The app name.
     */
    public String appName;

    /**
     * The name of the process that this object is associated with.
     */
    public String processName;

    /**
     * The pid of this process; 0 if none.
     */
    public int pid;

    /**
     * The user id of this process.
     */
    public int uid;

    /**
     * The icon.
     */
    public Drawable icon;

    /**
     * 鍗犵敤鐨勫唴瀛�.
     */
    public long memory;

    /**
     * 鍗犵敤鐨勫唴瀛�.
     */
    public String cpu;

    /**
     * 杩涚▼鐨勭姸鎬侊紝鍏朵腑S琛ㄧず浼戠湢锛孯琛ㄧず姝ｅ湪杩愯锛孼琛ㄧず鍍垫鐘舵�侊紝N琛ㄧず璇ヨ繘绋嬩紭鍏堝�兼槸璐熸暟.
     */
    public String status;

    /**
     * 褰撳墠浣跨敤鐨勭嚎绋嬫暟.
     */
    public String threadsCount;


    public boolean checked=true;

    /**
     * 鏄惁鏄郴缁熻繘绋�.
     */
    public boolean isSystem;

    /**
     * Instantiates a new ab process info.
     */
    public AppProcessInfo() {
        super();
    }

    /**
     * Instantiates a new ab process info.
     *
     * @param processName the process name
     * @param pid         the pid
     * @param uid         the uid
     */
    public AppProcessInfo(String processName, int pid, int uid) {
        super();
        this.processName = processName;
        this.pid = pid;
        this.uid = uid;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(AppProcessInfo another) {
        if (this.processName.compareTo(another.processName) == 0) {
            if (this.memory < another.memory) {
                return 1;
            } else if (this.memory == another.memory) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return this.processName.compareTo(another.processName);
        }
    }

}
