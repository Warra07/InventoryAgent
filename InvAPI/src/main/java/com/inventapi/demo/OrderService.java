package com.inventapi.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;
    ProduitRepository produitrepository;

    public List<Order> getAllOrders()
    {
        List<Order> orderList = repository.findAll();

        if(orderList.size() > 0) {
            return orderList;
        } else {
            return new ArrayList<Order>();
        }
    }

    public Order getOrderById(Long id)
    {
        Optional<Order> order = repository.findById(id);

        if(order.isPresent()) {
            return order.get();
        } else {
            return null;
        }
    }

    public Order createOrUpdateOrder(Order entity)
    {
        Optional<Order> order = repository.findById(entity.getId());

        if(order.isPresent())
        {
            Order newEntity = order.get();
            newEntity.setDate(entity.getDate());
            newEntity.setClient(entity.getClient());

            for(int i=0; i < entity.getProduits().size(); i++) {
                entity.getProduits().get(i).setOrder(entity);
            }

            newEntity = repository.save(newEntity);

            return newEntity;
        } else {
            for(int i=0; i < entity.getProduits().size(); i++) {
                entity.getProduits().get(i).setOrder(entity);
            }
            System.out.println("hi look here" + entity.getProduits().size());
            entity = repository.save(entity);


            return entity;
        }
    }

    public void deleteOrderById(Long id)
    {
        Optional<Order> order = repository.findById(id);

            repository.deleteById(id);
    }
}
