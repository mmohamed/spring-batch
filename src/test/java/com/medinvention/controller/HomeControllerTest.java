package com.medinvention.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

public class HomeControllerTest extends AbstractControllerTest {

    @Test
    public void testHome() throws Exception {
        this.mvc.perform(get("/home").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk());
    }
}
