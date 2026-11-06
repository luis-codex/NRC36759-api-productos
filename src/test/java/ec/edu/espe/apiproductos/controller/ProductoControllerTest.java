package ec.edu.espe.apiproductos.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void debeConsultarTodosLosProductos() throws Exception {
        mockMvc.perform(get("/api/productos")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void debeConsultarProductoExistente() throws Exception {
        mockMvc.perform(get("/api/productos/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Teclado mecánico")));
    }

    @Test
    void debeResponder404ParaProductoInexistente() throws Exception {
        mockMvc.perform(get("/api/productos/9999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado", is(404))).andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void debeRegistrarProductoValido() throws Exception {
        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombre":"Audífonos","precio":39.99,"stock":8}
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/productos/")));
    }

    @Test
    void debeRechazarPrecioInvalido() throws Exception {
        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombre":"Producto inválido","precio":0,"stock":2}
                        """))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.estado", is(400)));
    }

    @Test
    void debeRechazarStockNegativo() throws Exception {
        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombre":"Producto inválido","precio":10,"stock":-1}
                        """))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.estado", is(400)));
    }

    @Test
    void debeEliminarProductoExistente() throws Exception {
        mockMvc.perform(delete("/api/productos/2")).andExpect(status().isNoContent());
    }

    @Test
    void debeResponder404AlEliminarProductoInexistente() throws Exception {
        mockMvc.perform(delete("/api/productos/9998")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado", is(404)));
    }

    @Test
    void debeDistinguirRutaInexistente() throws Exception {
        mockMvc.perform(get("/ruta-inexistente")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").doesNotExist());
    }
}
