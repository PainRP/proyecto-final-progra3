package com.progra3.app.repository.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.progra3.treeengine.model.Node;

@Document(collection = "nodes")
public class NodeDocument {

    @Id
    private String id;
    private String code;
    private String name;
    private String type;
    private String description;
    private String parentId;

    public NodeDocument() {
    }

    public NodeDocument(Node node, String parentId) {
        this.id = node.getId();
        this.code = node.getCode();
        this.name = node.getName();
        this.type = node.getType();
        this.description = node.getDescription();
        this.parentId = parentId;
    }

    public Node toNode() {
        return new Node(id, code, name, type, description);
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}