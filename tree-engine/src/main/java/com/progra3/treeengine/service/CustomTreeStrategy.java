package com.progra3.treeengine.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
        List<TreeTraversalNode> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        if ("DFS".equalsIgnoreCase(type)) {
            runDFSCustom(root, null, result);
        } else if ("BFS".equalsIgnoreCase(type)) {
            runBFSCustom(root, result);
        }

        return result;
    }

    private void runDFSCustom(Node current, String parentId, List<TreeTraversalNode> result) {
        if (current == null) {
            return;
        }

        result.add(new TreeTraversalNode(current, parentId));

        if (current.getChildren() != null) {
            for (Node child : current.getChildren()) {
                runDFSCustom(child, current.getId(), result);
            }
        }
    }

    private void runBFSCustom(Node root, List<TreeTraversalNode> result) {
        if (root == null) {
            return;
        }

        Queue<TreeTraversalNode> queue = new LinkedList<>();
        queue.add(new TreeTraversalNode(root, null));

        recursiveBFS(queue, result);
    }

    private void recursiveBFS(Queue<TreeTraversalNode> queue, List<TreeTraversalNode> result) {
        if (queue.isEmpty()) {
            return;
        }

        TreeTraversalNode currentTraversal = queue.poll();
        Node currentNode = currentTraversal.getNode();
        result.add(currentTraversal);

        if (currentNode.getChildren() != null) {
            for (Node child : currentNode.getChildren()) {
                queue.add(new TreeTraversalNode(child, currentNode.getId()));
            }
        }

        recursiveBFS(queue, result);
    }

    @Override
    public int getHeight(Node root) {
        if (root == null) {
            return 0;
        }

        int maxHeight = 0;

        if (root.getChildren() != null) {
            for (Node child : root.getChildren()) {
                int childHeight = getHeight(child);
                if (childHeight > maxHeight) {
                    maxHeight = childHeight;
                }
            }
        }

        return 1 + maxHeight;
    }

    @Override
    public boolean hasCycle(Node root) {
        if (root == null) {
            return false;
        }

        List<Node> visited = new ArrayList<>();
        List<Node> visiting = new ArrayList<>();

        return detectCycleDFS(root, visited, visiting);
    }

    private boolean detectCycleDFS(Node current, List<Node> visited, List<Node> visiting) {
        if (current == null) {
            return false;
        }

        if (visiting.contains(current)) {
            return true;
        }

        if (visited.contains(current)) {
            return false;
        }

        visiting.add(current);

        if (current.getChildren() != null) {
            for (Node child : current.getChildren()) {
                if (detectCycleDFS(child, visited, visiting)) {
                    return true;
                }
            }
        }

        visiting.remove(current);
        visited.add(current);

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