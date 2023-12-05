package com.example.rest.rest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_name")
    private String name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order){
        if(orders == null)orders = new ArrayList();
        orders.add(order);
    }
    public void removeOrder(long orderId){
        orders = orders.stream().filter(o -> !(o.getId() == orderId)).collect(Collectors.toList());
    }
}
