package dev.medinvention.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;

import org.junit.Test;

import dev.medinvention.tax.Tax;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaxControllerTest extends AbstractControllerTest {

    @Test
    public void testValidation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new Tax());

        this.mvc.perform(post("/tax/calculate").content(json).contentType("application/json"))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] Need salary and registration date to calculate tax !"));

        this.mvc.perform(post("/tax/validate").content(json).contentType("application/json"))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] Need salary, registration date and rate to validate tax !"));
    }

    @Test
    public void testCalulate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ObjectMapper mapper = new ObjectMapper();

        String requestJSON = mapper.writeValueAsString(new Tax(100000L, simpleDateFormat.parse("01/10/2017")));
        String resultJSON = mapper
                .writeValueAsString(new Tax(100000L, 25000L, 0.25, simpleDateFormat.parse("01/10/2017")));

        this.mvc.perform(post("/tax/calculate").content(requestJSON).contentType("application/json"))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(resultJSON));
    }

    @Test
    public void testValidate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ObjectMapper mapper = new ObjectMapper();

        String goodRequestJSON = mapper
                .writeValueAsString(new Tax(100000L, 25000L, 0.25, simpleDateFormat.parse("01/10/2017")));
        String badRequestJSON = mapper
                .writeValueAsString(new Tax(100000L, 25000L, 0.25, simpleDateFormat.parse("01/10/2016")));

        this.mvc.perform(post("/tax/validate").content(goodRequestJSON).contentType("application/json"))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));

        this.mvc.perform(post("/tax/validate").content(badRequestJSON).contentType("application/json"))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string("false"));
    }
}
