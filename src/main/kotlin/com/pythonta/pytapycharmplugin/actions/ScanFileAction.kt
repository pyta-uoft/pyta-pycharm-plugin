package com.pythonta.pytapycharmplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentManager
import com.pythonta.pytapycharmplugin.services.MyProjectService
import com.pythonta.pytapycharmplugin.toolwindow.ReportToolWindowFactory
import com.pythonta.pytapycharmplugin.utils.PytaPluginUtils
import com.pythonta.pytapycharmplugin.utils.ScanUtil
import com.pythonta.pytapycharmplugin.utils.reporttoolwindow.ReportToolWindowPanel

/**
 * Represents an IDE action in which a *single* file is scanned using PythonTA
 * */
class ScanFileAction : AnAction() {

    /**
     * Contains the scanning logic that is executed when the user clicks on the button
     * representing this action.
     * @param e Contains contextual data about the action performed by the user
     * */
    override fun actionPerformed(e: AnActionEvent) {

        var selectedFile: VirtualFile? = null

        // Project guaranteed to exist because of update()
        val selectedProject: Project = PlatformDataKeys.PROJECT.getData(e.dataContext)!!
        val pythonSDKPath: String = selectedProject.service<MyProjectService>().getPythonSDKPath()

        val toolWindow: ToolWindow = ToolWindowManager
            .getInstance(selectedProject)
            .getToolWindow(ReportToolWindowFactory.TOOL_WINDOW_ID)!!

        val toolWindowContentManager: ContentManager = toolWindow.contentManager
        // Content will be replaced, no matter the results of the scan
        toolWindowContentManager.removeAllContents(true)

        val reportToolWindow = ReportToolWindowPanel()

        val selectedTextEditor = selectedProject.let { FileEditorManager.getInstance(it).selectedTextEditor }

        if (selectedTextEditor != null) {
            selectedFile = FileDocumentManager.getInstance().getFile(selectedTextEditor.document)
        }

        if (selectedFile == null) {
            // No need to add issues as selected file was null
            addContentToToolWindow(toolWindowContentManager, reportToolWindow)
            return
        }

        val selectedFilePath = selectedFile.path
        val result: String = ScanUtil.scan(pythonSDKPath, selectedFilePath)
        reportToolWindow.addIssuesToPanel(PytaPluginUtils.parsePytaOutputString(result))
        addContentToToolWindow(toolWindowContentManager, reportToolWindow)
    }

    /**
     * Contains the code that determines the *state* of the action (ie, whether it is enabled
     * or disabled, whether it is visible or not, etc.) based on user interactions with the IDE.
     * @param e Contains contextual data about the action performed by the user
     * */
    override fun update(e: AnActionEvent) {
        super.update(e)

        val project: Project? = e.project
        if (project == null) {
            e.presentation.isEnabled = false
        }

        val selectedFile: VirtualFile? = PlatformDataKeys.VIRTUAL_FILE.getData(e.dataContext)

        e.presentation.isVisible =
            (selectedFile != null) && (selectedFile.extension != null && selectedFile.extension == "py")
    }

    /*
    * Adds the contents of the tool window, represented by the "toolWindowPanel" property
    * of ReportToolWindowPanel, to the UI that is displayed to the user.
    * */
    private fun addContentToToolWindow(
        toolWindowContentManager: ContentManager,
        reportToolWindowPanel: ReportToolWindowPanel
    ) {
        val issueContent = toolWindowContentManager
            .factory
            .createContent(reportToolWindowPanel.toolWindowPanel, "Scan Results", false)

        toolWindowContentManager.addContent(issueContent)
    }
}
