package com.github.davidyzliu.pytapycharmplugin.utils.reporttoolwindow

import com.intellij.ui.treeStructure.Tree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.MutableTreeNode

/**
 * Base declarative builder to start the building process.
 */
fun root(rootContent: String, init: MutableTreeNode.() -> Unit = {}): Tree {
    val rootNode = DefaultMutableTreeNode(rootContent)
    rootNode.apply(init)
    return Tree(rootNode)
}

/**
 * Declarative MutableTreeNode builder which supports creating nested children in the init expression
 * @param content The displayed content of the node
 * @param init The expression containing additional creation-time operations, primarily nested builders
 * **/
fun MutableTreeNode.node(content: String, init: MutableTreeNode.() -> Unit = {}) {
    val node = DefaultMutableTreeNode(content)
    node.apply(init)
    insert(node, childCount)
}
