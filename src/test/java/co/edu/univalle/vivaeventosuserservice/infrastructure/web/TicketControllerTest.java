package co.edu.univalle.vivaeventosuserservice.infrastructure.web;

import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionDTO;
import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionResponseDTO;
import co.edu.univalle.vivaeventosuserservice.application.usecase.SelectTickets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.edu.univalle.vivaeventosuserservice.infrastructure.security.SecurityConfig;
import org.springframework.context.annotation.Import;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(TicketController.class)
@Import(SecurityConfig.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SelectTickets selectTickets;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void selectTickets_ShouldReturnOk_WhenAuthenticated() throws Exception {
        // Arrange
        TicketSelectionDTO request = new TicketSelectionDTO();
        request.setEventId(1L);
        request.setTicketType("VIP");
        request.setQuantity(2);

        TicketSelectionResponseDTO response = new TicketSelectionResponseDTO(
                "Boletas seleccionadas correctamente",
                1L,
                "VIP",
                2
        );

        when(selectTickets.execute(any(TicketSelectionDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/tickets/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Boletas seleccionadas correctamente"))
                .andExpect(jsonPath("$.eventId").value(1))
                .andExpect(jsonPath("$.ticketType").value("VIP"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void selectTickets_ShouldReturnForbidden_WhenNotAuthenticated() throws Exception {
        // Arrange
        TicketSelectionDTO request = new TicketSelectionDTO();

        // Act & Assert
        mockMvc.perform(post("/tickets/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
