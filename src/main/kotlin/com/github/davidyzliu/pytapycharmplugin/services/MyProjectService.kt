package com.github.davidyzliu.pytapycharmplugin.services

import com.intellij.openapi.project.Project
import com.github.davidyzliu.pytapycharmplugin.MyBundle
import com.intellij.openapi.roots.ProjectRootManager

/**
 * The Project Service (singleton) used to contain and maintain the Python SDK Path selected for the plugin's use.
 *
 * @property selectedPythonSDKPath the Python SDK path the user has set to be used
 * @constructor detects and uses the path of the default project SDK
 */
class MyProjectService(project: Project) {

    private var selectedPythonSDKPath: String = ""

    init {
        val sdk = ProjectRootManager.getInstance(project).projectSdk
        if (sdk != null)
            selectedPythonSDKPath = sdk.homePath.toString();
    }

    fun getPythonSDKPath(): String {
        return selectedPythonSDKPath
    }

    fun setPythonSDKPath(value: String) {
        selectedPythonSDKPath = value
    }

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
