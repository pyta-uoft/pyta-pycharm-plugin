package com.github.davidyzliu.pytapycharmplugin.utils.reporttoolwindow

import javax.swing.tree.DefaultMutableTreeNode

/**
 * Responsible for creating leaf and non-leaf nodes in a DefaultMutableTreeNode tree.
 * **/
class NodeBuilder(nodeContent: String, init: NodeBuilder.() -> Unit) : Builder {

    private var root: DefaultMutableTreeNode = DefaultMutableTreeNode(nodeContent)

    init {
        init()
    }

    /*
    * Adds a buildable entity to the root of the tree represented by this NodeBuilder
    * */
    private fun add(buildable: Builder) {
        root.add(buildable.build())
    }

    /**
     * Returns the root of the tree that has been constructed.
     * @return The root of the tree
     * **/
    override fun build(): DefaultMutableTreeNode {
        return root
    }

    /*
    * Adds a leaf node to the root of the tree represented by this NodeBuilder
    * */
    fun leaf(content: String) {
        add(LeafBuilder(content))
    }

    /**
     * Creates and adds a node with the provided content to the root of the tree represented
     * by this NodeBuilder
     * @param content The content of the node to be added
     * @param init The initialization function
     * **/
    fun node(content: String, init: NodeBuilder.() -> Unit) {
        val node = NodeBuilder(content, init)
        add(node)
    }
}
