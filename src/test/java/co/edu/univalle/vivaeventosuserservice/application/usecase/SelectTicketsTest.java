package co.edu.univalle.vivaeventosuserservice.application.usecase;

import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionDTO;
import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SelectTicketsTest {

    private SelectTickets selectTickets;

    @BeforeEach
    void setUp() {
        selectTickets = new SelectTickets();
    }

    @Test
    void execute_ShouldReturnSuccessResponse() {
        // Arrange
        TicketSelectionDTO request = new TicketSelectionDTO();
        request.setEventId(1L);
        request.setTicketType("VIP");
        request.setQuantity(2);

        // Act
        TicketSelectionResponseDTO response = selectTickets.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("Boletas seleccionadas correctamente", response.getMessage());
        assertEquals(1L, response.getEventId());
        assertEquals("VIP", response.getTicketType());
        assertEquals(2, response.getQuantity());
    }
}
