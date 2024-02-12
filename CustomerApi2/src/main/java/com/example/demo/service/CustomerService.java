package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtService;
import com.example.demo.controller.AuthenticationRequest;
import com.example.demo.controller.AuthenticationResponse;
import com.example.demo.controller.RegisterRequest;
import com.example.demo.entity.CustomerData;
import com.example.demo.entity.Role;
import com.example.demo.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepo;
    
    @Autowired
    JwtService jwtService;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
     PasswordEncoder passwordEncoder;
    
    public AuthenticationResponse register(RegisterRequest request) {
        var user = CustomerData.builder()
            .userName(request.getUserName())
            .email(request.getEmail())
            .pass(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
       customerRepo.save(user);
       var jwtToken=jwtService.generateToken(user);
       return AuthenticationResponse.builder()
    		   .token(jwtToken)
    		   .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
    			request.getEmail(),request.getPassword()));
    	
    	var user=customerRepo.findByEmail(request.getEmail()).orElseThrow();
    	var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
     		   .token(jwtToken)
     		   .build();
    }

    public List<Map<String, Object>> getCustomerData() {
        List<CustomerData> customers = customerRepo.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (CustomerData customer : customers) {
            Map<String, Object> cusData = new HashMap<>();
            cusData.put("Customer_Id", customer.getCId());
            cusData.put("Customer_Name", customer.getUserName());
            cusData.put("Customer_Email", customer.getEmail());
            cusData.put("Password", customer.getPassword());
            response.add(cusData);
        }
        return response;
    }
    
    @Transactional
    public CustomerData findCustomerById(int cId) {
        return customerRepo.findByCId(cId);
    }
//
//    public void addCustomer(CustomerData cusData) {
//        customerRepo.save(cusData);
//    }
}

