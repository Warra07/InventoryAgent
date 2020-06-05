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
@RequestMapping("/clients")
public class ClientController
{
    @Autowired
    ClientService service;

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> list = service.getAllClients();

        return new ResponseEntity<List<Client>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        Client entity = service.getClientById(id);

        return new ResponseEntity<Client>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Client> createOrUpdateClient(@RequestBody Client client)
            throws RecordNotFoundException {
        Client updated = service.createOrUpdateClient(client);
        return new ResponseEntity<Client>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteClientById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        service.deleteClientById(id);
        return HttpStatus.FORBIDDEN;
    }

}
