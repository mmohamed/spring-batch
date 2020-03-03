package dev.medinvention.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.medinvention.controller.exception.BadRequestException;
import dev.medinvention.controller.exception.HTTPClientException;
import dev.medinvention.tax.Tax;
import dev.medinvention.tax.TaxCalculator;

@RestController
@RequestMapping("tax")
public class TaxController {

    @Autowired
    private TaxCalculator taxCalculator;

    @PostMapping(path = "/calculate")
    public Tax calculate(@RequestBody Tax tax) throws HTTPClientException {

        if (null == tax.getSalary() || null == tax.getRegistrationDate()) {
            throw new BadRequestException("Need salary and registration date to calculate tax !");
        }

        return taxCalculator.calulate(tax);
    }

    @PostMapping(path = "/validate")
    public Boolean validate(@RequestBody Tax tax) throws HTTPClientException {

        if (null == tax.getSalary() || null == tax.getRegistrationDate() || null == tax.getRate()) {
            throw new BadRequestException("Need salary, registration date and rate to validate tax !");
        }

        return taxCalculator.validate(tax);
    }
}
