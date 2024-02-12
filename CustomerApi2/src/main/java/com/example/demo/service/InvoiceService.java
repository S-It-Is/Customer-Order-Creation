package com.example.demo.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CustomerData;
import com.example.demo.entity.OrderList;
import com.example.demo.repository.OrderRepository;

@Service
public class InvoiceService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    public void sendInvoiceEmail(int orderId) {
        OrderList order = orderRepository.findById(orderId).orElseThrow();

        // Generate invoice content
        String invoiceContent = generateInvoiceContent(order);

        // Create invoice file
        String fileName = "invoice_" + order.getOrderId() + ".pdf";
        File invoiceFile = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(invoiceFile);
            fileWriter.write(invoiceContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Send invoice email
        String customerEmail = order.getCusData().getEmail();
        String subject = "Invoice for order #" + order.getOrderId();
        String body = "Dear customer,\n\nPlease find attached the invoice for your order #" + order.getOrderId() + ".\n\nThank you for your business!\n\nSincerely,\nThe Order Management Team";

        emailService.sendEmail(customerEmail, subject, body, invoiceFile);
    }

    private String generateInvoiceContent(OrderList order) {
        StringBuilder invoiceContent = new StringBuilder();

        invoiceContent.append("Invoice for order #" + order.getOrderId() + "\n");
        invoiceContent.append("Customer: " + order.getCusData().getUserName() + "\n");
        invoiceContent.append("Customer Email: " + order.getCusData().getEmail() + "\n");
        invoiceContent.append("\n");
        invoiceContent.append("Order Items:\n");

        CustomerData customerData = order.getCusData();

        if (customerData != null) {
            List<OrderList> ordersList = customerData.getOrdersList();

            for (OrderList orderItem : ordersList) {
                invoiceContent.append("  - " + orderItem.getProductName() + " x " + orderItem.getQuantity() + " @ $" + orderItem.getPrice() + "\n");
            }
        }

        invoiceContent.append("\n");
        invoiceContent.append("Subtotal: $" + order.calculateSubtotal() + "\n"); // Use OrderList's calculateSubtotal method
        invoiceContent.append("Taxes: $" + order.calculateTaxes() + "\n"); // Use OrderList's calculateTaxes method
        invoiceContent.append("Total: $" + order.calculateTotal() + "\n"); // Use OrderList's calculateTotal method

        return invoiceContent.toString();
    }


    }



