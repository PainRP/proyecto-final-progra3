package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;

public class TreeTraversalNode {
    private final Node node;
    private final String parentId;

    public TreeTraversalNode(Node node, String parentId) {
        this.node = node;
        this.parentId = parentId;
    }

    public Node getNode() {
        return node;
    }

    public String getParentId() {
        return parentId;
    }
}

