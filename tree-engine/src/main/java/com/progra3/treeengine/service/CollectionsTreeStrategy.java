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
        Node parent = getSubtree(this.root, parentId);

        if (parent == null || parent.getChildren() == null) {
            return new ArrayList<>();
        }

        return parent.getChildren();
    }

    @Override
    public List<Node> getPathFromRoot(Node root, String nodeId) {
        List<Node> path = new ArrayList<>();

        if (root == null || nodeId == null) {
            return path;
        }

        findPath(root, nodeId, path);
        return path;
    }

    private boolean findPath(Node current, String nodeId, List<Node> path) {
        if (current == null) {
            return false;
        }

        path.add(current);

        if (nodeId.equals(current.getId())) {
            return true;
        }

        if (current.getChildren() != null) {
            for (Node child : current.getChildren()) {
                if (findPath(child, nodeId, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
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

    @Override
    public Node buildFullTree(Map<String, Node> flatNodes) {
        if (flatNodes == null || flatNodes.isEmpty()) {
            this.root = null;
            return null;
        }

        Node rootNode = null;

        for (Node node : flatNodes.values()) {
            if (node.getChildren() == null) {
                node.setChildren(new ArrayList<>());
            } else {
                node.getChildren().clear();
            }
        }

        // Find system root "0" if it exists in flatNodes
        Node systemRoot = findNodeByCode(flatNodes, "0");

        for (Node node : flatNodes.values()) {
            String code = node.getCode();
            if (code == null) continue;

            if (code.equals("0")) {
                rootNode = node;
            } else if (!code.contains(".")) {
                if (systemRoot != null) {
                    systemRoot.addChild(node);
                } else {
                    rootNode = node;
                }
            } else {
                String parentCode = code.substring(0, code.lastIndexOf("."));
                Node parent = findNodeByCode(flatNodes, parentCode);

                if (parent == null && systemRoot != null) {
                    parent = systemRoot;
                }

                if (parent != null) {
                    parent.addChild(node);
                }
            }
        }

        if (rootNode == null && !flatNodes.isEmpty()) {
            if (systemRoot != null) {
                rootNode = systemRoot;
            } else {
                rootNode = flatNodes.values().iterator().next();
            }
        }

        this.root = rootNode;
        sortChildrenByCode(rootNode);
        return rootNode;
    }

    @Override
    public Node getSubtree(Node root, String nodeId) {
        if (root == null || nodeId == null) {
            return null;
        }

        if (nodeId.equals(root.getId())) {
            return root;
        }

        if (root.getChildren() != null) {
            for (Node child : root.getChildren()) {
                Node found = getSubtree(child, nodeId);

                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    private Node findNodeByCode(Map<String, Node> flatNodes, String code) {
        for (Node node : flatNodes.values()) {
            if (code.equals(node.getCode())) {
                return node;
            }
        }

        return null;
    }

    private void sortChildrenByCode(Node node) {
        if (node == null) {
            return;
        }
        List<Node> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            children.sort((n1, n2) -> {
                String c1 = n1.getCode();
                String c2 = n2.getCode();
                if (c1 == null && c2 == null) return 0;
                if (c1 == null) return 1;
                if (c2 == null) return -1;
                return c1.compareTo(c2);
            });
            for (Node child : children) {
                sortChildrenByCode(child);
            }
        }
    }

    @Override
    public int getDepth(Node root, String nodeId) {
        List<Node> path = getPathFromRoot(root, nodeId);

        if (path.isEmpty()) {
            return 0;
        }

        return path.size() - 1;
    }

    @Override
    public List<Node> getAncestors(Node root, String nodeId) {
        List<Node> path = getPathFromRoot(root, nodeId);
        List<Node> ancestors = new ArrayList<>();

        if (path.size() <= 1) {
            return ancestors;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            ancestors.add(path.get(i));
        }

        return ancestors;
    }
}