package com.edutech.progressive.repository;
import com.edutech.progressive.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {

    Customers findByCustomerId(int customerId);

    void deleteByCustomerId(int customerId);

    // Customers findByEmail(@Param("email") String email);

    // Customers findByUsername(String username);
}