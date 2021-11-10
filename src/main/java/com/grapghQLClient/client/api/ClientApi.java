package com.grapghQLClient.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.grapghQLClient.client.entity.Customer;
import com.grapghQLClient.client.service.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApi.class);
    private static final String DATA="data";
    private static final String CUSTOMERS="customers";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerServiceImpl service;

    @Value("${api.url}")
    private String API_URL;

    @PostMapping("/invokeApi")
    public List<Customer> invokeApi(@RequestBody Object request) throws JsonProcessingException {

        /* For quering get call
       String queryreqs="{\"query\":\"query{vehicles(count: 1) { id, type,modelCode}}\"}";
        For mutation
       String mutationreq="{\"query\":\"mutation{createVehicle(type: \"car\", modelCode: \"a0192\", brandName: \"hr\", launchDate: \"2016-08-16\"){id}}\"}";
       **/

        List<Customer> custList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();

        headers.add("content-type", "application/json");
        try {

            ResponseEntity<Object> response = restTemplate.postForEntity(API_URL, new HttpEntity<>(request, headers), Object.class);

            LOGGER.info("Consumer Response " + response.getBody());

            ObjectMapper mapper = new ObjectMapper();

            if (response != null) {
                JsonNode node = mapper.valueToTree(response.getBody());

                ArrayNode customerArr = (ArrayNode) node.get(DATA).get(CUSTOMERS);

                customerArr.forEach(cust -> {
                    Customer customer = mapper.convertValue(cust, Customer.class);
                    service.saveCustomer(customer);
                    custList.add(customer);
                });
            }

        } catch (RestClientException e) {
            LOGGER.error("Error while invoking API");
        }

        return custList;
    }
}
