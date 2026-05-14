package com.progra3.treeengine.service;

import java.util.ArrayList;
import java.util.List;

import com.progra3.treeengine.model.Node;

public class CollectionsTreeStrategy implements TreeAlgorithmStrategy {

    @Override
    public Node createRoot(Node rootNode) {
        return rootNode;
    }

    @Override
    public Node addChild(Node parent, Node childNode) {
        if (parent.getChildren() == null) {
            parent.setChildren(new ArrayList<>());
        }

        parent.getChildren().add(childNode);
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
}