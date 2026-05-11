package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import java.util.ArrayList;
import java.util.List;

// Importante: No se usa @Service, @Component ni ninguna anotación de Spring.
public class TreeService implements ITreeService {

    public TreeService() {
        // Constructor por defecto, aquí se podría inicializar la carga en memoria del árbol en el futuro
    }

    @Override
    public Node getRoot() {
        // Mock simple de la raíz del Plan de Cuentas Contable
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
        // Devuelve una estructura vacía para que compile y funcione temporalmente
        return new ArrayList<>();
    }

    @Override
    public List<Node> search(String query, String method) {
        // Devuelve una estructura vacía por ahora (Aquí irán los 11 algoritmos)
        return new ArrayList<>();
    }
}
