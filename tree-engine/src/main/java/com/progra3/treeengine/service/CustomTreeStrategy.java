package com.progra3.treeengine.service;

import java.util.ArrayList;
import java.util.List;

import com.progra3.treeengine.model.Node;

public class CustomTreeStrategy implements TreeAlgorithmStrategy {

    @Override
    public Node createRoot(Node rootNode) {
        return rootNode;
    }

    @Override
    public Node addChild(Node parent, Node childNode) {
        parent.addChild(childNode);
        return childNode;
    }

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public List<Node> getChildren(String parentId) {
        return new ArrayList<>();
    }

    @Override
    public List<Node> getPathFromRoot(Node root, String nodeId) {
        return new ArrayList<>();
    }

    @Override
    public List<TreeTraversalNode> getTraversal(Node root, String type) {
        return new ArrayList<>();
    }

    @Override
    public int getHeight(Node root) {
        return 0;
    }

    @Override
    public boolean hasCycle(Node root) {
        return false;
    }
}