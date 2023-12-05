package com.example.rest.rest.service.impl;

import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.repository.DatabaseClientRepository;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseClientService implements ClientService {

    private final DatabaseClientRepository repository;


    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Клиент с ID {0} не найден", id)));
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Client client) {
        Client currentClient = findById(client.getId());
        BeanUtils.copyNonNullProperties(client, currentClient);
        return save(currentClient); // Он сюда просто клиента засунул
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
