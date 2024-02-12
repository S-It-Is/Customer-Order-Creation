package com.example.demo.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.CustomerData;

public interface CustomerRepository extends JpaRepository<CustomerData,Serializable> {
	CustomerData findByCId(int CId);
	Optional<CustomerData> findByEmail(String email);
}
