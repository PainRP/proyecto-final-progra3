package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.List;

public class CustomTreeStrategy implements TreeAlgorithmStrategy {

    @Override
    public Node createRoot(Node rootNode) {
        return rootNode;
    }

 // En CustomTreeStrategy.java
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
        return new java.util.ArrayList<>();
    }
}