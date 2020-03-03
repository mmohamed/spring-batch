package dev.medinvention.tax;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaxCalculator {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public Tax calulate(Tax tax) {
        try {
            return this.make(tax.getSalary(), tax.getRegistrationDate());
        }
        catch (Exception e) {
            log.error("Unable to valdate Tax : {}", e.getMessage());

            return null;
        }
    }

    public Boolean validate(Tax tax) {
        try {
            return tax.equals(this.make(tax.getSalary(), tax.getRegistrationDate()));
        }
        catch (Exception e) {
            log.error("Unable to valdate Tax : {}", e.getMessage());

            return false;
        }
    }

    private Tax make(Long salary, Date registrationDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date limitDate = simpleDateFormat.parse("01/01/2017");
        Double rate;

        if (null == registrationDate) {
            throw new TaxCalculatorException("To make  Tax, need registration date !");
        }

        if (null == salary) {
            throw new TaxCalculatorException("To make  Tax, need salary !");
        }

        if (registrationDate.after(limitDate)) {
            rate = 0.25;
        }
        else {
            if (salary < 30000) {
                rate = 0.15;
            }
            else if (salary < 50000) {
                rate = 0.23;
            }
            else {
                rate = 0.27;
            }
        }

        Long value = (long) Math.floor(salary * rate);

        return new Tax(salary, value, rate, registrationDate);
    }
}
