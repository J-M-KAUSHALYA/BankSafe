package com.edutech.progressive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edutech.progressive.BankSafeApplication;
import com.edutech.progressive.entity.CreditCard;
import com.edutech.progressive.repository.CreditCardRepository;
import com.edutech.progressive.service.CreditCardService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankSafeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DayElevenTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CreditCardService creditCardService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws SQLException {
        objectMapper = new ObjectMapper();
        creditCardRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCreditCard_Day11() throws Exception {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("1234567890123456");
        creditCard.setCardHolderName("John Doe");

        mockMvc.perform(post("/credit-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isCreated());

        List<CreditCard> creditCards = creditCardService.getAllCreditCards();

        assertEquals(1, creditCards.size());

        CreditCard addedCreditCard = creditCards.get(0);
        assertEquals("1234567890123456", addedCreditCard.getCardNumber());
        assertEquals("John Doe", addedCreditCard.getCardHolderName());
    }

    @Test
    public void testUpdateCreditCard_Day11() throws Exception {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("1234567890123456");
        creditCard.setCardHolderName("John Doe");

        mockMvc.perform(post("/credit-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isCreated());

        List<CreditCard> creditCards = creditCardService.getAllCreditCards();
        assertEquals(1, creditCards.size());
        Long creditCardId = creditCards.get(0).getId();

        CreditCard updatedCreditCard = new CreditCard();
        updatedCreditCard.setCardHolderName("Jane Doe");

        mockMvc.perform(put("/credit-cards/{id}", creditCardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCreditCard)))
                .andExpect(status().isOk());

        CreditCard retrievedCreditCard = creditCardRepository.findById(creditCardId).orElse(null);
        assertNotNull(retrievedCreditCard);
        assertEquals("Jane Doe", retrievedCreditCard.getCardHolderName());
    }

    @Test
    public void testDeleteCreditCard_Day11() throws Exception {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("1234567890123456");
        creditCard.setCardHolderName("John Doe");

        mockMvc.perform(post("/credit-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isCreated());

        List<CreditCard> creditCards = creditCardService.getAllCreditCards();
        assertEquals(1, creditCards.size());
        Long creditCardId = creditCards.get(0).getId();

        mockMvc.perform(delete("/credit-cards/{id}", creditCardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertFalse(creditCardRepository.existsById(creditCardId));
    }

    @Test
    public void testGetAllCreditCards_Day11() throws Exception {
        CreditCard creditCard1 = new CreditCard();
        creditCard1.setCardNumber("1234567890123456");
        creditCard1.setCardHolderName("John Doe");

        CreditCard creditCard2 = new CreditCard();
        creditCard2.setCardNumber("9876543210987654");
        creditCard2.setCardHolderName("Jane Doe");
        creditCardService.createCreditCard(creditCard1);
        creditCardService.createCreditCard(creditCard2);

        mockMvc.perform(get("/credit-cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Ensure there are two CreditCards in the response
                .andExpect(jsonPath("$[0].cardHolderName", is("John Doe")))
                .andExpect(jsonPath("$[1].cardHolderName", is("Jane Doe")));
    }

    @Test
    public void testGetCreditCardById_Day11() throws Exception {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("1234567890123456");
        creditCard.setCardHolderName("John Doe");
        creditCardRepository.save(creditCard);

        List<CreditCard> creditCards = creditCardService.getAllCreditCards();
        assertEquals(1, creditCards.size());
        Long creditCardId = creditCards.get(0).getId();

        mockMvc.perform(get("/credit-cards/{id}", creditCardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardHolderName", is("John Doe")));
    }
}
