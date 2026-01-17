package com.edutech.progressive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edutech.progressive.BankSafeApplication;
import com.edutech.progressive.entity.Loan;
import com.edutech.progressive.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankSafeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DayTenTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private LoanRepository loanRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws SQLException {
        objectMapper = new ObjectMapper();
        loanRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAndGetLoan_day10() throws Exception {
        // Create a loan
        Loan loan = new Loan();
        loan.setLoanType("Home Loan");
        loan.setAmount(100000.0);
        loan.setDuration(3);

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isCreated());

        // Retrieve the loan
        List<Loan> loans = loanRepository.findAll();
        assertEquals(1, loans.size());
        Long loanId = loans.get(0).getId();

        mockMvc.perform(get("/loans/{id}", loanId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(loanId.intValue())))
                .andExpect(jsonPath("$.loanType", is("Home Loan")))
                .andExpect(jsonPath("$.amount", is(100000.0)));
    }

    @Test
    public void testDeleteLoan_day10() throws Exception {
        // Create a loan
        Loan loan = new Loan();
        loan.setLoanType("Personal Loan");
        loan.setAmount(20000.0);
        loan.setDuration(3);
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isCreated());

        // Retrieve the loan
        List<Loan> loans = loanRepository.findAll();
        assertEquals(1, loans.size());
        Long loanId = loans.get(0).getId();

        // Delete the loan
        mockMvc.perform(delete("/loans/{id}", loanId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(loanRepository.existsById(loanId));
    }

    @Test
    public void testUpdateLoan_Day10() throws Exception {
        // Create a loan
        Loan loan = new Loan();
        loan.setLoanType("Car Loan");
        loan.setAmount(50000.0);
        loan.setDuration(3);
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isCreated());

        // Retrieve the loan
        List<Loan> loans = loanRepository.findAll();
        assertEquals(1, loans.size());
        Long loanId = loans.get(0).getId();

        // Update the loan
        loan.setAmount(60000.0);

        mockMvc.perform(put("/loans/{id}", loanId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk());

        // Verify the loan has been updated
        Loan updatedLoan = loanRepository.findById(loanId).orElse(null);
        assertNotNull(updatedLoan);
        assertEquals(60000.0, updatedLoan.getAmount(), 0.01);
    }
}
