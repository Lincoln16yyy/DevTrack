package com.devtrack.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.verifyNoInteractions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService service;

    @Test
    void deveListarProjetos() throws Exception {
        // 1. Ensine o mock a responder
        when(service.findAll()).thenReturn(
                List.of(new ProjectResponse(1L, "DevTrack", "Meu projeto", Instant.now()))
        );

        // 2. Faça a requisição e verifique
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("DevTrack"))
                .andExpect(jsonPath("$[0].description").value("Meu projeto"));
    }

    @Test
    void deveRetornar400QuandoNomeVazio() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "", "description": "Sem nome"}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void deveCriarProjetoQuandoDadosValidos() throws Exception {
        // Ensinar o mock responder o create
        when(service.create(any(ProjectRequest.class))).thenReturn(
                new ProjectResponse(1L, "DevTrack", "Meu projeto", Instant.now())
        );

        //  Post com dados validos
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "DevTrack", "description": "Meu projeto"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("DevTrack"))
                .andExpect(jsonPath("$.description").value("Meu projeto"));

        // Confirmar que o Controller chamou o Service
        verify(service).create(any(ProjectRequest.class));
    }
}