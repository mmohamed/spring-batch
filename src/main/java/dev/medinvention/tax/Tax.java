package dev.medinvention.tax;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tax {

    private Long salary;

    private Long value;

    private Double rate;

    private Date registrationDate;

    public Tax() {

    }

    public Tax(Long salary, Date registrationDate) {
        super();
        this.salary = salary;
        this.registrationDate = registrationDate;
    }

    public Tax(Long salary, Long value, Double rate, Date registrationDate) {
        super();
        this.salary = salary;
        this.value = value;
        this.rate = rate;
        this.registrationDate = registrationDate;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object tax) {
        if (null == tax) {
            return false;
        }
        if (this.getClass() != tax.getClass()) {
            return false;
        }
        return ((Tax) tax).getSalary() == this.salary && this.getRegistrationDate() == this.registrationDate
                && null != ((Tax) tax).getRate() && ((Tax) tax).getRate().equals(this.rate);
    }

    @Override
    public int hashCode() {
        return (salary != null ? salary.hashCode() : 1) * (registrationDate != null ? registrationDate.hashCode() : 1);
    }

}
