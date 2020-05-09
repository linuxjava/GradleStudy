package com.xiao.gradle.plugin

class IMoocReleaseInfo{
    public String versionName
    public String versionCode
    public String versionInfo
    public String filePath

    public IMoocReleaseInfo(){

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