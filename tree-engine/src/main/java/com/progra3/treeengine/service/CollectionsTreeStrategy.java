package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
        return new ArrayList<>();
    }

    @Override
    public List<Node> getPathFromRoot(Node root, String nodeId) {
        return new ArrayList<>();
    }

    @Override
    public List<TreeTraversalNode> getTraversal(Node root, String type) {
        List<TreeTraversalNode> result = new ArrayList<>();

        if (root == null || type == null) {
            return result;
        }

        if ("DFS".equalsIgnoreCase(type)) {
            runDFS(root, null, result);
        } else if ("BFS".equalsIgnoreCase(type)) {
            runBFS(root, result);
        }

        return result;
    }

    private void runDFS(Node current, String parentId, List<TreeTraversalNode> result) {
        if (current == null) {
            return;
        }

        result.add(new TreeTraversalNode(current, parentId));

        if (current.getChildren() != null) {
            for (Node child : current.getChildren()) {
                runDFS(child, current.getId(), result);
            }
        }
    }

    private void runBFS(Node root, List<TreeTraversalNode> result) {
        Queue<TreeTraversalNode> queue = new ArrayDeque<>();
        queue.add(new TreeTraversalNode(root, null));

        while (!queue.isEmpty()) {
            TreeTraversalNode currentTraversal = queue.poll();
            Node currentNode = currentTraversal.getNode();

            result.add(currentTraversal);

            if (currentNode.getChildren() != null) {
                for (Node child : currentNode.getChildren()) {
                    queue.add(new TreeTraversalNode(child, currentNode.getId()));
                }
            }
        }
    }

    @Override
    public int getHeight(Node root) {
        if (root == null) {
            return 0;
        }

        if (root.getChildren() == null || root.getChildren().isEmpty()) {
            return 0;
        }

        return 1 + root.getChildren()
                .stream()
                .mapToInt(this::getHeight)
                .max()
                .orElse(0);
    }

    @Override
    public boolean hasCycle(Node root) {
        return hasCycle(root, new HashSet<String>());
    }

    private boolean hasCycle(Node current, Set<String> visited) {
        if (current == null || current.getId() == null) {
            return false;
        }

        if (visited.contains(current.getId())) {
            return true;
        }

        visited.add(current.getId());

        if (current.getChildren() != null) {
            for (Node child : current.getChildren()) {
                if (hasCycle(child, visited)) {
                    return true;
                }
            }
        }

        visited.remove(current.getId());
        return false;
    }

    public Node buildFullTree(Map<String, Node> flatNodes) {
        return null;
    }

    public Node getSubtree(Node root, String nodeId) {
        return null;
    }

    public int getDepth(Node root, String nodeId) {
        return 0;
    }

    public List<Node> getAncestors(Node root, String nodeId) {
        return new ArrayList<>();
    }
}