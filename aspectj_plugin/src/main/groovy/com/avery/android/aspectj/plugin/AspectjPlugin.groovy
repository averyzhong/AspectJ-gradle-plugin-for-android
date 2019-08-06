package com.avery.android.aspectj.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AspectjPlugin implements Plugin<Project> {
    final def ASPECTJ_JRT = 'org.aspectj:aspectjrt:1.8.6'
    final def EXTENSION_NAME = 'aspectj'

    @Override
    void apply(Project project) {
        def isApp = project.plugins.withType(AppPlugin)
        def isLib = project.plugins.withType(LibraryPlugin)
        if (!isApp && !isLib) {
            throw new IllegalArgumentException("'android' or 'android-library' plugin required.")
        }
        def log = project.logger
        project.extensions.create(EXTENSION_NAME, AspectjExtension)
        project.dependencies {
            api(ASPECTJ_JRT)
        }
        def variants = isApp ? project.android.applicationVariants : project.android.libraryVariants
        variants.all { variant ->
            JavaCompile javaCompiler
            if (variant.hasProperty('javaCompileProvider')) {
                javaCompiler = variant.javaCompileProvider.get()
            } else {
                javaCompiler = variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile
            }
            javaCompiler.doLast {
                def aopConfig = project.extensions.findByName(EXTENSION_NAME)
                List<String> args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-XnoInline",
                                 "-inpath", it.destinationDir.toString(),
                                 "-aspectpath", it.classpath.asPath,
                                 "-d", it.destinationDir.toString(),
                                 "-classpath", it.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
                ]
                if (aopConfig.generateWeaveLog) {
                    args += "-log"
                    args += "weave.log"
                }
                log.debug "ajc args: " + Arrays.toString(args)
                MessageHandler handler = new MessageHandler(true)
                new Main().run(args as String[], handler)

                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break
                        case IMessage.WARNING:
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break
                    }
                }
            }
        }
    }
}
