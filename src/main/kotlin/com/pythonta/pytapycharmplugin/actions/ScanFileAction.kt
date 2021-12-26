package com.pythonta.pytapycharmplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.pythonta.pytapycharmplugin.services.MyProjectService
import com.pythonta.pytapycharmplugin.toolwindow.ReportToolWindowFactory
import com.pythonta.pytapycharmplugin.toolwindow.ReportToolWindowPanel
import com.pythonta.pytapycharmplugin.utils.PytaIssue
import com.pythonta.pytapycharmplugin.utils.ScanUtil

/**
 * Represents an IDE action in which a *single* file is scanned using PythonTA
 * Reference: https://plugins.jetbrains.com/docs/intellij/basic-action-system.html
 * */
class ScanFileAction : AnAction() {

    /**
     * Contains the scanning logic that is executed when the user clicks on the button
     * representing this action.
     * @param e Contains contextual data about the action performed by the user
     * */
    override fun actionPerformed(e: AnActionEvent) {
        // Get the action state
        val project: Project? = e.project
        val pythonSDKPath: String? = project?.service<MyProjectService>()?.getPythonSDKPath()
        val selectedFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)

        if (project === null || selectedFile === null || pythonSDKPath === null) {
            return
        }

        // Run the scan
        val issues: List<PytaIssue> = ScanUtil.scan(pythonSDKPath, selectedFile.path)

        // Display the results
        val toolWindow: ToolWindow = ToolWindowManager
            .getInstance(project)
            .getToolWindow(ReportToolWindowFactory.TOOL_WINDOW_ID)!!

        val reportToolWindow = ReportToolWindowPanel(selectedFile.name, issues)
        toolWindow.contentManager.addContent(reportToolWindow.content)
    }

    /**
     * Contains the code that determines the *state* of the action (ie, whether it is enabled
     * or disabled, whether it is visible or not, etc.) based on user interactions with the IDE.
     * @param e Contains contextual data about the action performed by the user
     * */
    override fun update(e: AnActionEvent) {
        super.update(e)

        val project: Project? = e.project
        if (project === null) {
            e.presentation.isEnabledAndVisible = false
        } else {
            e.presentation.isVisible = true

            val selectedFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
            e.presentation.isEnabled = selectedFile?.extension == "py"
        }
    }
}
