package com.example.rest.rest.web.controller.v1;

import com.example.rest.rest.AbstractTestController;
import com.example.rest.rest.StringTestUtils;
import com.example.rest.rest.exception.EntityNotFoundException;
import com.example.rest.rest.mapper.v1.ClientMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.model.ClientListResponse;
import com.example.rest.rest.web.model.ClientResponse;
import com.example.rest.rest.web.model.OrderResponse;
import com.example.rest.rest.web.model.UpsertClientRequest;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerTest extends AbstractTestController {

    @MockBean
    private ClientService clientService;
    @MockBean
    private ClientMapper clientMapper;

    @Test
    public void whenFindAll_thenReturnAllClients() throws Exception{
        // <<      Создание тестовых данных
        List<Client>clients = new ArrayList<>();
        clients.add(createClient(1L, null));
        Order order = createOrder(1L, 100L, null);
        clients.add(createClient(1L, order));

        List<ClientResponse> clientResponses = new ArrayList<>();
        clientResponses.add(createClientResponse(1L, null));
        OrderResponse orderResponse = createOrderResponse(1L, 100L);
        clientResponses.add(createClientResponse(2L, orderResponse));

        ClientListResponse clientListResponse = new ClientListResponse(clientResponses);
        ////////////////////////////////////// >>  Создание тестовых данных


        // это какие результаты должны быть получены в случае вызова данных методов
        Mockito.when(clientService.findAll()).thenReturn(clients);
        Mockito.when(clientMapper.clientListToResponseList(clients)).thenReturn(clientListResponse);


        // <<  Вызов контроллера  - тут они и подставляются, видимо
        String actualResponse = mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ///////////////////////////////// >>  Вызов контроллера

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_clients_response.json");

        // << Произведение проверок >>
        Mockito.verify(clientService, Mockito.times(1)).findAll();
        Mockito.verify(clientMapper, Mockito.times(1)).clientListToResponseList(clients);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenGetClientById_thenReturnClientById() throws Exception{
        Client client = createClient(1L, null);
        ClientResponse clientResponse = createClientResponse(1L, null);

        Mockito.when(clientService.findById(1L)).thenReturn(client);
        Mockito.when(clientMapper.clientToResponse(client)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/client/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_client_by_id_response.json");

        Mockito.verify(clientService, Mockito.times(1)).findById(1L);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(client);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenCreateClient_thenReturnNewClient() throws Exception{
        Client client = new Client();
        client.setName("Client 1");
        Client createdClient = createClient(1L, null);
        ClientResponse clientResponse = createClientResponse(1L, null);
        UpsertClientRequest request = new UpsertClientRequest("Client 1");

        Mockito.when(clientService.save(client)).thenReturn(createdClient);
        Mockito.when(clientMapper.requestToClient(request)).thenReturn(client);
        Mockito.when(clientMapper.clientToResponse(createdClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(
                post("/api/v1/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/create_client_response.json");

        Mockito.verify(clientService, Mockito.times(1)).save(client);
        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(request);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(createdClient);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenUpdateClient_thenReturnUpdatedClient() throws Exception{
        UpsertClientRequest request = new UpsertClientRequest("New Client 1");
        Client updatedClient = new Client(1L, "New Client 1", new ArrayList<>());
        ClientResponse clientResponse = new ClientResponse(1L, "New Client 1", new ArrayList<>());

        Mockito.when(clientMapper.requestToClient(1L, request)).thenReturn(updatedClient);
        Mockito.when(clientService.update(updatedClient)).thenReturn(updatedClient);
        Mockito.when(clientMapper.clientToResponse(updatedClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(put("/api/v1/client/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/update_client_response.json");

        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(1L, request);
        Mockito.verify(clientService, Mockito.times(1)).update(updatedClient);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(updatedClient);

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenDeleteClientById_thenReturnStatusNoContent() throws Exception{
        mockMvc.perform(delete("/api/v1/client/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(clientService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenFindByIdNotExistedClient_thenReturnError() throws Exception{

//        Mockito.doReturn(status().isNotFound()).when(clientService.findById(500L));
            Mockito.when(clientService.findById(500L)).thenThrow(new EntityNotFoundException("Клиент с ID 500 не найден"));

            var response = mockMvc.perform(get("/api/v1/client/500"))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse();

            response.setCharacterEncoding("UTF-8");

            String actualResponse = response.getContentAsString();
            String expectedResponse = StringTestUtils.readStringFromResource("response/client_by_id_not_found.json");

            Mockito.verify(clientService, Mockito.times(1)).findById(500L);

            JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @Test
    public void whenCreateClientWithEmptyName_thenThrowError() throws Exception{
        var response = mockMvc.perform(post("/api/v1/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpsertClientRequest())))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/empty_client_name_response.json");

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidSizeName")
    public void whenCreateClientWithInvalidSizeName_thenReturnError(String name) throws Exception{
        var response = mockMvc.perform(post("/api/v1/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpsertClientRequest(name))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/client_name_size_exception_response.json");

        JsonAssert.assertJsonEquals(actualResponse, expectedResponse);
    }

    public static Stream<Arguments> invalidSizeName(){
        return Stream.of(
                Arguments.of(RandomString.make(2)),
                Arguments.of(RandomString.make(31))
        );
    }




}