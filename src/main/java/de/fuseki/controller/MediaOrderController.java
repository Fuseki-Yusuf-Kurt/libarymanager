package de.fuseki.controller;

import de.fuseki.dtos.CreateMediaOrderDto;
import de.fuseki.service.MediaOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MediaOrderController {

    private final MediaOrderService mediaOrderService;
    @PostMapping("/order")
    public ResponseEntity addOrder(@RequestBody CreateMediaOrderDto createMediaOrderDto) {
        mediaOrderService.addOrder(createMediaOrderDto);
        return ResponseEntity.ok().build();
    }
}
