package com.xiao.gradle.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.extensions.create('IMoocReleaseInfo', IMoocReleaseInfo.class)
        //project.extensions.create("inner", InnerExt.class, project)
        project.tasks.create('IMoocReleaseInfoTask', IMoocReleaseInfoTask.class)

        AppExtension android = project.extensions.getByType(AppExtension.class)
        android.registerTransform(new MyTransform(project))
    }
}