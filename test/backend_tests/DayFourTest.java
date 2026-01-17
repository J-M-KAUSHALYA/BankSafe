package com.edutech.progressive;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.edutech.progressive.controller.CustomerController;
import com.edutech.progressive.entity.Customers;
import com.edutech.progressive.service.impl.CustomerServiceImplArraylist;
import com.edutech.progressive.service.impl.CustomerServiceImplJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@ExtendWith(SpringExtension.class)
public class DayFourTest {
   @Mock
   private CustomerServiceImplJpa customerServiceImplJpa;

   @Mock
   private CustomerServiceImplArraylist customerServiceImplArraylist;

   @InjectMocks
   private CustomerController customerController;
   private ObjectMapper objectMapper;

   private MockMvc mockMvc;

   @BeforeEach
   void setUp() {
       mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
       objectMapper = new ObjectMapper();
       String url = "jdbc:mysql://localhost:3306/mydb";
       String user = "root";
       String password = "root";
       try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {
           List<String> tableNames = List.of("customers", "accounts", "transactions");
           for (String tableName:tableNames) {
               String deleteQuery = "DELETE FROM " + tableName;
               statement.executeUpdate(deleteQuery);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   // Helper classes to create Objects
   Customers getCustomerObject(Integer id, String name, String email, String username) {
       Customers customers = new Customers();
       if (id!=null) {
           customers.setCustomerId(id.intValue());
       }
       customers.setName(name);
       customers.setEmail(email);
       customers.setUsername(username);
       customers.setPassword("password");
       return customers;
   }

   // Day - 4
   @Test
   public void testMainMethodOutput_Day4() {
       SpringApplication app = new SpringApplication(BankSafeApplication.class);
       app.setDefaultProperties(Collections.singletonMap("server.port", "9999"));
       ConfigurableApplicationContext context = app.run();
       assertNotNull(context);
       context.close();
   }

}



