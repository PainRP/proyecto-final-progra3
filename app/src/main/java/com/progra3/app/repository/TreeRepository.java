package com.progra3.app.repository;

import com.progra3.treeengine.model.Node;
import java.util.Map;

public interface TreeRepository {
    Node save(Node node);
    Node saveChild(String parentId, Node childNode);
    Node findById(String id);
    void setRootId(String rootId);
    String getRootId();
    Map<String, Node> findAll();
}