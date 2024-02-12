package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CustomerData;
import com.example.demo.entity.OrderList;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class OrderService {
    
    @Autowired
    OrderRepository orderRepo;
    
    @Autowired
    CustomerRepository customerRepo;
    
    @Autowired
    private ExcelGenerator excelGenerator;
    
    @Autowired
    public OrderService(OrderRepository orderRepo, CustomerRepository customerRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
    }
    
    public List<Map<String, Object>> getOrders() {
        List<OrderList> orders = orderRepo.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (OrderList order : orders) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("Order_Id", order.getOrderId());
            orderData.put("Product_Name", order.getProductName());
            orderData.put("Price", order.getPrice());
            orderData.put("Quantity", order.getQuantity());

            // Explicitly fetch customer data to avoid lazy loading issues
            CustomerData customerData = order.getCusData();
            if (customerData != null) {
                orderData.put("Cus_Id", customerData.getCId());
            } else {
                orderData.put("Cus_Id", null);
            }

            response.add(orderData);
        }
        return response;
    }
    
    public void generateExcel(LocalDate fromDate, LocalDate toDate, HttpServletResponse response) throws IOException {
        List<OrderList> orders = getOrdersBetweenDates(fromDate, toDate);
        excelGenerator.generateExcel(orders, response);
    }
    
    public List<OrderList> getOrdersForCustomer(int customerId){
        CustomerData customer = customerRepo.findByCId(customerId);
        if (customer != null) {
            return OrderList.getOrdersByCustomerId(orderRepo, customerId);
        } else {
            return Collections.emptyList(); 
        }
    }


    
//    public List<Map<String, Object>> getOrders() {
////        List<OrderList> orders = orderRepo.findAll();
////        return orders.stream().map(this::mapOrderToMap).collect(Collectors.toList());
//        List<OrderList> orders = orderRepo.findAll();
//        List<Map<String, Object>> response = new ArrayList<>();
//        for (OrderList order : orders) {
//            Map<String, Object> orderData = new HashMap<>();
//            orderData.put("Order_Id", order.getOrderId());
//            orderData.put("Product_Name", order.getProductName());
//            orderData.put("Price", order.getPrice());
//            orderData.put("Quantity", order.getQuantity());
//            orderData.put("Cus_Id", order.getCusData());
//            response.add(orderData);
//        }
//        return response;
//    }
    
    
    
  public void createOrder(OrderList order,CustomerData cusData) {
        CustomerData customer = customerRepo.findById(cusData.getCId()).orElse(null);
        if (customer != null) {
            order.setCusData(customer);
            orderRepo.save(order);
        } else {
            System.out.println("Customer not found");
        }
    }
  public List<OrderList> getOrdersBetweenDates(LocalDate fromDate, LocalDate toDate) {
      return OrderList.getOrdersBetweenDates(orderRepo, fromDate, toDate);
  }

    

    private Map<String, Object> mapOrderToMap(OrderList order) {
        Map<String, Object> orderMap = new LinkedHashMap<>();
        orderMap.put("order_Id", order.getOrderId());
        orderMap.put("Product_Name", order.getProductName());
        orderMap.put("Price", order.getPrice());
        orderMap.put("Quantity", order.getQuantity()); 
        orderMap.put("CustomerId",order.getCusData());
        return orderMap;
    }
}


