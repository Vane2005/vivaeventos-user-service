package co.edu.univalle.vivaeventosuserservice.application.usecase;

import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionDTO;
import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class SelectTickets {

    public TicketSelectionResponseDTO execute(TicketSelectionDTO request) {
        // En un escenario real, aquí se validaría la disponibilidad con el servicio de tickets
        // o se guardaría una reserva temporal. Por ahora, retornamos éxito.
        
        return new TicketSelectionResponseDTO(
                "Boletas seleccionadas correctamente",
                request.getEventId(),
                request.getTicketType(),
                request.getQuantity()
        );
    }
}
