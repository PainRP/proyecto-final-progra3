package com.progra3.app.repository;

import java.util.ArrayList;
import java.util.List;

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
    private List<Node> children = new ArrayList<>();

    public NodeDocument() {
    }

    public NodeDocument(Node node) {
        this.id = node.getId();
        this.code = node.getCode();
        this.name = node.getName();
        this.type = node.getType();
        this.description = node.getDescription();
        this.children = node.getChildren();
    }

    public Node toNode() {
        Node node = new Node(id, code, name, type, description);
        node.setChildren(children);
        return node;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}