package com.inventapi.demo;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrderController
{
    @Autowired
    OrderService service;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> list = service.getAllOrders();

        return new ResponseEntity<List<Order>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        Order entity = service.getOrderById(id);

        return new ResponseEntity<Order>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrUpdateOrder(@RequestBody Order order)
            throws RecordNotFoundException {
        Order updated = service.createOrUpdateOrder(order);
        return new ResponseEntity<Order>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteOrderById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        service.deleteOrderById(id);
        return HttpStatus.FORBIDDEN;
    }

}
