package com.example.demo.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import com.example.demo.repository.OrderRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class OrderList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
	private int OrderId;
	
	 @Column(name = "product_name")
	private String productName;
	 
	 @Column(name = "price")
	private double Price;
	 
	 @Column(name = "quantity")
	private int Quantity;
	
	 @Column(name = "customer_id", insertable = false, updatable = false)
	private int customerId;
	 
	 private LocalDate orderDate;
		
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public CustomerData getCusData() {
		return customerData;
	}
	public void setCusData(CustomerData cusData) {
		this.customerData = cusData;
	}
	public int getOrderId() {
		return OrderId;
	}
	public void setOrderId(int orderId) {
		OrderId = orderId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private CustomerData customerData;
	
    public static List<OrderList> getOrdersBetweenDates(OrderRepository repository, LocalDate fromDate, LocalDate toDate) {
        return repository.findByOrderDateBetween(fromDate, toDate);
    }
    
    public static List<OrderList> getOrdersByCustomerId(OrderRepository repository, int customerId) {
        return repository.findByCustomerData_CId(customerId);
    }

    public double calculateSubtotal() {
        double subtotal = 0.0;
        if (customerData != null && customerData.getOrdersList() != null) {
            for (OrderList orderItem : customerData.getOrdersList()) {
                subtotal += orderItem.getQuantity() * orderItem.getPrice();
            }
        }
        return subtotal;
    }

    public double calculateTaxes() {
    	 double subtotal = calculateSubtotal();
       double taxes = subtotal * 0.05; // Assuming a 5% tax rate
       return taxes;
    }

    public double calculateTotal() {
    	 double subtotal = calculateSubtotal();
       double taxes = calculateTaxes();
       double total = subtotal + taxes;
       return total;
    }

  
	}

