package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.List;

public interface TreeAlgorithmStrategy {
    Node createRoot(Node rootNode);
    Node addChild(Node parent, Node childNode);
    Node getRoot();
    List<Node> getChildren(String parentId);
}