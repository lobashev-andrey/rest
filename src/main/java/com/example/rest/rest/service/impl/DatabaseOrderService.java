package com.example.rest.rest.service.impl;

import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.DatabaseOrderRepository;
import com.example.rest.rest.repository.OrderRepository;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseOrderService implements OrderService {

    private final DatabaseOrderRepository orderRepository;
    private final ClientService databaseClientService;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                                "Заказ с ID {0} не найден", id
                )));
    }

    @Override
    public Order save(Order order) {
        Client client = databaseClientService.findById(order.getClient().getId());
        order.setClient(client);
//        client.addOrder(order); Почему-то не стали добавлять
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        Client client = databaseClientService.findById(order.getClient().getId());
        Order existedOrder = findById(order.getId());
        BeanUtils.copyNonNullProperties(order, existedOrder);
        existedOrder.setClient(client);  // ОПЯТЬ ЭТО ЗАЧЕМ-ТО
        return orderRepository.save(existedOrder);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }


}
