package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomTreeStrategyTest {

    @Test
    void shouldCalculateHeightCorrectly() {
        CustomTreeStrategy strategy = new CustomTreeStrategy();
        Node root = buildTree();

        int height = strategy.getHeight(root);

        assertEquals(2, height);
    }

    @Test
    void shouldFindNodeCorrectly() {
        CustomTreeStrategy strategy = new CustomTreeStrategy();
        Node root = buildTree();

        Node found = strategy.getSubtree(root, "4");

        assertNotNull(found);
        assertEquals("4", found.getId());
        assertEquals("Nieto A1", found.getName());
    }

    @Test
    void shouldReturnNullWhenNodeDoesNotExist() {
        CustomTreeStrategy strategy = new CustomTreeStrategy();
        Node root = buildTree();

        Node found = strategy.getSubtree(root, "999");

        assertNull(found);
    }

    @Test
    void shouldDetectCycleCorrectly() {
        CustomTreeStrategy strategy = new CustomTreeStrategy();

        Node root = new Node("1", "ROOT", "Raiz", "ROOT", "Nodo raiz");
        Node child = new Node("2", "A", "Hijo A", "FOLDER", "Primer hijo");

        root.addChild(child);
        child.addChild(root);

        assertTrue(strategy.hasCycle(root));
    }

    @Test
    void shouldReturnFalseWhenTreeHasNoCycle() {
        CustomTreeStrategy strategy = new CustomTreeStrategy();
        Node root = buildTree();

        assertFalse(strategy.hasCycle(root));
    }

    private Node buildTree() {
        Node root = new Node("1", "ROOT", "Raiz", "ROOT", "Nodo raiz");

        Node childA = new Node("2", "A", "Hijo A", "FOLDER", "Primer hijo");
        Node childB = new Node("3", "B", "Hijo B", "FOLDER", "Segundo hijo");

        Node grandChildA = new Node("4", "A1", "Nieto A1", "LEAF", "Nieto del hijo A");
        Node grandChildB = new Node("5", "A2", "Nieto A2", "LEAF", "Nieto del hijo A");

        childA.addChild(grandChildA);
        childA.addChild(grandChildB);

        root.addChild(childA);
        root.addChild(childB);

        return root;
    }
}