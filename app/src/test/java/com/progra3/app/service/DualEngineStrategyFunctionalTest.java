package com.progra3.app.service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.progra3.treeengine.model.Node;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("memory")
class DualEngineStrategyFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TreeOrchestratorService treeService;

    @Test
    void shouldExposeTreeEndpointSuccessfully() throws Exception {
        Node root = buildTree();

        when(treeService.getFullTree()).thenReturn(root);

        mockMvc.perform(get("/api/tree"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.code").value("ROOT"))
                .andExpect(jsonPath("$.name").value("Raiz"))
                .andExpect(jsonPath("$.children[0].id").value("2"))
                .andExpect(jsonPath("$.children[1].id").value("3"));
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