package com.xiao.gradle.plugin

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class IMoocReleaseInfoTask extends DefaultTask{
    IMoocReleaseInfoTask() {
        group 'imooc'
        description 'IMoocReleaseInfoTask'
    }

    @TaskAction
    void action(){
        IMoocReleaseInfo releaseInfo = project.extensions.IMoocReleaseInfo

        println releaseInfo
        println releaseInfo.innerExt
        //println project.extensions.inner

        File outFile = project.file(releaseInfo.filePath)
        def sw = new StringWriter();
        def xmlBuilder = new MarkupBuilder(sw)
        if (outFile.text != null && outFile.text.size() > 0) {
            //文件有内容
            //创建节点
            xmlBuilder.release {
                versionName(releaseInfo.versionName)
                versionCode(releaseInfo.versionCode)
                versionInfo(releaseInfo.versionInfo)
            }

            //获取已有数据
            List<String> lines = outFile.readLines()
            def length = lines.size() - 1

            outFile.withWriter { BufferedWriter writer ->
                //此处index从0开始
                lines.eachWithIndex { String entry, int index ->
                    if (length == index) {
                        writer.append(sw.toString() + '\r\n')
                    }

                    writer.append(entry + '\r\n')
                }
            }

        } else {
            //文件没有内容
            xmlBuilder.releases {
                release {
                    versionName(releaseInfo.versionName)
                    versionCode(releaseInfo.versionCode)
                    versionInfo(releaseInfo.versionInfo)
                }
            }

            outFile.withWriter { BufferedWriter writer ->
                writer.append(sw.toString())
            }
        }
    }
}