package de.fuseki.controller;

import de.fuseki.dtos.CreateOrderDto;
import de.fuseki.dtos.OrderDto;
import de.fuseki.service.OrderService;
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

    private final OrderService orderService;
    @PostMapping("/order")
    public ResponseEntity addOrder(@RequestBody CreateOrderDto createOrderDto) {
        orderService.addOrder(createOrderDto);
        return ResponseEntity.ok().build();
    }
}
