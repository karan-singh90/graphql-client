package com.grapghQLClient.client.service;

import com.grapghQLClient.client.entity.Customer;
import com.grapghQLClient.client.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository repository;

    @Override
    public Customer saveCustomer(Customer obj) {

       return repository.save(obj);
    }
}
