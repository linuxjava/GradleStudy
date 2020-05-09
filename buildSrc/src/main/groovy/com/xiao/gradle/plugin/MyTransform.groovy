package com.xiao.gradle.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement

class MyTransform extends Transform {
    private Project project

    MyTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        // 返回 transform 的名称，最终的名称会是 transformClassesWithMyTransformForDebug 这种形式
        return "MyTransform";
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        /**
         返回需要处理的数据类型 有 下面几种类型可选
         public static final Set<ContentType> CONTENT_CLASS = ImmutableSet.of(CLASSES);
         public static final Set<ContentType> CONTENT_JARS = ImmutableSet.of(CLASSES, RESOURCES);
         public static final Set<ContentType> CONTENT_RESOURCES = ImmutableSet.of(RESOURCES);
         public static final Set<ContentType> CONTENT_NATIVE_LIBS = ImmutableSet.of(NATIVE_LIBS);
         public static final Set<ContentType> CONTENT_DEX = ImmutableSet.of(ExtendedContentType.DEX);
         public static final Set<ContentType> DATA_BINDING_ARTIFACT = ImmutableSet.of(ExtendedContentType.DATA_BINDING);
         */
        return TransformManager.CONTENT_CLASS;
    }

@Override
Set<? super QualifiedContent.Scope> getScopes() {
    /**
     返回需要处理内容的范围，有下面几种类型
     PROJECT(1), 只处理项目的内容
     SUB_PROJECTS(4), 只处理子项目
     EXTERNAL_LIBRARIES(16), 只处理外部库
     TESTED_CODE(32), 只处理当前 variant 对应的测试代码
     PROVIDED_ONLY(64), 处理依赖
     @Deprecated
      PROJECT_LOCAL_DEPS(2) ,
     @Deprecated
      SUB_PROJECTS_LOCAL_DEPS(8) ;
     */
    return TransformManager.SCOPE_FULL_PROJECT;
}

    @Override
    boolean isIncremental() {
        // 是否增量，如果返回 true，TransformInput 会包括一份修改的文件列表，返回 false，会进行全量编译，删除上一次的输出内容
        return true;
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        // 在这里处理 class
        super.transform(transformInvocation)
        //当前是否是增量编译(由isIncremental() 方法的返回和当前编译是否有增量基础)
        boolean isIncremental = transformInvocation.isIncremental();
        //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                //在这里对jar做相应处理
                println 'JarInput*****' + jarInput.getFile().absolutePath
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyFile(jarInput.getFile(), dest);
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);

                //directoryInput.getFile()指向所有源码编译出来的class目录(build\intermediates\classes\release或build\intermediates\classes\debug)
                println 'DirectoryInput*****' + directoryInput.getFile().absolutePath
                printFile(directoryInput.getFile())

                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                FileUtils.copyDirectory(directoryInput.getFile(), dest);
            }
        }
    }

    void printFile(File dir) {
        project.fileTree(dir.absolutePath){ FileTree fileTree ->
            fileTree.visit { FileTreeElement element ->
                println element.name
            }
        }
    }
}




























