package com.example.rest.rest.web.controller.v1;

import com.example.rest.rest.mapper.v1.ClientMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.model.ClientListResponse;
import com.example.rest.rest.web.model.ClientResponse;
import com.example.rest.rest.web.model.ErrorResponse;
import com.example.rest.rest.web.model.UpsertClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
@Tag(name = "Client V1", description = "Client API version V1")
public class ClientController {

    private final ClientService clientServiceImpl;
    private final ClientMapper clientMapper;

    @Operation(
            summary = "Get clients",
            description = "Get all clients",
            tags = {"client"}
    )
    @GetMapping
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(clientMapper
                .clientListToResponseList(clientServiceImpl.findAll()));
    }

    @Operation(
            summary = "Get client by ID",
            description = "Get client by ID. Return id, name and list of orders",
            tags = {"client", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode ="200",
                    content = {
                            @Content(schema = @Schema(implementation = ClientResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable long id){
        return ResponseEntity.ok(clientMapper.clientToResponse(
                clientServiceImpl.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid UpsertClientRequest request){
        Client newClient = clientServiceImpl.save(clientMapper.requestToClient(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientMapper.clientToResponse(newClient));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientResponse> update(@PathVariable Long clientId, @RequestBody UpsertClientRequest request){
        Client updatedClient = clientServiceImpl.update(clientMapper.requestToClient(clientId, request));

        return ResponseEntity.ok(clientMapper.clientToResponse(updatedClient));
    }

    @Operation(
            summary = "Delete client by ID",
            description = "Delete client by ID",
            tags = {"client", "id"}
    )
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable Long clientId){
        clientServiceImpl.deleteById(clientId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
