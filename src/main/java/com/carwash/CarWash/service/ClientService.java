package com.carwash.CarWash.service;

import com.carwash.CarWash.entity.Client;
import com.carwash.CarWash.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> findClientByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public long countTotalClients() {
        return clientRepository.count(); // Compte le nombre total d'entrées dans la table clients
    }

    public Client updateClient(Long clientId, Client updatedClient) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    client.setName(client.getName());
                    client.setPhoneNumber(client.getPhoneNumber());
                    client.setEmail(updatedClient.getEmail());

                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }
}
