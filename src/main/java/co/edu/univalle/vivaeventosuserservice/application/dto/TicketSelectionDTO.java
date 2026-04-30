package co.edu.univalle.vivaeventosuserservice.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketSelectionDTO {
    private Long eventId;
    private String ticketType;
    private Integer quantity;
}
