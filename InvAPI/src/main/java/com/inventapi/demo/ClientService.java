package com.inventapi.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    public List<Client> getAllClients()
    {
        List<Client> clientList = repository.findAll();

        if(clientList.size() > 0) {
            return clientList;
        } else {
            return new ArrayList<Client>();
        }
    }

    public Client getClientById(Long id)
    {
        Optional<Client> client = repository.findById(id);

        if(client.isPresent()) {
            return client.get();
        } else {
            return null;
        }
    }

    public Client createOrUpdateClient(Client entity)
    {
        Optional<Client> client = repository.findById(entity.getId());

        if(client.isPresent())
        {
            Client newEntity = client.get();
            newEntity.setNom(entity.getNom());
            newEntity.setEmail(entity.getEmail());
            newEntity.setPrenom(entity.getPrenom());

            newEntity = repository.save(newEntity);

            return newEntity;
        } else {
            entity = repository.save(entity);

            return entity;
        }
    }

    public void deleteClientById(Long id)
    {
        Optional<Client> client = repository.findById(id);

        if(client.isPresent())
        {
            repository.deleteById(id);
        } else {
            System.out.println("Error");
        }
    }
}