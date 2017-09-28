package com.capgemini.tax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.main.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TaxCalculatorTest {

    @Autowired
    private TaxCalculator taxCalculator;

    @Test
    public void testCalculate() throws Exception {

        assertNull(taxCalculator.calulate(new Tax()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Tax tax = new Tax();

        tax.setSalary(100000L);
        tax.setRegistrationDate(simpleDateFormat.parse("01/10/2017"));

        Tax calculatedTax = taxCalculator.calulate(tax);

        assertEquals((Double) 0.25, calculatedTax.getRate());
        assertEquals((Long) 25000L, calculatedTax.getValue());

        tax.setSalary(100000L);
        tax.setRegistrationDate(simpleDateFormat.parse("01/10/2016"));

        calculatedTax = taxCalculator.calulate(tax);

        assertEquals((Double) 0.27, calculatedTax.getRate());
        assertEquals((Long) 27000L, calculatedTax.getValue());

        tax.setSalary(45000L);

        calculatedTax = taxCalculator.calulate(tax);

        assertEquals((Double) 0.23, calculatedTax.getRate());
        assertEquals((Long) 10350L, calculatedTax.getValue());

        tax.setSalary(20000L);

        calculatedTax = taxCalculator.calulate(tax);

        assertEquals((Double) 0.15, calculatedTax.getRate());
        assertEquals((Long) 3000L, calculatedTax.getValue());
    }

    @Test
    public void testValidate() throws Exception {

        assertFalse(taxCalculator.validate(new Tax()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Tax tax = new Tax();

        tax.setSalary(100000L);
        tax.setRegistrationDate(simpleDateFormat.parse("01/10/2017"));
        tax.setRate(0.25);
        tax.setValue(25000L);

        assertTrue(taxCalculator.validate(tax));

        tax.setRegistrationDate(simpleDateFormat.parse("01/10/2016"));

        assertFalse(taxCalculator.validate(tax));
    }
}
