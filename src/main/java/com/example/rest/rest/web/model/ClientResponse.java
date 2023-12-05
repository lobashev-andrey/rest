package com.example.rest.rest.web.model;

import com.example.rest.rest.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    private Long id;

    private String name;

    private List<OrderResponse> orders = new ArrayList<>();
}
