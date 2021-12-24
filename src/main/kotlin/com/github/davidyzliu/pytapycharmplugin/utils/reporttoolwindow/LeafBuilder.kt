package com.github.davidyzliu.pytapycharmplugin.utils.reporttoolwindow

import javax.swing.tree.DefaultMutableTreeNode

/**
 * Responsible for creating a leaf in a DefaultMutableTreeNode tree.
 * **/
class LeafBuilder(private val leafContent: String) : Builder {

    override fun build(): DefaultMutableTreeNode {
        return DefaultMutableTreeNode(leafContent)
    }
}
