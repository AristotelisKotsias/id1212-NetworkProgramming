package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "CURRENCY")
public class Currency implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "fromCurr")
    private String fromCurr;
    @Basic(optional = false)
    @Column(name = "SEK")
    private double SEK;
    @Basic(optional = false)
    @Column(name = "EURO")
    private double EURO;
    @Basic(optional = false)
    @Column(name = "USD")
    private double USD;
    @Basic(optional = false)
    @Column(name = "GBP")
    private double GBP;
    public Currency() {
    }

    public Currency(String fromCurr) {
        this.fromCurr = fromCurr;
    }

    public Currency(String fromCurr, Double SEK, Double EURO, Double USD, Double POUNDS) {
        this.fromCurr = fromCurr;
        this.SEK = SEK;
        this.EURO = EURO;
        this.USD = USD;
        this.GBP = POUNDS;

    }

    public String convert(String toCurr, Double amount) {
        double result = 0;

        switch (toCurr) {
            case "SEK":
                result = conversionOfCurrency(SEK, amount);
                break;
            case "EURO":
                result = conversionOfCurrency(EURO, amount);
                break;
            case "USD":
                result = conversionOfCurrency(USD, amount);
                break;
            case "GBP":
                result = conversionOfCurrency(GBP, amount);
                break;

        }
        return String.valueOf(result);
    }

    public Double conversionOfCurrency(Double currency, double amount) {
        return amount * currency;

    }

    public String getFromCurr() {
        return fromCurr;
    }

    public void setFromCurr(String fromCurr) {
        this.fromCurr = fromCurr;
    }

    public double getSEK() {
        return SEK;
    }

    public void setSEK(double SEK) {
        this.SEK = SEK;
    }

    public double getEURO() {
        return EURO;
    }

    public void setEURO(double EURO) {
        this.EURO = EURO;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getPOUNDS() {
        return GBP;
    }

    public void setPOUNDS(double POUNDS) {
        this.GBP = POUNDS;
    }  

}