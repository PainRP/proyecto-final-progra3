package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.ArrayList;
import java.util.List;

public class CollectionsTreeStrategy implements TreeAlgorithmStrategy {

    private Node root;

    @Override
    public Node createRoot(Node rootNode) {
        this.root = rootNode;
        return this.root;
    }

    @Override
    public Node addChild(Node parent, Node childNode) {
        parent.addChild(childNode);
        return childNode;
    }

    @Override
    public Node getRoot() {
        return this.root;
    }

    @Override
    public List<Node> getChildren(String parentId) {
        if (root == null) {
            return new ArrayList<>();
        }

        Node parent = findNodeById(root, parentId);

        if (parent == null) {
            return new ArrayList<>();
        }

        return parent.getChildren();
    }

    private Node findNodeById(Node current, String id) {
        if (current.getId() != null && current.getId().equals(id)) {
            return current;
        }

        for (Node child : current.getChildren()) {
            Node found = findNodeById(child, id);

            if (found != null) {
                return found;
            }
        }

        return null;
    }
}