package com.github.davidyzliu.pytapycharmplugin.utils.reporttoolwindow

import com.github.davidyzliu.pytapycharmplugin.utils.PytaIssue
import com.github.davidyzliu.pytapycharmplugin.utils.PytaMessage
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree

/**
 * Represents the Report Panel in the Tool Window for PythonTA.
 *
 * This Report Panel is used to display the results of the PythonTA scan to the user. If
 * no file is selected or no scan has been performed, then an appropriate message is
 * displayed to the user.
 * **/
class ReportToolWindowPanel {
    var toolWindowPanel: DialogPanel

    init {
        toolWindowPanel = panel {
            row {
                tree("No report loaded...") {}()
            }
        }
    }

    /**
     * Changes the content of the panel to display the provided list of PythonTA issues,
     * representing the results of a scan performed by the user
     * @param issues A list of PythonTA issues which represent the problems identified by PythonTA
     * **/
    fun addIssuesToPanel(issues: List<PytaIssue>) {
        toolWindowPanel.removeAll()
        toolWindowPanel.add(getParsedTree(issues))
    }

    /*
    * Extracts the required information from the PytaMessage into a string to report
    * to the user.
    * */
    private fun parseMessage(msg: PytaMessage): String {
        return "[${msg.symbol}] (${msg.line}:${msg.column}) ${msg.msg}"
    }

    /*
    * Creates a tree with the root having the provided content and the rest of the tree
    * being initialized by the provided initialization function "init"
    * */
    private fun tree(rootContent: String, init: NodeBuilder.() -> Unit): Tree {
        val builder = NodeBuilder(rootContent, init)
        return Tree(builder.build())
    }

    /*
    * Creates a tree from the list of PythonTA issues to display to the user in the
    * Tool Window on the IDE.
    * */
    private fun getParsedTree(issues: List<PytaIssue>): Tree {
        return tree("PyTA Report: report summary") {
            for (issue: PytaIssue in issues)
                node(issue.filename) {
                    for (message: PytaMessage in issue.msgs)
                        leaf(parseMessage(message))
                }
        }
    }
}
