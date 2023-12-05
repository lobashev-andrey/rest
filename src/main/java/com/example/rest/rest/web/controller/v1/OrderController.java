package com.example.rest.rest.web.controller.v1;


import com.example.rest.rest.mapper.v1.OrderMapper;
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
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderServiceImpl;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll(){
        return ResponseEntity.ok(
                orderMapper.orderListToOrderListResponse(orderServiceImpl.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){

        return ResponseEntity.ok(
                orderMapper.orderToResponse(orderServiceImpl.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request){
        Order order = orderServiceImpl.save(orderMapper.requestToOrder(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.orderToResponse(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @RequestBody UpsertOrderRequest request){
        Order updatedOrder = orderServiceImpl.update(orderMapper.requestToOrder(id, request));

        return ResponseEntity.ok(orderMapper.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        orderServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
