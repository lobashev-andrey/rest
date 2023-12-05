package com.example.rest.rest.web.controller.v2;

import com.example.rest.rest.mapper.v2.OrderMapperV2;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/order")
public class OrderControllerV2 {

    private final OrderService databaseOrderService;
    private final OrderMapperV2 orderMapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll(){
        return ResponseEntity.ok(orderMapper.orderListToOrderListResponse(
                databaseOrderService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderMapper.orderToResponse(
                databaseOrderService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request){
        Order newOrder =  databaseOrderService.save(
                orderMapper.requestToOrder(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.orderToResponse(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") Long orderId, @RequestBody UpsertOrderRequest request){
        Order updatedOrder = databaseOrderService.update(
                orderMapper.requestToOrder(orderId, request));

        return ResponseEntity.ok(orderMapper.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        databaseOrderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
