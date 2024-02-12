package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.CustomerData;
import com.example.demo.entity.OrderList;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.InvoiceService;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	
	@Autowired
	CustomerRepository custoRepo; 
	
	@Autowired
	OrderRepository orderRepo;
	
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	InvoiceService invoiceService;

    @Autowired
    OrderService orderService;

    @GetMapping
    public Map<String, List<Map<String, Object>>> getOrders() {
        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("orders", orderService.getOrders());
        return response;
    }
    
    @GetMapping("/excel")
    public void downloadOrdersExcel(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletResponse response) throws IOException {
        orderService.generateExcel(fromDate, toDate, response);
    }
    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody OrderList orderList) {

        if (orderList.getCustomerId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer ID is required");
        }


        CustomerData customer = custoRepo.findById(orderList.getCustomerId()).orElse(null);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }


        orderList.setCusData(customer);
        
        orderList.setOrderDate(LocalDate.now());

        orderRepo.save(orderList);
        
       invoiceService.sendInvoiceEmail(orderList.getOrderId());

        return ResponseEntity.status(HttpStatus.CREATED).body("Order added successfully");
    }
    

}

