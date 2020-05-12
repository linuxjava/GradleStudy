package com.xiao.gradle.plugin

import org.gradle.api.Action

class IMoocReleaseInfo{
    public String versionName
    public String versionCode
    public String versionInfo
    public String filePath
    InnerExt innerExt = new InnerExt();

    public IMoocReleaseInfo(){

    }

    //创建内部Extension，名称为方法名 inner
    void inner(Action<InnerExt> action) {
        action.execute(inner)
    }

    //创建内部Extension，名称为方法名 inner
    void inner(Closure c) {
        org.gradle.util.ConfigureUtil.configure(c, innerExt)
    }


    @Override
    public String toString() {
        return "IMoocReleaseInfo{" +
                "versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionInfo='" + versionInfo + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}