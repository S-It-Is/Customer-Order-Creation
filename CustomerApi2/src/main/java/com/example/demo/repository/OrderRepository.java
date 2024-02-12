package com.example.demo.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OrderList;

@Repository
public interface OrderRepository extends JpaRepository<OrderList, Serializable> {
    List<OrderList> findByOrderDateBetween(LocalDate fromDate, LocalDate toDate);
    List<OrderList> findByCustomerData_CId(int customerId);
}

