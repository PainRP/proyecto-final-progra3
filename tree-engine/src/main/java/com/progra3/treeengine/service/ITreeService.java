package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.List;

public interface ITreeService {
    Node getRoot();
    List<Node> getChildren(String parentId);
    List<Node> search(String query, String method);
}
