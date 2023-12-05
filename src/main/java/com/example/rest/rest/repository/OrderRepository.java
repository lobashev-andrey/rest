package com.example.rest.rest.repository;

import com.example.rest.rest.exception.UpdateStateException;
import com.example.rest.rest.model.Order;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<Order> findAll();
    Optional<Order> findById(Long id);
    Order save(Order order);
    Order update(Order order);
    void deleteById(Long id);
    void deleteByIdIn(List<Long> ids);

}
