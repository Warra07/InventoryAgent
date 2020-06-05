package com.inventapi.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    public List<Product> getAllProducts()
    {
        List<Product> productList = repository.findAll();

        if(productList.size() > 0) {
            return productList;
        } else {
            return new ArrayList<Product>();
        }
    }

    public Product getProductById(Long id)
    {
        Optional<Product> product = repository.findById(id);

        if(product.isPresent()) {
            return product.get();
        } else {
            return null;
        }
    }

    public Product createOrUpdateProduct(Product entity)
    {
        Optional<Product> product = repository.findById(entity.getId());

        if(product.isPresent())
        {
            Product newEntity = product.get();
            newEntity.setCategorie(entity.getCategorie());
            newEntity.setnom(entity.getnom());
            newEntity.setPrice(entity.getPrice());
            newEntity.setQuantity(entity.getQuantity());

            newEntity = repository.save(newEntity);

            return newEntity;
        } else {
            entity = repository.save(entity);

            return entity;
        }
    }

    public void deleteProductById(Long id)
    {
        Optional<Product> product = repository.findById(id);

        if(product.isPresent())
        {
            repository.deleteById(id);
        } else {
            System.out.println("Error");
        }
    }
}