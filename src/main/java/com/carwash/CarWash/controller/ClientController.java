package com.carwash.CarWash.controller;


import com.carwash.CarWash.entity.Client;
import com.carwash.CarWash.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

//    public ClientController(ClientService clientService) {
//        this.clientService = clientService;
//    }

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<Client> getClientByPhoneNumber(@PathVariable String phoneNumber) {
        Optional<Client> client = clientService.findClientByPhoneNumber(phoneNumber);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client updatedclient) {
        return ResponseEntity.ok(clientService.updateClient(clientId, updatedclient));
    }
}
