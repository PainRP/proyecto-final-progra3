package com.progra3.treeengine.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private String code;
    private String name;
    private String type;
    private String description;
    private String parentId;
    private List<Node> children;

    
    public Node() {
        this.children = new ArrayList<>();
    }

 
    public Node(String id, String code, String name, String type, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.description = description;
        this.children = new ArrayList<>();
    }

    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public List<Node> getChildren() { return children; }
    public void setChildren(List<Node> children) { this.children = children; }
    
    public void addChild(Node child) {
        this.children.add(child);
    }
}
