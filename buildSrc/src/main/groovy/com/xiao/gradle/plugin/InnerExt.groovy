package com.xiao.gradle.plugin

import org.gradle.api.Project

class InnerExt{
    String innerName
    String msg

    InnerExt(Project project) {

    }

    void innerName(String name) {
        innerName = name
    }

    void msg(String msg) {
        this.msg = msg
    }

    String toString() {
        return "InnerExt[ name = ${innerName}, msg = ${msg}]"
    }
}