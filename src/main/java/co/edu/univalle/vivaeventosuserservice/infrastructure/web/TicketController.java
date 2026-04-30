package co.edu.univalle.vivaeventosuserservice.infrastructure.web;

import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionDTO;
import co.edu.univalle.vivaeventosuserservice.application.dto.TicketSelectionResponseDTO;
import co.edu.univalle.vivaeventosuserservice.application.usecase.SelectTickets;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final SelectTickets selectTickets;

    public TicketController(SelectTickets selectTickets) {
        this.selectTickets = selectTickets;
    }

    @PostMapping("/select")
    public ResponseEntity<TicketSelectionResponseDTO> selectTickets(@RequestBody TicketSelectionDTO request) {
        TicketSelectionResponseDTO response = selectTickets.execute(request);
        return ResponseEntity.ok(response);
    }
}
