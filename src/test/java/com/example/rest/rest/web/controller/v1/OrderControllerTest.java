package com.example.rest.rest.web.controller.v1;

import com.example.rest.rest.AbstractTestController;
import com.example.rest.rest.StringTestUtils;
import com.example.rest.rest.mapper.v1.OrderMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.model.OrderListResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OrderControllerTest extends AbstractTestController {

    @MockBean
    OrderService orderService;
    @MockBean
    OrderMapper orderMapper;



    @Test
    public void whenFindAll_thenReturnAllOrders() throws Exception{
        Client client = createClient(1L, null);
        Order order = createOrder(1L, 100L, client);
        Order order2 = createOrder(2L, 200L, client);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        OrderResponse orderResponse = createOrderResponse(1L, 100L);
        OrderResponse orderResponse2 = createOrderResponse(2L, 200L);
        List<OrderResponse> orderResponses = new ArrayList<>();
        orderResponses.add(orderResponse);
        orderResponses.add(orderResponse2);

        OrderListResponse orderListResponse = new OrderListResponse(orderResponses);

        Mockito.when(orderService.findAll()).thenReturn(orders);
        Mockito.when(orderMapper.orderListToOrderListResponse(orders)).thenReturn(orderListResponse);


        String actualResponse = mockMvc.perform(get("/api/v1/order"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_orders_response.json");

        Mockito.verify(orderService, Mockito.times(1)).findAll();
        Mockito.verify(orderMapper, Mockito.times(1)).orderListToOrderListResponse(orders);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenFindById_thenReturnOrderById() throws Exception {
        Order order = createOrder(1L, 100L, new Client());

        Mockito.when(orderService.findById(1L)).thenReturn(order);
        Mockito.when(orderMapper.orderToResponse(order)).thenReturn(createOrderResponse(1L, 100L));

        String actualResponse = mockMvc.perform(get("/api/v1/order/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_order_by_id_response.json");

        Mockito.verify(orderService, Mockito.times(1)).findById(1L);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(order);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenCreateOrder_thenReturnNewOrder() throws Exception {
        Order order = createOrder(1L, 100L, null);

        UpsertOrderRequest upsertOrderRequest = new UpsertOrderRequest(1L, "Test product 1", new BigDecimal(100));

        OrderResponse orderResponse = createOrderResponse(1L, 100L);

        Mockito.when(orderMapper.requestToOrder(upsertOrderRequest)).thenReturn(order);
        Mockito.when(orderService.save(order)).thenReturn(order);
        Mockito.when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);


        String actualResponse = mockMvc.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(upsertOrderRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/create_order_response.json");

        Mockito.verify(orderService, Mockito.times(1)).save(order);
        Mockito.verify(orderMapper, Mockito.times(1)).requestToOrder(upsertOrderRequest);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(order);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenCreateOrderWithEmptyProduct_thenThrowException() throws Exception {
        UpsertOrderRequest request = new UpsertOrderRequest(1L, "", new BigDecimal(100));

        var response = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/empty_product_name_response.json");

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }



}
