package com.pythonta.pytapycharmplugin.toolwindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

/**
 * Responsible for creating the custom tool window which displays the results of PythonTA
 * scan to users.
 * */
class ReportToolWindowFactory : ToolWindowFactory {

    companion object {
        /*
        * Same as the ID of the tool window specified in plugin.xml.
        * Used to retrieve this tool window to update its contents to display the results of a scan.
        * */
        const val TOOL_WINDOW_ID = "PythonTA"
    }

    /**
     * Initializes the tool window.
     * @param project The project in which this tool window is open
     * @param toolWindow The parent tool window in which this tool window will fit in.
     * */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val tw = ReportToolWindowPanel()
        toolWindow.contentManager.addContent(tw.content)
    }
}
