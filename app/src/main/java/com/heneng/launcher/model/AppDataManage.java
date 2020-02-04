
package com.heneng.launcher.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

import com.heneng.launcher.ui.activity.application.MyApplication;
import com.heneng.launcher.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppDataManage {

    private final Context mContext;

    protected static List<String> appPackageFilter = null; // 过滤器,隐藏不必要的app

    private static void initappPackageFilter(){
        appPackageFilter = new ArrayList<>();
        appPackageFilter.add("com.android.settings");
        appPackageFilter.add("com.heneng");
      //  appPackageFilter.add("com.heneng.launcher");
    }

    private static boolean appPackageFilter(String packageName){

        if(appPackageFilter == null){
            initappPackageFilter();
        }
        for(String p : appPackageFilter){
            if(packageName.startsWith(p) && !packageName.equals(MyApplication.getInstance().getPackageName())){
                return true;
            }
        }
        return false;
    }

    public AppDataManage(Context context) {
        mContext = context;
        initappPackageFilter();
    }

    public ArrayList<AppInfoBean> getLaunchAppList() {
        PackageManager localPackageManager = mContext.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> localList = localPackageManager.queryIntentActivities(localIntent, 0);
        ArrayList<AppInfoBean> localArrayList = null;
        Iterator<ResolveInfo> localIterator = null;
        localArrayList = new ArrayList<>();
        if (localList.size() != 0) {
            localIterator = localList.iterator();
        }
        while (true) {
            if (!localIterator.hasNext())
                break;
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            AppInfoBean localAppBean = new AppInfoBean();
            localAppBean.setIcon(localResolveInfo.activityInfo.loadIcon(localPackageManager));
            localAppBean.setTitle(localResolveInfo.activityInfo.loadLabel(localPackageManager).toString());
            localAppBean.setPackageName(localResolveInfo.activityInfo.packageName);
            localAppBean.setDataDir(localResolveInfo.activityInfo.applicationInfo.publicSourceDir);
            localAppBean.setLauncherName(localResolveInfo.activityInfo.name);
            String pkgName = localResolveInfo.activityInfo.packageName;
            PackageInfo mPackageInfo;
            try {
                mPackageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
                if ((mPackageInfo.applicationInfo.flags & mPackageInfo.applicationInfo.FLAG_SYSTEM) > 0) {// 系统预装
                    localAppBean.setSysApp(true);
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (appPackageFilter(localAppBean.getPackageName())) {
                localArrayList.add(localAppBean);
            }
        }
        return localArrayList;
    }

    public ArrayList<AppInfoBean> getUninstallAppList() {
        PackageManager localPackageManager = mContext.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> localList = localPackageManager.queryIntentActivities(localIntent, 0);
        ArrayList<AppInfoBean> localArrayList = null;
        Iterator<ResolveInfo> localIterator = null;
        if (localList != null) {
            localArrayList = new ArrayList<>();
            localIterator = localList.iterator();
        }
        while (true) {
            if (!localIterator.hasNext())
                break;
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            AppInfoBean localAppBean = new AppInfoBean();
            localAppBean.setIcon(localResolveInfo.activityInfo.loadIcon(localPackageManager));
            localAppBean.setTitle(localResolveInfo.activityInfo.loadLabel(localPackageManager).toString());
            localAppBean.setPackageName(localResolveInfo.activityInfo.packageName);
            localAppBean.setDataDir(localResolveInfo.activityInfo.applicationInfo.publicSourceDir);
            String pkgName = localResolveInfo.activityInfo.packageName;
            PackageInfo mPackageInfo;
            try {
                mPackageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
                if ((mPackageInfo.applicationInfo.flags & mPackageInfo.applicationInfo.FLAG_SYSTEM) > 0) {// 系统预装
                    localAppBean.setSysApp(true);
                } else {
                    if (appPackageFilter(localAppBean.getPackageName())) {
                        localArrayList.add(localAppBean);
                    }
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return localArrayList;
    }

    public ArrayList<AppInfoBean> getAutoRunAppList() {
        PackageManager localPackageManager = mContext.getPackageManager();
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> localList = localPackageManager.queryIntentActivities(localIntent, 0);
        ArrayList<AppInfoBean> localArrayList = null;
        Iterator<ResolveInfo> localIterator = null;
        if (localList != null) {
            localArrayList = new ArrayList<>();
            localIterator = localList.iterator();
        }

        while (true) {
            if (!localIterator.hasNext())
                break;
            ResolveInfo localResolveInfo = localIterator.next();
            AppInfoBean localAppBean = new AppInfoBean();
            localAppBean.setIcon(localResolveInfo.activityInfo.loadIcon(localPackageManager));
            localAppBean.setTitle(localResolveInfo.activityInfo.loadLabel(localPackageManager).toString());
            localAppBean.setPackageName(localResolveInfo.activityInfo.packageName);
            localAppBean.setDataDir(localResolveInfo.activityInfo.applicationInfo.publicSourceDir);
            String pkgName = localResolveInfo.activityInfo.packageName;
            String permission = "android.permission.RECEIVE_BOOT_COMPLETED";
            try {
                PackageInfo mPackageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
                if ((PackageManager.PERMISSION_GRANTED == localPackageManager.checkPermission(permission, pkgName))
                        && !((mPackageInfo.applicationInfo.flags & mPackageInfo.applicationInfo.FLAG_SYSTEM) > 0)) {
                    localArrayList.add(localAppBean);
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return localArrayList;
    }
}
