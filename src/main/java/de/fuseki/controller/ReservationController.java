package de.fuseki.controller;

import de.fuseki.dtos.CreateReservationDto;
import de.fuseki.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<CreateReservationDto> addReservation(@RequestBody CreateReservationDto reservationDto) {
        return ResponseEntity.ok(reservationService.addReservation(reservationDto));
    }
}
