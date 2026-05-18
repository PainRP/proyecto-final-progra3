package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.ArrayList;
import java.util.List;

public class CollectionsTreeStrategy implements TreeAlgorithmStrategy {

    private Node root;

    /*
     * Estructura usada:
     * - ArrayList para manejar la lista de hijos de cada nodo.
     * - Búsqueda recursiva sobre el árbol para localizar nodos por id.
     * Esta clase solo contiene lógica pura del motor y no interactúa con repositorios.
     */
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