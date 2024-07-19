package de.fuseki.controller;

import de.fuseki.dtos.CreateReservationDto;
import de.fuseki.dtos.ReservationDto;
import de.fuseki.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<CreateReservationDto> addReservation(@RequestBody CreateReservationDto reservationDto) {
        return ResponseEntity.ok(reservationService.addReservation(reservationDto));
    }
    @GetMapping("/reservation/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Integer id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }
    @DeleteMapping("/reservation/{id}")
    public ResponseEntity deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

}
