package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.ArrayList;
import java.util.List;

public class TreeService implements ITreeService {

    public TreeService() {
           }

    @Override
    public Node getRoot() {
        return new Node(
            "1", 
            "1000", 
            "Activo", 
            "ROOT", 
            "Cuentas principales de activos"
        );
    }

    @Override
    public List<Node> getChildren(String parentId) {
        return new ArrayList<>();
    }

    @Override
    public List<Node> search(String query, String method) {
        return new ArrayList<>();
    }
}
