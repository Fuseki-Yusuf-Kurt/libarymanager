package de.fuseki.controller;

import de.fuseki.dtos.CreateOrderDto;
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
public class OrderController {

    private final MediaOrderService mediaOrderService;
    @PostMapping("/order")
    public ResponseEntity addOrder(@RequestBody CreateOrderDto createOrderDto) {
        mediaOrderService.addOrder(createOrderDto);
        return ResponseEntity.ok().build();
    }
}
