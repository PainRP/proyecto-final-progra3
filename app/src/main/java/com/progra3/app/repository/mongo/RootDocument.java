package com.progra3.app.repository.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tree_config")
public class RootDocument {

    @Id
    private String id;
    private String rootId;

    public RootDocument() {
    }

    public RootDocument(String id, String rootId) {
        this.id = id;
        this.rootId = rootId;
    }

    public String getId() {
        return id;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }
}