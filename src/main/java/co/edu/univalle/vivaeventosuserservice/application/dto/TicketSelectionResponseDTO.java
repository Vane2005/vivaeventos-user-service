package co.edu.univalle.vivaeventosuserservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketSelectionResponseDTO {
    private String message;
    private Long eventId;
    private String ticketType;
    private Integer quantity;
}
