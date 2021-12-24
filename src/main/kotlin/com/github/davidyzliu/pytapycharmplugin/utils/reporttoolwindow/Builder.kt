package com.github.davidyzliu.pytapycharmplugin.utils.reporttoolwindow

import javax.swing.tree.DefaultMutableTreeNode

/**
 * Can be implemented to denote objects that can be built into a DefaultMutableTreeNode
 * **/
interface Builder {
    fun build(): DefaultMutableTreeNode
}
