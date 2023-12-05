package com.example.rest.rest.web.controller.v2;

import com.example.rest.rest.mapper.v2.ClientMapperV2;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.utils.BeanUtils;
import com.example.rest.rest.web.model.ClientListResponse;
import com.example.rest.rest.web.model.ClientResponse;
import com.example.rest.rest.web.model.UpsertClientRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/client")
@RequiredArgsConstructor
public class ClientControllerV2 {

    private final ClientService databaseClientService;
    private final ClientMapperV2 clientMapper;

    @GetMapping
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(clientMapper.clientListToClientListResponse(
                databaseClientService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(clientMapper.clientToResponse(
                databaseClientService.findById(id)));
    }

    @PostMapping("/")
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid UpsertClientRequest request){
        Client newClient = databaseClientService.save(clientMapper.requestToClient(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(clientMapper.clientToResponse(newClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable("id") Long clientId, @RequestBody @Valid UpsertClientRequest request){
        return ResponseEntity.ok().body(clientMapper.clientToResponse(
                databaseClientService.update(
                        clientMapper.requestToClient(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        databaseClientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }






}
