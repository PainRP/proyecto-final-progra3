package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.List;
import java.util.Map;

public interface TreeAlgorithmStrategy {
    Node createRoot(Node rootNode);
    Node addChild(Node parent, Node childNode);
    List<Node> getPathFromRoot(Node root, String nodeId);
    List<TreeTraversalNode> getTraversal(Node root, String type);
    int getHeight(Node root);
    boolean hasCycle(Node root);
    Node buildFullTree(Map<String, Node> flatNodes);
    Node getSubtree(Node root, String nodeId);
    int getDepth(Node root, String nodeId);
    List<Node> getAncestors(Node root, String nodeId);
}