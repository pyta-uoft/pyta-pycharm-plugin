package com.github.davidyzliu.pytapycharmplugin.services

import com.intellij.openapi.project.Project
import com.github.davidyzliu.pytapycharmplugin.MyBundle
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.LocalFileSystem

class MyProjectService(project: Project) {

    private val PYTHON_TA_FILE_NAME: String = "python_ta.exe"

    private var pythonTAExecutablePath: String = ""

    init {
        val sdk = ProjectRootManager.getInstance(project).projectSdk
        val sdkExe = LocalFileSystem.getInstance().findFileByPath(sdk?.homePath.toString())
        if (sdkExe != null) {
            val pytaExe = sdkExe.parent.findChild(PYTHON_TA_FILE_NAME)
            if (pytaExe != null)
                pythonTAExecutablePath = pytaExe.path
        }
    }

    fun getPythonTAPath(): String {
        return pythonTAExecutablePath
    }

    fun setPythonTAPath(value: String) {
        pythonTAExecutablePath = value
    }

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
